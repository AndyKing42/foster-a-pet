package org.greatlogic.glgwt.client.db;

import java.util.ArrayList;
import java.util.TreeSet;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.event.GLCommitCompleteEvent;
import org.greatlogic.glgwt.shared.IGLColumn;
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
}
//--------------------------------------------------------------------------------------------------
@Override
public void commitChanges() {
  throw new UnsupportedOperationException("Use GLDBUpdater#commitChanges");
}
//--------------------------------------------------------------------------------------------------
void commitChangesAddAllChanges(final StringBuilder sb) {
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
      if (commitChangesAddColumnChange(sb, table, change, firstChange, insert, updatedColumnSet)) {
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
}
//--------------------------------------------------------------------------------------------------
private boolean commitChangesAddColumnChange(final StringBuilder sb, final IGLTable table,
                                             final Change<GLRecord, ?> change,
                                             final boolean firstChange, final boolean insert,
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
public void delete(final GLRecord record) {
  if (_deletedRecordList == null) {
    _deletedRecordList = new ArrayList<>();
  }
  _deletedRecordList.add(record);
}
//--------------------------------------------------------------------------------------------------
public GLRecordDef getRecordDef() {
  return _recordDef;
}
//--------------------------------------------------------------------------------------------------
@Override
public boolean remove(final GLRecord record) {
  throw new UnsupportedOperationException("Use 'GLListStore#delete' to delete records");
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