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
import java.io.Serializable;
import java.util.TreeMap;
import java.util.TreeSet;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLTable;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.Change;

public class GLDBUpdate implements Serializable {
//--------------------------------------------------------------------------------------------------
private static final long          serialVersionUID = 1122L;

private boolean                    _inserted;
private TreeMap<IGLColumn, String> _modifiedColumnMap;
private String                     _primaryKeyColumnName;
private String                     _primaryKeyValue;
private IGLTable                   _table;
//--------------------------------------------------------------------------------------------------
/**
 * Provide a default constructor so that objects can be passed to the server using GWT RPC.
 */
@SuppressWarnings("unused")
private GLDBUpdate() {}
//--------------------------------------------------------------------------------------------------
public GLDBUpdate(final GLRecord record) {
  _table = record.getRecordDef().getTable();
  _inserted = record.getInserted();
  _primaryKeyColumnName = _table.getPrimaryKeyColumn().toString();
  _primaryKeyValue = record.getKeyValueAsString();
  _modifiedColumnMap = new TreeMap<>();
}
//--------------------------------------------------------------------------------------------------
GLDBUpdate(final GLRecord modifiedRecord, final TreeSet<IGLColumn> modifiedColumnSet) {
  this(modifiedRecord);
  for (final IGLColumn column : _table.getColumns()) {
    if (modifiedColumnSet.contains(column)) {
      final String value = modifiedRecord.asString(column);
      if (!_inserted || !value.isEmpty()) {
        _modifiedColumnMap.put(column, value);
      }
    }
  }
  populateDefaultValuesForInsert(modifiedColumnSet);
}
//--------------------------------------------------------------------------------------------------
GLDBUpdate(final IGLTable table, final Store<GLRecord>.Record listStoreRecord) {
  this(listStoreRecord.getModel());
  final TreeSet<IGLColumn> updatedColumnSet = new TreeSet<>();
  for (final Change<GLRecord, ?> change : listStoreRecord.getChanges()) {
    final String value = GLClientUtil.formatObjectSpecial(change.getValue());
    if (!_inserted || !value.isEmpty()) {
      final String columnName = change.getChangeTag().toString();
      final IGLColumn column = table.findColumnUsingColumnName(columnName);
      _modifiedColumnMap.put(column, resolveValue(column, value));
      updatedColumnSet.add(column);
    }
  }
  populateDefaultValuesForInsert(updatedColumnSet);
}
//--------------------------------------------------------------------------------------------------
public boolean getInserted() {
  return _inserted;
}
//--------------------------------------------------------------------------------------------------
public TreeMap<IGLColumn, String> getModifiedColumnMap() {
  return _modifiedColumnMap;
}
//--------------------------------------------------------------------------------------------------
public String getPrimaryKeyColumnName() {
  return _primaryKeyColumnName;
}
//--------------------------------------------------------------------------------------------------
public String getPrimaryKeyValue() {
  return _primaryKeyValue;
}
//--------------------------------------------------------------------------------------------------
public IGLTable getTable() {
  return _table;
}
//--------------------------------------------------------------------------------------------------
private void populateDefaultValuesForInsert(final TreeSet<IGLColumn> updatedColumnSet) {
  if (_inserted) {
    for (final IGLColumn column : _table.getColumns()) {
      if (!updatedColumnSet.contains(column) && column.getDefaultValue() != null) {
        _modifiedColumnMap.put(column, column.getDefaultValue().toString());
      }
    }
  }
}
//--------------------------------------------------------------------------------------------------
private String resolveValue(final IGLColumn column, final String value) {
  if (column.getLookupType() == null) {
    return value;
  }
  final IGLTable lookupTable = column.getLookupType().getTable();
  if (lookupTable == null) {
    return value;
  }
  return Integer.toString(GLClientUtil.getLookupCache()
                                      .lookupKeyValueUsingDisplayValue(lookupTable, value));
}
//--------------------------------------------------------------------------------------------------
}