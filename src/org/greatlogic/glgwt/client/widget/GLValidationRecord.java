package org.greatlogic.glgwt.client.widget;
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
import java.math.BigDecimal;
import java.util.TreeMap;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.widget.grid.GLColumnConfig;
import org.greatlogic.glgwt.shared.IGLColumn;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;

public class GLValidationRecord {
//--------------------------------------------------------------------------------------------------
private final TreeMap<String, GLColumnConfig<?>> _columnConfigMap; // column name -> GLColumnConfig
private final GridRowEditing<GLRecord>           _gridEditing;
//--------------------------------------------------------------------------------------------------
public GLValidationRecord(final TreeMap<String, GLColumnConfig<?>> columnConfigMap,
                          final GridRowEditing<GLRecord> gridEditing) {
  _columnConfigMap = columnConfigMap;
  _gridEditing = gridEditing;
}
//--------------------------------------------------------------------------------------------------
public BigDecimal asDec(final IGLColumn column) {
  return GLClientUtil.stringToDec(asString(column));
}
//--------------------------------------------------------------------------------------------------
public int asInt(final IGLColumn column) {
  return GLClientUtil.stringToInt(asString(column));
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