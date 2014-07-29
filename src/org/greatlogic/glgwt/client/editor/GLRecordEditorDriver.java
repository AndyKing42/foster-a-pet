package org.greatlogic.glgwt.client.editor;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import javax.validation.ConstraintViolation;
import org.greatlogic.glgwt.client.core.GLRecord;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.EditorVisitor;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.event.AddEvent;
import com.sencha.gxt.widget.core.client.event.AddEvent.AddHandler;
import com.sencha.gxt.widget.core.client.event.RemoveEvent;
import com.sencha.gxt.widget.core.client.event.RemoveEvent.RemoveHandler;

public class GLRecordEditorDriver implements EditorDriver<GLRecord> {
//--------------------------------------------------------------------------------------------------
private static TreeMap<String, IGLTable>              _tableByTableNameMap;

private final HashMap<Container, HandlerRegistration> _addHandlerMap;
private GLRecord                                      _originalRecord;
private final HashMap<Container, HandlerRegistration> _removeHandlerMap;
private final HashMap<IGLColumn, Widget>              _widgetByColumnMap;
//--------------------------------------------------------------------------------------------------
public GLRecordEditorDriver(final Class<? extends Enum<?>> tableEnumClass) {
  if (_tableByTableNameMap == null) {
    _tableByTableNameMap = new TreeMap<>();
    for (final Enum<?> table : tableEnumClass.getEnumConstants()) {
      _tableByTableNameMap.put(table.toString(), ((IGLTable)table));
    }
  }
  _addHandlerMap = new HashMap<>();
  _removeHandlerMap = new HashMap<>();
  _widgetByColumnMap = new HashMap<>();
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
    _addHandlerMap.put(container, handler);
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
      _widgetByColumnMap.put(column, widget);
    }
  }
}
//--------------------------------------------------------------------------------------------------
public void edit(final GLRecord record) {
  _originalRecord = record;
  final IGLTable table = record.getRecordDef().getTable();
  for (final IGLColumn column : table.getColumns()) {
    final Widget widget = _widgetByColumnMap.get(column);
    if (widget != null) {
      setWidgetValue(widget, record, column);
    }
  }
}
//--------------------------------------------------------------------------------------------------
@Override
public GLRecord flush() {
  _originalRecord.
  // todo Auto-generated method stub
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
  final String columnName = itemId.substring(dotIndex);
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
public void removeWidget(final Widget widget) {
  if (widget instanceof Container) {
    final Container container = (Container)widget;
    for (final Widget childWidget : container) {
      removeWidget(childWidget);
    }
    _addHandlerMap.remove(container);
    _removeHandlerMap.remove(container);
  }
  else {
    final IGLColumn column = getColumnFromWidget(widget);
    if (column != null) {
      _widgetByColumnMap.remove(column);
    }
  }
}
//--------------------------------------------------------------------------------------------------
@Override
public boolean setConstraintViolations(final Iterable<ConstraintViolation<?>> violations) {
  throw new UnsupportedOperationException();
}
//--------------------------------------------------------------------------------------------------
private void setWidgetValue(final Widget widget, final GLRecord record, final IGLColumn column) {
  // TODO Auto-generated method stub

}
//--------------------------------------------------------------------------------------------------
}