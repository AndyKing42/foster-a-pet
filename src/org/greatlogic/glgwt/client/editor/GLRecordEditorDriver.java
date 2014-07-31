package org.greatlogic.glgwt.client.editor;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.validation.ConstraintViolation;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.event.GLRecordChangeEvent;
import org.greatlogic.glgwt.client.event.GLRecordChangeEvent.IGLRecordChangeEventHandler;
import org.greatlogic.glgwt.client.ui.GLFieldInitializers;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.EditorVisitor;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.event.AddEvent;
import com.sencha.gxt.widget.core.client.event.AddEvent.AddHandler;
import com.sencha.gxt.widget.core.client.event.RemoveEvent;
import com.sencha.gxt.widget.core.client.event.RemoveEvent.RemoveHandler;
import com.sencha.gxt.widget.core.client.form.BigDecimalField;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.TextField;

public class GLRecordEditorDriver implements EditorDriver<GLRecord>, IGLRecordChangeEventHandler {
//--------------------------------------------------------------------------------------------------
private static TreeMap<String, IGLTable>              _tableByTableNameMap;

private final HashMap<Container, HandlerRegistration> _containerAddHandlerMap;
private final HashMap<IGLColumn, HasValue<?>>         _hasValueByColumnMap;
private final HashMap<IGLTable, GLRecord>             _modifiedRecordMap;
private final HashMap<IGLTable, GLRecord>             _originalRecordMap;
private final HashMap<Container, HandlerRegistration> _removeHandlerMap;
//--------------------------------------------------------------------------------------------------
public GLRecordEditorDriver(final Class<? extends Enum<?>> tableEnumClass) {
  if (_tableByTableNameMap == null) {
    _tableByTableNameMap = new TreeMap<>();
    for (final Enum<?> table : tableEnumClass.getEnumConstants()) {
      _tableByTableNameMap.put(table.toString(), ((IGLTable)table));
    }
  }
  _containerAddHandlerMap = new HashMap<>();
  _hasValueByColumnMap = new HashMap<>();
  _modifiedRecordMap = new HashMap<>();
  _originalRecordMap = new HashMap<>();
  _removeHandlerMap = new HashMap<>();
  GLClientUtil.getEventBus().addHandler(GLRecordChangeEvent.RecordChangeEventType, this);
}
//--------------------------------------------------------------------------------------------------
@Override
public void accept(final EditorVisitor visitor) {
  throw new UnsupportedOperationException();
}
//--------------------------------------------------------------------------------------------------
public void addWidget(final Widget widget) {
  if (widget instanceof Container) {
    final Container container = (Container)widget;
    for (final Widget childWidget : container) {
      addWidget(childWidget);
    }
    HandlerRegistration handler = container.addAddHandler(new AddHandler() {
      @Override
      public void onAdd(final AddEvent event) {
        addWidget(event.getWidget());
      }
    });
    _containerAddHandlerMap.put(container, handler);
    handler = container.addRemoveHandler(new RemoveHandler() {
      @Override
      public void onRemove(final RemoveEvent event) {
        removeWidget(event.getWidget());
      }
    });
    _removeHandlerMap.put(container, handler);
  }
  else {
    final IGLColumn column = getColumnFromWidget(widget);
    if (column != null) {
      _hasValueByColumnMap.put(column, (HasValue<?>)widget);
    }
  }
}
//--------------------------------------------------------------------------------------------------
public void edit(final GLRecord... records) {
  for (final GLRecord record : records) {
    final IGLTable table = record.getRecordDef().getTable();
    _originalRecordMap.put(table, record);
    for (final IGLColumn column : table.getColumns()) {
      final HasValue<?> hasValue = _hasValueByColumnMap.get(column);
      if (hasValue != null) {
        setWidgetValue(hasValue, record, column);
      }
    }
  }
}
//--------------------------------------------------------------------------------------------------
@Override
public GLRecord flush() {
  for (final Entry<IGLColumn, HasValue<?>> hasValueEntry : _hasValueByColumnMap.entrySet()) {
    final HasValue<?> hasValue = hasValueEntry.getValue();
    final Object value = hasValue.getValue();
    final String stringValue = value == null ? "" : value.toString();
  }
  return null;
}
//--------------------------------------------------------------------------------------------------
private IGLColumn getColumnFromWidget(final Widget widget) {
  if (!(widget instanceof Component)) {
    return null;
  }
  final String itemId = ((Component)widget).getItemId();
  final int dotIndex = itemId.indexOf('.');
  if (dotIndex < 1 || dotIndex == itemId.length() - 1) {
    return null;
  }
  final String tableName = itemId.substring(0, dotIndex);
  final IGLTable table = _tableByTableNameMap.get(tableName);
  if (table == null) {
    return null;
  }
  final String columnName = itemId.substring(dotIndex + 1);
  return table.findColumnUsingColumnName(columnName);
}
//--------------------------------------------------------------------------------------------------
@Override
public List<EditorError> getErrors() {
  // todo Auto-generated method stub
  return null;
}
//--------------------------------------------------------------------------------------------------
@Override
public boolean hasErrors() {
  // todo Auto-generated method stub
  return false;
}
//--------------------------------------------------------------------------------------------------
@Override
public boolean isDirty() {
  // todo Auto-generated method stub
  return false;
}
//--------------------------------------------------------------------------------------------------
@Override
public void onRecordChangeEvent(final GLRecordChangeEvent recordChangeEvent) {
  final GLRecord changedRecord = recordChangeEvent.getRecord();
  for (final GLRecord originalRecord : _originalRecordMap.values()) {
    if (originalRecord.getRecordDef().getTable() == changedRecord.getRecordDef().getTable()) {
      if (originalRecord.getKeyValueAsString().equals(recordChangeEvent.getRecord()
                                                                       .getKeyValueAsString())) {
        break;
      }
      return;
    }
  }
  setValue(changedRecord.getRecordDef().getTable(), recordChangeEvent.getColumnName(),
           recordChangeEvent.getNewValue());
}
//--------------------------------------------------------------------------------------------------
public void removeWidget(final Widget widget) {
  if (widget instanceof Container) {
    final Container container = (Container)widget;
    for (final Widget childWidget : container) {
      removeWidget(childWidget);
    }
    _containerAddHandlerMap.remove(container);
    _removeHandlerMap.remove(container);
  }
  else {
    final IGLColumn column = getColumnFromWidget(widget);
    if (column != null) {
      _hasValueByColumnMap.remove(column);
    }
  }
}
//--------------------------------------------------------------------------------------------------
@Override
public boolean setConstraintViolations(final Iterable<ConstraintViolation<?>> violations) {
  throw new UnsupportedOperationException();
}
//--------------------------------------------------------------------------------------------------
public void setValue(final IGLTable table, final String columnName, final Object value) {
  final IGLColumn column = table.findColumnUsingColumnName(columnName);
  final HasValue<?> hasValue = _hasValueByColumnMap.get(column);
  if (hasValue != null) {
    setWidgetValue(hasValue, column, value);
  }
}
//--------------------------------------------------------------------------------------------------
private void setWidgetValue(final HasValue<?> hasValue, final GLRecord record,
                            final IGLColumn column) {
  setWidgetValue(hasValue, column, record.asObject(column));
}
//--------------------------------------------------------------------------------------------------
private void setWidgetValue(final HasValue<?> hasValue, final IGLColumn column, final Object value) {
  final String stringValue = value == null ? "" : value.toString();
  GLLog.popup(10, column + ":" + stringValue);
  switch (column.getDataType()) {
    case Boolean: {
      final CheckBox field = (CheckBox)hasValue;
      GLFieldInitializers.initialize(field, column);
      field.setValue(GLClientUtil.stringToBoolean(stringValue));
      break;
    }
    case Currency: {
      final BigDecimalField field = (BigDecimalField)hasValue;
      GLFieldInitializers.initialize(field, column);
      field.setValue(GLClientUtil.stringToDec(stringValue));
      break;
    }
    case Date: {
      final DateField field = (DateField)hasValue;
      GLFieldInitializers.initialize(field, column);
      field.setValue(GLClientUtil.stringToDate(stringValue));
      break;
    }
    case DateTime: {
      final DateField field = (DateField)hasValue;
      GLFieldInitializers.initialize(field, column);
      field.setValue(GLClientUtil.stringToDate(stringValue));
      break;
    }
    case Decimal: {
      final BigDecimalField field = (BigDecimalField)hasValue;
      GLFieldInitializers.initialize(field, column);
      field.setValue(GLClientUtil.stringToDec(stringValue));
      break;
    }
    case Int: {
      final IntegerField field = (IntegerField)hasValue;
      GLFieldInitializers.initialize(field, column);
      field.setValue(GLClientUtil.stringToInt(stringValue));
      break;
    }
    case String: {
      final TextField field = (TextField)hasValue;
      GLFieldInitializers.initialize(field, column);
      field.setValue(stringValue);
      break;
    }
  }
}
//--------------------------------------------------------------------------------------------------
}