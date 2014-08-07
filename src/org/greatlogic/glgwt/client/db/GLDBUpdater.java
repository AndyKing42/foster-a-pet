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
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.shared.IGLTable;
import org.greatlogic.glgwt.shared.requestresponse.GLServiceResponse;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.Store;

public class GLDBUpdater {
//--------------------------------------------------------------------------------------------------
private ArrayList<GLRecord>           _insertedRecordList;
private final HashSet<GLListStore>    _listStoreSet;
private final HashSet<GLRecordEditor> _recordEditorSet;
//--------------------------------------------------------------------------------------------------
public GLDBUpdater() {
  _recordEditorSet = new HashSet<>();
  _listStoreSet = new HashSet<>();
}
//--------------------------------------------------------------------------------------------------
private void addToUpdateList(final ArrayList<GLDBUpdate> dbUpdateList, final GLDBUpdate dbUpdate,
                             final GLRecord record) {
  dbUpdateList.add(dbUpdate);
  if (record.getInserted()) {
    if (_insertedRecordList == null) {
      _insertedRecordList = new ArrayList<>();
    }
    _insertedRecordList.add(record);
  }
}
//--------------------------------------------------------------------------------------------------
public void registerRecordEditor(final GLRecordEditor recordEditor) {
  _recordEditorSet.add(recordEditor);
}
//--------------------------------------------------------------------------------------------------
public void registerListStore(final GLListStore listStore) {
  _listStoreSet.add(listStore);
}
//--------------------------------------------------------------------------------------------------
public void saveAllChanges() {
  TreeMap<IGLTable, TreeSet<String>> deletedKeyValueMap = null; // table -> key value set
  final ArrayList<GLDBUpdate> dbUpdateList = new ArrayList<>();
  for (final GLRecordEditor recordEditor : _recordEditorSet) {
    final GLRecord modifiedRecord = recordEditor.getModifiedRecord();
    if (modifiedRecord != null) {
      addToUpdateList(dbUpdateList,
                      new GLDBUpdate(modifiedRecord, recordEditor.getModifiedColumnSet()),
                      modifiedRecord);
    }
  }
  for (final GLListStore listStore : _listStoreSet) {
    if (listStore.getRecordDef() != null) {
      final IGLTable table = listStore.getRecordDef().getTable();
      if (listStore.getModifiedRecords() != null) {
        for (final Store<GLRecord>.Record record : listStore.getModifiedRecords()) {
          addToUpdateList(dbUpdateList, new GLDBUpdate(table, record), record.getModel());
        }
      }
      if (listStore.getDeletedRecordList() != null) {
        for (final GLRecord record : listStore.getDeletedRecordList()) {
          if (deletedKeyValueMap == null) {
            deletedKeyValueMap = new TreeMap<>();
          }
          TreeSet<String> keyValueSet = deletedKeyValueMap.get(table);
          if (keyValueSet == null) {
            keyValueSet = new TreeSet<>();
            deletedKeyValueMap.put(table, keyValueSet);
          }
          keyValueSet.add(record.getKeyValueAsString());
        }
      }
    }
  }
  sendDBChangesToServer(deletedKeyValueMap, dbUpdateList);
}
//--------------------------------------------------------------------------------------------------
private void sendDBChangesToServer(final TreeMap<IGLTable, TreeSet<String>> deletedKeyValueMap,
                                   final ArrayList<GLDBUpdate> dbUpdateList) {
  final AsyncCallback<GLServiceResponse> callback;
  callback = new AsyncCallback<GLServiceResponse>() {
    @Override
    public void onFailure(final Throwable t) {
      GLLog.popup(10, "Database changes failed:" + t.getMessage());
    }
    @Override
    public void onSuccess(final GLServiceResponse result) {
      GLLog.popup(10, "Database changes have been saved on the server");
      if (_insertedRecordList != null) {
        for (final GLRecord record : _insertedRecordList) {
          record.setInserted(false);
        }
      }
      for (final GLRecordEditor recordEditor : _recordEditorSet) {
        recordEditor.commitChangesAfterDBUpdate();
      }
      for (final GLListStore listStore : _listStoreSet) {
        listStore.commitChangesAfterDBUpdate();
      }
    }
  };
  GLClientUtil.getRemoteServiceHelper().applyDBChanges(deletedKeyValueMap, dbUpdateList, callback);
}
//--------------------------------------------------------------------------------------------------
}