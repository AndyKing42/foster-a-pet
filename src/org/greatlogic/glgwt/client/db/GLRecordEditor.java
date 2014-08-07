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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.event.GLRecordChangeEvent;
import org.greatlogic.glgwt.client.event.GLRecordChangeEvent.IGLRecordChangeEventHandler;
import org.greatlogic.glgwt.client.widget.GLFieldUtils;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLLookupType;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.container.InsertResizeContainer;
import com.sencha.gxt.widget.core.client.event.AddEvent;
import com.sencha.gxt.widget.core.client.event.AddEvent.AddHandler;
import com.sencha.gxt.widget.core.client.event.RemoveEvent;
import com.sencha.gxt.widget.core.client.event.RemoveEvent.RemoveHandler;
import com.sencha.gxt.widget.core.client.form.BigDecimalField;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
/**
 * Manages editing for one record.
 */
public class GLRecordEditor implements IGLRecordChangeEventHandler {
//--------------------------------------------------------------------------------------------------
private final HashMap<Container, HandlerRegistration> _containerAddHandlerMap;
private final HashMap<IGLColumn, HasValue<?>>         _hasValueByColumnMap;
private final boolean                                 _insertWidgetsBelowLabels;
private TreeSet<IGLColumn>                            _modifiedColumnSet;
private GLRecord                                      _modifiedRecord;
private final GLRecord                                _originalRecord;
private final HashMap<Container, HandlerRegistration> _removeHandlerMap;
private final IGLTable                                _table;
//--------------------------------------------------------------------------------------------------
public GLRecordEditor(final GLRecord record, final boolean insertWidgetsBelowLabels,
                      final Widget... widgets) {
  _originalRecord = record;
  _insertWidgetsBelowLabels = insertWidgetsBelowLabels;
  _containerAddHandlerMap = new HashMap<>();
  _hasValueByColumnMap = new HashMap<>();
  _removeHandlerMap = new HashMap<>();
  _table = record.getRecordDef().getTable();
  for (final Widget widget : widgets) {
    addWidget(widget);
  }
  for (final IGLColumn column : _table.getColumns()) {
    final HasValue<?> hasValue = _hasValueByColumnMap.get(column);
    if (hasValue != null) {
      setWidgetValue(hasValue, record, column);
    }
  }
  GLClientUtil.getEventBus().addHandler(GLRecordChangeEvent.RecordChangeEventType, this);
  GLClientUtil.getDBUpdater().registerRecordEditor(this);
}
//--------------------------------------------------------------------------------------------------
private void addWidget(final Widget widget) {
  if (widget instanceof Container && !(widget instanceof FieldLabel)) {
    addWidgetContainer((Container)widget);
  }
  else {
    addWidgetNonContainer(widget);
  }
}
//--------------------------------------------------------------------------------------------------
private void addWidgetContainer(final Container container) {
  createContainerHandlers(container);
  // copy the existing children because additional children may be added in the call to addWidget()
  final ArrayList<Widget> childWidgetList = new ArrayList<>();
  for (final Widget childWidget : container) {
    childWidgetList.add(childWidget);
  }
  for (final Widget childWidget : childWidgetList) {
    addWidget(childWidget);
  }
}
//--------------------------------------------------------------------------------------------------
private void addWidgetNonContainer(final Widget widget) {
  final IGLColumn column = getColumnFromWidget(widget);
  if (column != null) {
    HasValue<?> hasValue;
    if (widget instanceof FieldLabel) {
      final FieldLabel fieldLabel = (FieldLabel)widget;
      if (fieldLabel.getText().isEmpty()) {
        fieldLabel.setText(column.getTitle());
      }
      hasValue = GLFieldUtils.createField(column);
      final Container parent = (Container)fieldLabel.getParent();
      if (_insertWidgetsBelowLabels && parent instanceof InsertResizeContainer) {
        final int childIndex = parent.getWidgetIndex(fieldLabel);
        ((InsertResizeContainer)parent).insert((Widget)hasValue, childIndex + 1);
      }
      else {
        fieldLabel.add((Widget)hasValue);
      }
    }
    else {
      hasValue = (HasValue<?>)widget;
    }
    _hasValueByColumnMap.put(column, hasValue);
    hasValue.addValueChangeHandler(createFieldValueChangeHandler(column));
  }
}
//--------------------------------------------------------------------------------------------------
public void commitChangesAfterDBUpdate() {
  if (_modifiedRecord != null) {
    _modifiedRecord = null;
    _modifiedColumnSet = null;
  }
}
//--------------------------------------------------------------------------------------------------
private void createContainerHandlers(final Container container) {
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
//--------------------------------------------------------------------------------------------------
private ValueChangeHandler createFieldValueChangeHandler(final IGLColumn column) {
  return new ValueChangeHandler() {
    @Override
    public void onValueChange(final ValueChangeEvent event) {
      GLLog.popup(10, String.valueOf(event.getValue()));
      if (_modifiedRecord == null) {
        _modifiedRecord = new GLRecord(_originalRecord);
        _modifiedColumnSet = new TreeSet<>();
      }
      // todo: if the value is changed back to the original value then remove the column from the
      // todo: modified column set, and possibly set _modifiedRecord to null
      Object value = event.getValue();
      final IGLLookupType lookupType = column.getLookupType();
      if (lookupType != null && lookupType.getTable() != null) {
        value = ((GLRecord)value).asObject(lookupType.getTable().getPrimaryKeyColumn());
      }
      _modifiedRecord.set(column, value);
      _modifiedColumnSet.add(column);
    }
  };
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
  if (!tableName.equalsIgnoreCase(_table.toString())) {
    return null;
  }
  final String columnName = itemId.substring(dotIndex + 1);
  return _table.findColumnUsingColumnName(columnName);
}
//--------------------------------------------------------------------------------------------------
TreeSet<IGLColumn> getModifiedColumnSet() {
  return _modifiedColumnSet;
}
//--------------------------------------------------------------------------------------------------
GLRecord getModifiedRecord() {
  return _modifiedRecord;
}
//--------------------------------------------------------------------------------------------------
GLRecord getOriginalRecord() {
  return _originalRecord;
}
//--------------------------------------------------------------------------------------------------
@Override
public void onRecordChangeEvent(final GLRecordChangeEvent recordChangeEvent) {
  final GLRecord changedRecord = recordChangeEvent.getRecord();
  if (_table != changedRecord.getRecordDef().getTable() ||
      !_originalRecord.getKeyValueAsString().equals(recordChangeEvent.getRecord()
                                                                     .getKeyValueAsString())) {
    return;
  }
  setValue(recordChangeEvent.getColumnName(), recordChangeEvent.getNewValue());
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
private void setValue(final String columnName, final Object value) {
  final IGLColumn column = _table.findColumnUsingColumnName(columnName);
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
@SuppressWarnings("unchecked")
private void setWidgetValue(final HasValue<?> hasValue, final IGLColumn column, final Object value) {
  final String stringValue = value == null ? "" : value.toString();
  switch (column.getDataType()) {
    case Boolean:
      ((CheckBox)hasValue).setValue(GLClientUtil.stringToBoolean(stringValue));
      break;
    case Currency:
      ((BigDecimalField)hasValue).setValue(GLClientUtil.stringToDec(stringValue));
      break;
    case Date:
      ((DateField)hasValue).setValue(GLClientUtil.stringToDate(stringValue));
      break;
    case DateTime:
      ((DateField)hasValue).setValue(GLClientUtil.stringToDate(stringValue));
      break;
    case Decimal:
      ((BigDecimalField)hasValue).setValue(GLClientUtil.stringToDec(stringValue));
      break;
    case Int:
      if (column.getLookupType().getTable() == null) {
        ((HasValue<Integer>)hasValue).setValue(GLClientUtil.stringToInt(stringValue));
      }
      else if (hasValue instanceof ComboBox) {
        final int key = GLClientUtil.stringToInt(stringValue);
        final GLRecord record = GLClientUtil.getLookupCache() //
                                            .lookupRecordUsingKeyValue(EFAPTable.PetType, key);
        ((ComboBox<GLRecord>)hasValue).setValue(record);
      }
      break;
    case String:
      ((HasValue<String>)hasValue).setValue(stringValue);
      break;
  }
}
//--------------------------------------------------------------------------------------------------
}