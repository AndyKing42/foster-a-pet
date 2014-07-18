package org.greatlogic.glgwt.client.widget;

import java.math.BigDecimal;
import java.util.TreeMap;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.core.GLRecord;
import org.greatlogic.glgwt.client.core.GLUtil;
import org.greatlogic.glgwt.shared.IGLColumn;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;

public class GLValidationRecord {
//--------------------------------------------------------------------------------------------------
private final TreeMap<String, GLColumnConfig<?>> _columnConfigMap; // column name -> GLColumnConfig
private final GridRowEditing<GLRecord>           _gridEditing;
//--------------------------------------------------------------------------------------------------
GLValidationRecord(final TreeMap<String, GLColumnConfig<?>> columnConfigMap,
                   final GridRowEditing<GLRecord> gridEditing) {
  _columnConfigMap = columnConfigMap;
  _gridEditing = gridEditing;
}
//--------------------------------------------------------------------------------------------------
public BigDecimal asDec(final IGLColumn column) {
  return GLUtil.stringToDec(asString(column));
}
//--------------------------------------------------------------------------------------------------
public int asInt(final IGLColumn column) {
  return GLUtil.stringToInt(asString(column));
}
//--------------------------------------------------------------------------------------------------
private Object asObject(final IGLColumn column) {
  final GLColumnConfig<?> columnConfig = _columnConfigMap.get(column.toString());
  if (columnConfig == null) {
    return null;
  }
  final IsField<Object> editor = _gridEditing.getEditor(columnConfig);
  if (editor == null) {
    return null;
  }
  return editor.getValue();
}
//--------------------------------------------------------------------------------------------------
public String asString(final IGLColumn column) {
  final Object value = asObject(column);
  return value == null ? "" : value.toString();
}
//--------------------------------------------------------------------------------------------------
public void setInvalid(final IGLColumn column, final String message) {
  final GLColumnConfig<?> columnConfig = _columnConfigMap.get(column.toString());
  if (columnConfig == null) {
    GLLog.popup(30, "Failed to find the column configuration for column:" + column);
    return;
  }
  columnConfig.setInvalid(message);
}
//--------------------------------------------------------------------------------------------------
}