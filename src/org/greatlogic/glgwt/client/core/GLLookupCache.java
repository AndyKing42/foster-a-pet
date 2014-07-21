package org.greatlogic.glgwt.client.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import org.greatlogic.glgwt.client.event.GLLookupTableLoadedEvent;
import org.greatlogic.glgwt.shared.IGLLookupType;
import org.greatlogic.glgwt.shared.IGLTable;

public class GLLookupCache {
//--------------------------------------------------------------------------------------------------
private static final ArrayList<String>                        EmptyAbbrevList;
private final HashMap<IGLLookupType, ArrayList<String>>       _abbrevListByLookupTypeMap;
private final HashMap<IGLLookupType, GLCacheDef>              _cacheDefByNameMap;
private final HashMap<IGLTable, GLCacheDef>                   _cacheDefByTableMap;
private final ArrayList<IGLTable>                             _cachedTableList;
private final HashMap<GLCacheDef, TreeMap<String, GLRecord>>  _displayValueToRecordMapByCacheDefMap;
private final HashMap<GLCacheDef, TreeMap<Integer, GLRecord>> _keyToRecordMapByCacheDefMap;
private final HashMap<GLCacheDef, GLListStore>                _listStoreByCacheDefMap;
//==================================================================================================
private static class GLCacheDef {
private final EGLCacheDefType _cacheDefType;
private final IGLLookupType   _lookupType;
private final IGLTable        _table;
private enum EGLCacheDefType {
ListOfValues,
Table
}
GLCacheDef(final IGLLookupType lookupType) {
  this(EGLCacheDefType.ListOfValues, lookupType, null);
}
GLCacheDef(final IGLTable table) {
  this(EGLCacheDefType.Table, null, table);
}
private GLCacheDef(final EGLCacheDefType cacheDefType, final IGLLookupType lookupType,
                   final IGLTable table) {
  _cacheDefType = cacheDefType;
  _lookupType = lookupType;
  _table = table;
}
}
//==================================================================================================
static {
  EmptyAbbrevList = new ArrayList<>();
}
//--------------------------------------------------------------------------------------------------
public GLLookupCache() {
  _abbrevListByLookupTypeMap = new HashMap<>();
  _cacheDefByNameMap = new HashMap<>();
  _cacheDefByTableMap = new HashMap<>();
  _cachedTableList = new ArrayList<>();
  _displayValueToRecordMapByCacheDefMap = new HashMap<>();
  _keyToRecordMapByCacheDefMap = new HashMap<>();
  _listStoreByCacheDefMap = new HashMap<>();
}
//--------------------------------------------------------------------------------------------------
public void addListCache(final IGLLookupType lookupType, final String... listEntries) {
  final ArrayList<String> abbrevList = new ArrayList<>();
  for (final String listEntry : listEntries) {
    final int barIndex = listEntry.indexOf('|');
    String abbrev;
    String desc;
    if (barIndex > 0) {
      abbrev = listEntry.substring(0, barIndex);
      desc = barIndex < listEntry.length() - 1 ? listEntry.substring(barIndex + 1) : "";
    }
    else {
      abbrev = listEntry;
      desc = "";
    }
    abbrevList.add(abbrev);
  }
  _abbrevListByLookupTypeMap.put(lookupType, abbrevList);
}
//--------------------------------------------------------------------------------------------------
private GLListStore addTableCache(final IGLTable table) {
  final GLCacheDef cacheDef = findCacheDef(table);
  GLListStore result = _listStoreByCacheDefMap.get(cacheDef);
  if (result != null) {
    return result;
  }
  result = new GLListStore();
  _displayValueToRecordMapByCacheDefMap.put(cacheDef, new TreeMap<String, GLRecord>());
  _keyToRecordMapByCacheDefMap.put(cacheDef, new TreeMap<Integer, GLRecord>());
  _listStoreByCacheDefMap.put(cacheDef, result);
  return result;
}
//--------------------------------------------------------------------------------------------------
private GLCacheDef findCacheDef(final IGLTable lookupTable) {
  GLCacheDef result = _cacheDefByTableMap.get(lookupTable);
  if (result == null) {
    result = new GLCacheDef(lookupTable);
    _cacheDefByTableMap.put(lookupTable, result);
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
public ArrayList<String> getAbbrevList(final IGLLookupType lookupType) {
  final ArrayList<String> result = _abbrevListByLookupTypeMap.get(lookupType);
  return result == null ? EmptyAbbrevList : result;
}
//--------------------------------------------------------------------------------------------------
public GLListStore getListStore(final IGLTable table) {
  return addTableCache(table);
}
//--------------------------------------------------------------------------------------------------
public String lookupDisplayValue(final IGLTable lookupTable, final int key) {
  final TreeMap<Integer, GLRecord> keyToRecordMap;
  keyToRecordMap = _keyToRecordMapByCacheDefMap.get(findCacheDef(lookupTable));
  if (keyToRecordMap == null) {
    return "?";
  }
  final GLRecord record = keyToRecordMap.get(key);
  return record == null ? "?" : record.asString(lookupTable.getComboboxColumnMap().get(1));
}
//--------------------------------------------------------------------------------------------------
public int lookupKeyValue(final IGLTable lookupTable, final String displayValue) {
  final GLRecord record = lookupRecord(lookupTable, displayValue);
  if (record == null) {
    return 0;
  }
  return record.asInt(lookupTable.getPrimaryKeyColumnMap().get(1));
}
//--------------------------------------------------------------------------------------------------
public GLRecord lookupRecord(final IGLTable lookupTable, final String displayValue) {
  final TreeMap<String, GLRecord> displayValueToRecordMap;
  displayValueToRecordMap = _displayValueToRecordMapByCacheDefMap.get(findCacheDef(lookupTable));
  if (displayValueToRecordMap == null) {
    return null;
  }
  return displayValueToRecordMap.get(displayValue);
}
//--------------------------------------------------------------------------------------------------
public void reloadAll() {
  for (final IGLTable table : _cachedTableList) {
    reload(table, false);
  }
}
//--------------------------------------------------------------------------------------------------
public void reload(final IGLTable table, final boolean addToReloadList) {
  if (addToReloadList) {
    _cachedTableList.add(table);
  }
  final GLListStore listStore = getListStore(table);
  listStore.clear();
  try {
    final GLSQL sql = GLSQL.select();
    sql.from(table);
    GLLog.popup(20, "Reload of " + table + " was requested");
    sql.executeSelect(listStore, new IGLSQLSelectCallback() {
      @Override
      public void onFailure(final Throwable t) {
        GLLog.popup(30, table + " loading failed: " + t.getMessage());
      }
      @Override
      public void onSuccess() {
        final GLCacheDef cacheDef = findCacheDef(table);
        final TreeMap<String, GLRecord> displayValueToRecordMap;
        displayValueToRecordMap = _displayValueToRecordMapByCacheDefMap.get(cacheDef);
        GLLog.popup(20, "Reload of " + table + " was successful");
        final TreeMap<Integer, GLRecord> keyToRecordMap;
        keyToRecordMap = _keyToRecordMapByCacheDefMap.get(cacheDef);
        if (displayValueToRecordMap == null || keyToRecordMap == null) {
          GLLog.popup(30, "Lookup cache reload failed (map is null)");
          return;
        }
        displayValueToRecordMap.clear();
        keyToRecordMap.clear();
        for (int recordIndex = 0; recordIndex < listStore.size(); ++recordIndex) {
          final GLRecord record = listStore.get(recordIndex);
          displayValueToRecordMap.put(record.asString(table.getComboboxColumnMap().get(1)), record);
          keyToRecordMap.put(record.asInt(table.getPrimaryKeyColumnMap().get(1)), record);
        }
        GLClientUtil.getEventBus().fireEvent(new GLLookupTableLoadedEvent(table));
      }
    });
  }
  catch (final GLDBException dbe) {

  }
}
//--------------------------------------------------------------------------------------------------
}