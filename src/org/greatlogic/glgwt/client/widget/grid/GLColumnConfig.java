package org.greatlogic.glgwt.client.widget.grid;
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
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLSharedEnums.EGLColumnDataType;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

public class GLColumnConfig<DataType> extends ColumnConfig<GLRecord, DataType> {
//--------------------------------------------------------------------------------------------------
private final IGLColumn _column;
private int             _columnIndex;
private DateTimeFormat  _dateTimeFormat;
private Field<?>        _field;
private Validator<?>    _validator;
//--------------------------------------------------------------------------------------------------
public GLColumnConfig(final IGLColumn column,
                      final ValueProvider<GLRecord, DataType> valueProvider, final String header,
                      final int width) {
  super(valueProvider, width, header);
  _column = column;
  if (width < 0) {
    if (column.getDefaultGridColumnWidth() < 0) {
      setWidth(column.getDataType().getDefaultGridColumnWidth());
    }
    else {
      setWidth(column.getDefaultGridColumnWidth());
    }
  }
  if (_column != null) {
    _validator = GLClientUtil.getValidators().getColumnValidator(_column);
    final EGLColumnDataType dataType = _column.getDataType();
    if (dataType == EGLColumnDataType.Boolean) {
      setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    }
    else if (dataType.getNumeric() &&
             (_column.getLookupType() == null || _column.getLookupType().getTable() == null)) {
      setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    }
    else {
      setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    }
  }
}
//--------------------------------------------------------------------------------------------------
public void clearInvalid() {
  if (_field != null) {
    _field.clearInvalid();
  }
}
//--------------------------------------------------------------------------------------------------
public IGLColumn getColumn() {
  return _column;
}
//--------------------------------------------------------------------------------------------------
public int getColumnIndex() {
  return _columnIndex;
}
//--------------------------------------------------------------------------------------------------
public DateTimeFormat getDateTimeFormat() {
  return _dateTimeFormat;
}
//--------------------------------------------------------------------------------------------------
public Validator<?> getValidator() {
  return _validator;
}
//--------------------------------------------------------------------------------------------------
public void setColumnIndex(final int columnIndex) {
  _columnIndex = columnIndex;
}
//--------------------------------------------------------------------------------------------------
public void setDateTimeFormat(final DateTimeFormat dateTimeFormat) {
  _dateTimeFormat = dateTimeFormat;
}
//--------------------------------------------------------------------------------------------------
public void setField(final Field<?> field) {
  _field = field;
}
//--------------------------------------------------------------------------------------------------
public void setInvalid(final String message) {
  if (_field == null) {
    GLLog.popup(30, "Failed to find the column configuration field for column:" + _column);
    return;
  }
  _field.forceInvalid(message);
  final Timer timer = new Timer() {
    @Override
    public void run() {
      _field.clearInvalid();
    }
  };
  timer.schedule(5000);
}
//--------------------------------------------------------------------------------------------------
@Override
public String toString() {
  return (_column == null ? "Select" : _column.toString()) + " (" + getHeader() + ")";
}
//--------------------------------------------------------------------------------------------------
}