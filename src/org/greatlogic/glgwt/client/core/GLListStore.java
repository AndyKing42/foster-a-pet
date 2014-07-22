package org.greatlogic.glgwt.client.core;

import java.util.ArrayList;
import java.util.TreeSet;
import org.greatlogic.glgwt.client.event.GLCommitCompleteEvent;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent.StoreUpdateHandler;
/**
 * A ListStore that contains GLRecord entries.
 */
public class GLListStore extends ListStore<GLRecord> {
//--------------------------------------------------------------------------------------------------
private GLRecordDef _recordDef;
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
}
//--------------------------------------------------------------------------------------------------
@Override
public void commitChanges() {
  final StringBuilder sb = new StringBuilder();
  final ArrayList<GLRecord> insertedRecordList = new ArrayList<>();
  final IGLTable table = _recordDef.getTable();
  final String primaryKeyColumnName = table.getPrimaryKeyColumn().toString();
  for (final Record record : getModifiedRecords()) {
    final GLRecord glRecord = record.getModel();
    final boolean insert = glRecord.getInserted();
    sb.append(glRecord.getInserted() ? "I-" : "U-").append(table.toString()).append("/");
    sb.append(primaryKeyColumnName).append("=").append(glRecord.getKeyValueAsString()).append(":");
    boolean firstChange = true;
    final TreeSet<IGLColumn> updatedColumnSet = new TreeSet<>();
    for (final Change<GLRecord, ?> change : record.getChanges()) {
      if (commitChangesAddChange(sb, table, change, firstChange, insert, updatedColumnSet)) {
        firstChange = false;
      }
    }
    if (insert) {
      for (final IGLColumn column : table.getColumns()) {
        if (!updatedColumnSet.contains(column) && column.getDefaultValue() != null) {
          sb.append(";").append(column.toString()).append("=");
          sb.append(column.getDefaultValue().toString());
        }
      }
      insertedRecordList.add(glRecord);
    }
    sb.append("\n");
  }
  sendDBChangesToServer(sb, insertedRecordList, null);
}
//--------------------------------------------------------------------------------------------------
private boolean commitChangesAddChange(final StringBuilder sb, final IGLTable table,
                                       final Change<GLRecord, ?> change, final boolean firstChange,
                                       final boolean insert,
                                       final TreeSet<IGLColumn> updatedColumnSet) {
  final String value = GLClientUtil.formatObjectSpecial(change.getValue());
  if (insert && value.isEmpty()) {
    return false;
  }
  final String columnName = change.getChangeTag().toString();
  final IGLColumn column = table.findColumnUsingColumnName(columnName);
  updatedColumnSet.add(column);
  sb.append(firstChange ? "" : ";").append(columnName).append("=");
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
  return true;
}
//--------------------------------------------------------------------------------------------------
public GLRecordDef getRecordDef() {
  return _recordDef;
}
//--------------------------------------------------------------------------------------------------
@Override
public boolean remove(final GLRecord record) {
  final ArrayList<GLRecord> recordList = new ArrayList<>(1);
  recordList.add(record);
  remove(recordList);
  return true;
}
//--------------------------------------------------------------------------------------------------
public void remove(final ArrayList<GLRecord> recordList) {
  final StringBuilder sb = new StringBuilder();
  final IGLTable table = _recordDef.getTable();
  sb.append("D-").append(table.toString()).append("/");
  sb.append(table.getPrimaryKeyColumn().toString()).append("=");
  boolean firstRecord = true;
  for (final GLRecord record : recordList) {
    sb.append(firstRecord ? "" : ",").append(record.getKeyValueAsString());
    firstRecord = false;
  }
  if (sb.length() > 0) {
    sendDBChangesToServer(sb, null, recordList);
  }
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