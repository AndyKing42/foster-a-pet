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
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.event.GLCommitCompleteEvent;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent.StoreUpdateHandler;
/**
 * A ListStore that contains GLRecord entries.
 */
public class GLListStore extends ListStore<GLRecord> {
//--------------------------------------------------------------------------------------------------
private ArrayList<GLRecord> _deletedRecordList;
private GLRecordDef         _recordDef;
//--------------------------------------------------------------------------------------------------
/**
 * Creates a new GLListStore.
 */
public GLListStore() {
  super(new ModelKeyProvider<GLRecord>() {
    @Override
    public String getKey(final GLRecord record) {
      return record.getKeyValueAsString();
    }
  });
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
ArrayList<GLRecord> getDeletedRecordList() {
  return _deletedRecordList;
}
//--------------------------------------------------------------------------------------------------
public GLRecordDef getRecordDef() {
  return _recordDef;
}
//--------------------------------------------------------------------------------------------------
@Override
public boolean remove(final GLRecord record) {
  throw new UnsupportedOperationException("Use 'GLListStore#delete' to delete records");
}
//--------------------------------------------------------------------------------------------------
public void setRecordDef(final GLRecordDef recordDef) {
  _recordDef = recordDef;
}
//--------------------------------------------------------------------------------------------------
@Override
public String toString() {
  return super.toString();
}
//--------------------------------------------------------------------------------------------------
}