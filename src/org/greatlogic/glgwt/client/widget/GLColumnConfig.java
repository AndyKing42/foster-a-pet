package org.greatlogic.glgwt.client.widget;

import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.core.GLRecord;
import org.greatlogic.glgwt.client.core.GLUtil;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLEnums.EGLColumnDataType;
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
  setWidth(width < 0 ? column.getDefaultGridColumnWidth() : width);
  if (_column != null) {
    _validator = GLUtil.getValidators().getColumnValidator(_column);
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