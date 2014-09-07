package org.greatlogic.glgwt.client.db;
/*
 * Copyright 2006-2014 Andy King (GreatLogic.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.event.GLLookupTableLoadedEvent;
import org.greatlogic.glgwt.client.event.GLLookupTableLoadedEvent.IGLLookupTableLoadedEventHandler;
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
private final IGLSQLModifier                                  _sqlModifier;
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
public GLLookupCache(final IGLSQLModifier sqlModifier) {
  _sqlModifier = sqlModifier;
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
private GLCacheDef findCacheDef(final IGLTable table) {
  GLCacheDef result = _cacheDefByTableMap.get(table);
  if (result == null) {
    result = new GLCacheDef(table);
    _cacheDefByTableMap.put(table, result);
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
  final GLCacheDef cacheDef = findCacheDef(table);
  return cacheDef == null ? null : _listStoreByCacheDefMap.get(cacheDef);
}
//--------------------------------------------------------------------------------------------------
public boolean getLookupHasBeenLoaded(final IGLTable table) {
  return _cacheDefByTableMap.containsKey(table);
}
//--------------------------------------------------------------------------------------------------
public void load(final IGLTable table, final boolean addToReloadList, final boolean forceReload,
                 final IGLLookupTableLoadedEventHandler eventHandler) {
  if (!forceReload && getLookupHasBeenLoaded(table)) {
    final GLLookupTableLoadedEvent event = new GLLookupTableLoadedEvent(table, false);
    GLClientUtil.getEventBus().fireEvent(event);
    if (eventHandler != null) {
      eventHandler.onLookupTableLoadedEvent(event);
    }
    return;
  }
  try {
    final GLSQL sql = GLSQL.select();
    sql.from(table);
    _sqlModifier.modifySQL(sql);
    final GLListStore listStore = new GLListStore(sql, false, table.getColumns());
    listStore.load(new IGLListStoreLoadedCallback() {
      @Override
      public void onSuccess() {
        if (addToReloadList) {
          _cachedTableList.add(table);
        }
        final GLCacheDef cacheDef = findCacheDef(table);
        _listStoreByCacheDefMap.put(cacheDef, listStore);
        final TreeMap<String, GLRecord> displayValueToRecordMap = new TreeMap<String, GLRecord>();
        _displayValueToRecordMapByCacheDefMap.put(cacheDef, displayValueToRecordMap);
        final TreeMap<Integer, GLRecord> keyToRecordMap = new TreeMap<Integer, GLRecord>();
        _keyToRecordMapByCacheDefMap.put(cacheDef, keyToRecordMap);
        for (int recordIndex = 0; recordIndex < listStore.size(); ++recordIndex) {
          final GLRecord record = listStore.get(recordIndex);
          displayValueToRecordMap.put(record.asString(table.getComboboxColumnMap().get(1)), record);
          keyToRecordMap.put(record.asInt(table.getPrimaryKeyColumn()), record);
        }
        final GLLookupTableLoadedEvent event = new GLLookupTableLoadedEvent(table, true);
        GLClientUtil.getEventBus().fireEvent(event);
        if (eventHandler != null) {
          eventHandler.onLookupTableLoadedEvent(event);
        }
      }
    });
  }
  catch (final GLDBException dbe) {

  }
}
//--------------------------------------------------------------------------------------------------
public String lookupDisplayValueUsingKeyValue(final IGLTable lookupTable, final int key) {
  final GLRecord record = lookupRecordUsingKeyValue(lookupTable, key);
  return record == null ? "?" : record.asString(lookupTable.getComboboxColumnMap().get(1));
}
//--------------------------------------------------------------------------------------------------
public int lookupKeyValueUsingDisplayValue(final IGLTable lookupTable, final String displayValue) {
  final GLRecord record = lookupRecordUsingDisplayValue(lookupTable, displayValue);
  if (record == null) {
    return 0;
  }
  return record.asInt(lookupTable.getPrimaryKeyColumn());
}
//--------------------------------------------------------------------------------------------------
public GLRecord lookupRecordUsingDisplayValue(final IGLTable lookupTable, final String displayValue) {
  final TreeMap<String, GLRecord> displayValueToRecordMap;
  displayValueToRecordMap = _displayValueToRecordMapByCacheDefMap.get(findCacheDef(lookupTable));
  return displayValueToRecordMap == null ? null : displayValueToRecordMap.get(displayValue);
}
//--------------------------------------------------------------------------------------------------
public GLRecord lookupRecordUsingKeyValue(final IGLTable lookupTable, final int key) {
  final TreeMap<Integer, GLRecord> keyToRecordMap;
  keyToRecordMap = _keyToRecordMapByCacheDefMap.get(findCacheDef(lookupTable));
  return keyToRecordMap == null ? null : keyToRecordMap.get(key);
}
//--------------------------------------------------------------------------------------------------
public void reloadAll() {
  for (final IGLTable table : _cachedTableList) {
    load(table, false, true, null);
  }
}
//--------------------------------------------------------------------------------------------------
}