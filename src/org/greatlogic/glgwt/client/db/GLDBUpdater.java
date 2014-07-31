package org.greatlogic.glgwt.client.db;

import java.util.ArrayList;
import java.util.HashSet;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.editor.GLRecordEditorDriver;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GLDBUpdater {
//--------------------------------------------------------------------------------------------------
private final HashSet<GLListStore>          _listStoreSet;
private final HashSet<GLRecordEditorDriver> _recordEditorDriveSet;
//--------------------------------------------------------------------------------------------------
GLDBUpdater() {
  _listStoreSet = new HashSet<>();
  _recordEditorDriveSet = new HashSet<>();
}
//--------------------------------------------------------------------------------------------------
public void addListStore(final GLListStore listStore) {
  _listStoreSet.add(listStore);
}
//--------------------------------------------------------------------------------------------------
public void addRecordEditorDriver(final GLRecordEditorDriver recordEditorDriver) {
  _recordEditorDriveSet.add(recordEditorDriver);
}
//--------------------------------------------------------------------------------------------------
public void commitChanges() {
  final StringBuilder sb = new StringBuilder();
  for (final GLRecordEditorDriver recordEditorDriver : _recordEditorDriveSet) {
    recordEditorDriver.commitChangesAddAllChanges(sb);
  }
  for (final GLListStore listStore : _listStoreSet) {
    listStore.commitChangesAddAllChanges(sb);
  }
  sendDBChangesToServer(sb, insertedRecordList, null);
}
//--------------------------------------------------------------------------------------------------
private void sendDBChangesToServer(final StringBuilder dbChangesSB,
                                   final ArrayList<GLRecord> insertedRecordList,
                                   final ArrayList<GLRecord> deletedRecordList) {
  GLClientUtil.getRemoteService().applyDBChanges(dbChangesSB.toString(), new AsyncCallback<Void>() {
    @Override
    public void onFailure(final Throwable t) {
      GLLog.popup(10, "Database changes failed:" + t.getMessage());
    }
    @Override
    public void onSuccess(final Void result) {
      GLLog.popup(10, "Database changes have been saved on the server");
      if (insertedRecordList != null) {
        for (final GLRecord record : insertedRecordList) {
          record.setInserted(false);
        }
      }
      if (deletedRecordList != null) {
        for (final GLRecord record : deletedRecordList) {
          GLListStore.super.remove(record);
        }
      }
      GLListStore.super.commitChanges();
    }
  });
}
//--------------------------------------------------------------------------------------------------
}