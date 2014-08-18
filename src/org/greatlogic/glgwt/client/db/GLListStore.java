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
import java.util.Collection;
import java.util.TreeSet;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.event.GLCommitCompleteEvent;
import org.greatlogic.glgwt.client.event.GLLookupTableLoadedEvent;
import org.greatlogic.glgwt.client.event.GLLookupTableLoadedEvent.IGLLookupTableLoadedEventHandler;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLLookupType;
import org.greatlogic.glgwt.shared.IGLTable;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent.StoreUpdateHandler;
/**
 * A ListStore that contains GLRecord entries.
 */
public class GLListStore extends ListStore<GLRecord> {
//--------------------------------------------------------------------------------------------------
private static final IGLColumn[] EmptyColumnArray = new IGLColumn[0];

private IGLColumn[]              _columns;
private IGLTable                 _currentLookupTable;
private ArrayList<GLRecord>      _deletedRecordList;
private boolean                  _loadLookupTables;
private GLRecordDef              _recordDef;
private GLSQL                    _sql;
//--------------------------------------------------------------------------------------------------
public GLListStore(final GLSQL sql, final boolean loadLookupTables,
                   final Collection<IGLColumn> columns) {
  this(sql, loadLookupTables, columns.toArray(EmptyColumnArray));
}
//--------------------------------------------------------------------------------------------------
public GLListStore(final GLSQL sql, final boolean loadLookupTables, final IGLColumn... columns) {
  super(new ModelKeyProvider<GLRecord>() {
    @Override
    public String getKey(final GLRecord record) {
      return record.getKeyValueAsString();
    }
  });
  _sql = sql;
  _loadLookupTables = loadLookupTables;
  _columns = columns;
  addStoreUpdateHandler(new StoreUpdateHandler<GLRecord>() {
    @Override
    public void onUpdate(final StoreUpdateEvent<GLRecord> event) {
      GLClientUtil.getEventBus().fireEvent(new GLCommitCompleteEvent());
    }
  });
  GLClientUtil.getDBUpdater().registerListStore(this);
}
//--------------------------------------------------------------------------------------------------
@Override
public void commitChanges() {
  throw new UnsupportedOperationException("Use GLDBUpdater#commitChanges");
}
//--------------------------------------------------------------------------------------------------
void commitChangesAfterDBUpdate() {
  GLListStore.super.commitChanges();
  if (_deletedRecordList != null) {
    _deletedRecordList.clear();
  }
}
//--------------------------------------------------------------------------------------------------
public void delete(final GLRecord record) {
  if (_deletedRecordList == null) {
    _deletedRecordList = new ArrayList<>();
  }
  _deletedRecordList.add(record);
  GLListStore.super.remove(record);
}
//--------------------------------------------------------------------------------------------------
public IGLColumn[] getColumns() {
  return _columns;
}
//--------------------------------------------------------------------------------------------------
ArrayList<GLRecord> getDeletedRecordList() {
  return _deletedRecordList;
}
//--------------------------------------------------------------------------------------------------
public GLRecordDef getRecordDef() {
  return _recordDef;
}
//--------------------------------------------------------------------------------------------------
public void load(final IGLListStoreLoadedCallback callback) {
  if (_loadLookupTables) {
    loadLookupTables(callback);
  }
  else {
    loadMainTable(callback);
  }
}
//--------------------------------------------------------------------------------------------------
private void loadLookupTables(final IGLListStoreLoadedCallback callback) {
  TreeSet<IGLTable> lookupTableSet = null;
  for (final IGLColumn column : _columns) {
    final IGLLookupType lookupType = column.getLookupType();
    if (lookupType != null) {
      final IGLTable lookupTable = lookupType.getTable();
      if (lookupTable != null && !GLClientUtil.getLookupCache().getLookupHasBeenLoaded(lookupTable)) {
        if (lookupTableSet == null) {
          lookupTableSet = new TreeSet<>();
        }
        lookupTableSet.add(lookupTable);
      }
    }
  }
  loadNextLookupTable(lookupTableSet, callback);
}
//--------------------------------------------------------------------------------------------------
private void loadMainTable(final IGLListStoreLoadedCallback callback) {
  _sql.executeSelect(this, new IGLSQLSelectCallback() {
    @Override
    public void onFailure(final Throwable t) {
      GLLog.popup(30, _sql.getTable() + " load failed: " + t.getMessage());
    }
    @Override
    public void onSuccess() {
      GLLog.popup(5, _sql.getTable() + " load succeeded (" + size() + " rows)");
      callback.onSuccess();
    }
  });
}
//--------------------------------------------------------------------------------------------------
private void loadNextLookupTable(final TreeSet<IGLTable> lookupTableSet,
                                 final IGLListStoreLoadedCallback callback) {
  if (lookupTableSet == null || lookupTableSet.size() == 0) {
    loadMainTable(callback);
    return;
  }
  final IGLLookupTableLoadedEventHandler eventHandler = new IGLLookupTableLoadedEventHandler() {
    @Override
    public void onLookupTableLoadedEvent(final GLLookupTableLoadedEvent lookupTableLoadedEvent) {
      if (lookupTableLoadedEvent.getTable() == _currentLookupTable) {
        loadLookupTables(callback);
      }
    }
  };
  GLClientUtil.getEventBus().addHandler(GLLookupTableLoadedEvent.LookupTableLoadedEventType,
                                        eventHandler);
  _currentLookupTable = lookupTableSet.first();
  lookupTableSet.remove(_currentLookupTable);
  GLClientUtil.getLookupCache().load(_currentLookupTable, true, false);
}
//--------------------------------------------------------------------------------------------------
@Override
public boolean remove(final GLRecord record) {
  throw new UnsupportedOperationException("Use 'GLListStore#delete' to delete records");
}
//--------------------------------------------------------------------------------------------------
void setRecordDef(final GLRecordDef recordDef) {
  _recordDef = recordDef;
}
//--------------------------------------------------------------------------------------------------
@Override
public String toString() {
  return super.toString();
}
//--------------------------------------------------------------------------------------------------
}