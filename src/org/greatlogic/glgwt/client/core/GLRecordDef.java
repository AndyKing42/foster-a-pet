package org.greatlogic.glgwt.client.core;

import java.util.TreeMap;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLTable;
/**
 * Provides a mapping of field names to field indexes.
 */
public class GLRecordDef {
//--------------------------------------------------------------------------------------------------
private final TreeMap<String, Integer> _fieldIndexByFieldNameMap; // field name -> field index
private final IGLTable                 _table;
//--------------------------------------------------------------------------------------------------
/**
 * Create a new record definition.
 * @param table The table associated with this record definition.
 * @param fieldNames This will be an array of field names or columns (IGLColumn).
 */
public GLRecordDef(final IGLTable table, final Object[] fieldNames) {
  _table = table;
  _fieldIndexByFieldNameMap = new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);
  if (fieldNames != null) {
    for (int fieldNameIndex = 0; fieldNameIndex < fieldNames.length; ++fieldNameIndex) {
      _fieldIndexByFieldNameMap.put(fieldNames[fieldNameIndex].toString(), fieldNameIndex);
    }
  }
}
//--------------------------------------------------------------------------------------------------
public int addField(final Object fieldName) {
  _fieldIndexByFieldNameMap.put(fieldName.toString(), _fieldIndexByFieldNameMap.size());
  return _fieldIndexByFieldNameMap.size() - 1;
}
//--------------------------------------------------------------------------------------------------
public int getFieldIndex(final IGLColumn column) throws GLInvalidFieldOrColumnException {
  return getFieldIndex(column.toString());
}
//--------------------------------------------------------------------------------------------------
public int getFieldIndex(final String fieldName) throws GLInvalidFieldOrColumnException {
  final Integer result = _fieldIndexByFieldNameMap.get(fieldName);
  if (result == null) {
    throw new GLInvalidFieldOrColumnException(fieldName);
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
public TreeMap<String, Integer> getFieldIndexByFieldNameMap() {
  return _fieldIndexByFieldNameMap;
}
//--------------------------------------------------------------------------------------------------
public int getNumberOfFields() {
  return _fieldIndexByFieldNameMap.size();
}
//--------------------------------------------------------------------------------------------------
public IGLTable getTable() {
  return _table;
}
//--------------------------------------------------------------------------------------------------
@Override
public String toString() {
  return "Table:" + _table + " Columns:" + _fieldIndexByFieldNameMap.keySet();
}
//--------------------------------------------------------------------------------------------------
}