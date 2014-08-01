package org.greatlogic.glgwt.client.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.Change;

public class GLDBUpdater {
//--------------------------------------------------------------------------------------------------
private final HashSet<GLRecordEditorDriver> _editorDriverSet;
private ArrayList<GLRecord>                 _insertedRecordList;
private final HashSet<GLListStore>          _listStoreSet;
//--------------------------------------------------------------------------------------------------
public GLDBUpdater() {
  _editorDriverSet = new HashSet<>();
  _listStoreSet = new HashSet<>();
}
//--------------------------------------------------------------------------------------------------
private void addColumnValueToSB(final StringBuilder sb, final IGLColumn column, final String value,
                                final boolean firstChange, final TreeSet<IGLColumn> updatedColumnSet) {
  updatedColumnSet.add(column);
  sb.append(firstChange ? "" : ";").append(column.toString()).append("=");
  if (column.getLookupType() != null) {
    final IGLTable lookupTable = column.getLookupType().getTable();
    if (lookupTable == null) {
      sb.append(value);
    }
    else {
      sb.append(GLClientUtil.getLookupCache().lookupKeyValue(lookupTable, value));
    }
  }
  else {
    sb.append(value);
  }
}
//--------------------------------------------------------------------------------------------------
private void addDeletesToSB(final StringBuilder sb, final IGLTable table,
                            final ArrayList<GLRecord> recordList) {
  if (recordList == null || recordList.isEmpty()) {
    return;
  }
  sb.append("D-").append(table.toString()).append("/");
  sb.append(table.getPrimaryKeyColumn().toString()).append("=");
  boolean firstRecord = true;
  for (final GLRecord record : recordList) {
    sb.append(firstRecord ? "" : ",").append(record.getKeyValueAsString());
    firstRecord = false;
  }
}
//--------------------------------------------------------------------------------------------------
private void addEditorDriverInsertsAndUpdatesToSB(final StringBuilder sb, final IGLTable table,
                                                  final GLRecord originalRecord,
                                                  final GLRecord modifiedRecord) {
  final boolean insert = originalRecord == null;
  addRecordHeaderToSB(sb, table, modifiedRecord, insert);
  boolean firstChange = true;
  final TreeSet<IGLColumn> updatedColumnSet = new TreeSet<>();
  for (final IGLColumn column : table.getColumns()) {
    final String modifiedValue = modifiedRecord.asString(column);
    final boolean modified = originalRecord != null && //
                             !modifiedValue.equals(originalRecord.asString(column));
    if (modified || (insert && !modifiedValue.isEmpty())) {
      addColumnValueToSB(sb, column, modifiedValue, firstChange, updatedColumnSet);
      firstChange = false;
    }
  }
  if (insert) {
    addInsertedRecordWithDefaultValues(sb, table, modifiedRecord, updatedColumnSet);
  }
  if (!firstChange || insert) {
    sb.append("\n");
  }
}
//--------------------------------------------------------------------------------------------------
private void addInsertedRecordWithDefaultValues(final StringBuilder sb, final IGLTable table,
                                                final GLRecord glRecord,
                                                final TreeSet<IGLColumn> updatedColumnSet) {
  if (_insertedRecordList == null) {
    _insertedRecordList = new ArrayList<>();
  }
  _insertedRecordList.add(glRecord);
  for (final IGLColumn column : table.getColumns()) {
    if (!updatedColumnSet.contains(column) && column.getDefaultValue() != null) {
      sb.append(";").append(column.toString()).append("=");
      sb.append(column.getDefaultValue().toString());
    }
  }
}
//--------------------------------------------------------------------------------------------------
private boolean addListStoreColumnChangeToSB(final StringBuilder sb, final IGLTable table,
                                             final Change<GLRecord, ?> change,
                                             final boolean firstChange, final boolean insert,
                                             final TreeSet<IGLColumn> updatedColumnSet) {
  final String value = GLClientUtil.formatObjectSpecial(change.getValue());
  if (insert && value.isEmpty()) {
    return false;
  }
  final String columnName = change.getChangeTag().toString();
  final IGLColumn column = table.findColumnUsingColumnName(columnName);
  addColumnValueToSB(sb, column, value, firstChange, updatedColumnSet);
  return true;
}
//--------------------------------------------------------------------------------------------------
private void addListStoreInsertsAndUpdatesToSB(final StringBuilder sb, final IGLTable table,
                                               final Store<GLRecord>.Record record) {
  final GLRecord glRecord = record.getModel();
  final boolean insert = glRecord.getInserted();
  addRecordHeaderToSB(sb, table, glRecord, insert);
  boolean firstChange = true;
  final TreeSet<IGLColumn> updatedColumnSet = new TreeSet<>();
  for (final Change<GLRecord, ?> change : record.getChanges()) {
    if (addListStoreColumnChangeToSB(sb, table, change, firstChange, insert, updatedColumnSet)) {
      firstChange = false;
    }
  }
  if (insert) {
    addInsertedRecordWithDefaultValues(sb, table, glRecord, updatedColumnSet);
  }
  sb.append("\n");
}
//--------------------------------------------------------------------------------------------------
private void addRecordHeaderToSB(final StringBuilder sb, final IGLTable table,
                                 final GLRecord record, final boolean insert) {
  final String primaryKeyColumnName = table.getPrimaryKeyColumn().toString();
  sb.append(insert ? "I-" : "U-").append(table.toString()).append("/");
  sb.append(primaryKeyColumnName).append("=").append(record.getKeyValueAsString()).append(":");
}
//--------------------------------------------------------------------------------------------------
public void registerEditorDriver(final GLRecordEditorDriver editorDriver) {
  _editorDriverSet.add(editorDriver);
}
//--------------------------------------------------------------------------------------------------
public void registerListStore(final GLListStore listStore) {
  _listStoreSet.add(listStore);
}
//--------------------------------------------------------------------------------------------------
public void saveAllChanges() {
  final StringBuilder sb = new StringBuilder();
  for (final GLRecordEditorDriver recordEditorDriver : _editorDriverSet) {
    for (final GLRecord modifiedRecord : recordEditorDriver.getModifiedRecords()) {
      addEditorDriverInsertsAndUpdatesToSB(sb, modifiedRecord.getRecordDef().getTable(),
                                           recordEditorDriver.getOriginalRecord(modifiedRecord),
                                           modifiedRecord);
    }
  }
  for (final GLListStore listStore : _listStoreSet) {
    if (listStore.getRecordDef() != null) {
      final IGLTable table = listStore.getRecordDef().getTable();
      for (final Store<GLRecord>.Record record : listStore.getModifiedRecords()) {
        addListStoreInsertsAndUpdatesToSB(sb, table, record);
      }
      addDeletesToSB(sb, table, listStore.getDeletedRecordList());
    }
  }
  sendDBChangesToServer(sb);
}
//--------------------------------------------------------------------------------------------------
private void sendDBChangesToServer(final StringBuilder dbChangesSB) {
  GLClientUtil.getRemoteService().applyDBChanges(dbChangesSB.toString(), new AsyncCallback<Void>() {
    @Override
    public void onFailure(final Throwable t) {
      GLLog.popup(10, "Database changes failed:" + t.getMessage());
    }
    @Override
    public void onSuccess(final Void result) {
      GLLog.popup(10, "Database changes have been saved on the server");
      if (_insertedRecordList != null) {
        for (final GLRecord record : _insertedRecordList) {
          record.setInserted(false);
        }
      }
      for (final GLListStore listStore : _listStoreSet) {
        listStore.commitChangesAfterDBUpdate();
      }
    }
  });
}
//--------------------------------------------------------------------------------------------------
}