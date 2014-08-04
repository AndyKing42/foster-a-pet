package org.greatlogic.glgwt.client.ui;
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
import java.util.Date;
import java.util.HashMap;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.db.GLListStore;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.widget.grid.GLColumnConfig;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLEnums.EGLColumnDataType;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.TextMetrics;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.form.BigDecimalField;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.BigDecimalPropertyEditor;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;

public class GLFieldUtils {
//--------------------------------------------------------------------------------------------------
private static final DateTimePropertyEditor               DatePropertyEditor;
private static final DateTimePropertyEditor               DateTimePropertyEditor;
private static final String                               Zeroes;
private static final HashMap<Widget, HandlerRegistration> _comboboxExpandHandlerMap;
private static TextMetrics                                _textMetrics;

//--------------------------------------------------------------------------------------------------
static {
  DatePropertyEditor = new DateTimePropertyEditor("MM/dd/yyyy");
  DateTimePropertyEditor = new DateTimePropertyEditor("MM/dd/yyyy hh:mm:ss");
  Zeroes = "0000000000000000000000000000000000000000";
  _comboboxExpandHandlerMap = new HashMap<>();
}
//--------------------------------------------------------------------------------------------------
public static void addComboboxExpandHandler(final ComboBox<?> combobox) {
  final HandlerRegistration expandHandler = combobox.addExpandHandler(new ExpandHandler() {
    @Override
    public void onExpand(final ExpandEvent event) {
      int maxWidth = 0;
      for (final Element element : combobox.getListView().getElements()) {
        if (_textMetrics == null) {
          _textMetrics = TextMetrics.get();
          _textMetrics.bind(element.getClassName());
        }
        final int width = _textMetrics.getWidth(element.getInnerText());
        if (width > maxWidth) {
          maxWidth = width;
        }
      }
      combobox.setMinListWidth(maxWidth + 10);
      final HandlerRegistration handler = _comboboxExpandHandlerMap.get(combobox);
      if (handler != null) {
        handler.removeHandler();
      }
    }
  });
  _comboboxExpandHandlerMap.put(combobox, expandHandler);
}
//--------------------------------------------------------------------------------------------------
public static Converter<String, GLRecord> createConverter(final IGLTable parentTable) {
  final Converter<String, GLRecord> result = new Converter<String, GLRecord>() {
    @Override
    public String convertFieldValue(final GLRecord record) {
      return record == null ? "" : record.asString(parentTable.getComboboxColumnMap().get(1));
    }
    @Override
    public GLRecord convertModelValue(final String displayValue) {
      return GLClientUtil.getLookupCache().lookupRecordUsingDisplayValue(parentTable, displayValue);
    }
  };
  return result;
}
//--------------------------------------------------------------------------------------------------
public static Field<?> createField(final IGLColumn column) {
  return createField(null, null, column, false);
}
//--------------------------------------------------------------------------------------------------
public static Field<?> createField(final GridEditing<GLRecord> gridEditing,
                                   final GLColumnConfig<?> columnConfig, final boolean inlineEditing) {
  return createField(gridEditing, columnConfig, columnConfig.getColumn(), inlineEditing);
}
//--------------------------------------------------------------------------------------------------
private static Field<?> createField(final GridEditing<GLRecord> gridEditing,
                                    final GLColumnConfig<?> columnConfig, final IGLColumn column,
                                    final boolean inlineEditing) {
  Field<?> field;
  if (column.getLookupType() == null) {
    field = createFieldUsingDataType(gridEditing, columnConfig, column, inlineEditing);
  }
  else if (column.getLookupType().getTable() == null) {
    field = GLFieldUtils.createFixedCombobox(column);
    if (gridEditing != null) {
      gridEditing.addEditor((ColumnConfig<GLRecord, String>)columnConfig, (SimpleComboBox)field);
    }
  }
  else {
    field = GLFieldUtils.createForeignKeyCombobox(column);
    final Converter<String, GLRecord> converter;
    converter = GLFieldUtils.createConverter(column.getLookupType().getTable());
    if (gridEditing != null) {
      gridEditing.addEditor((ColumnConfig<GLRecord, String>)columnConfig, converter,
                            (ComboBox)field);
    }
  }
  return field;
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
private static Field<?> createFieldUsingDataType(final GridEditing<GLRecord> gridEditing,
                                                 final GLColumnConfig<?> columnConfig,
                                                 final IGLColumn column, final boolean inlineEditing) {
  Field<?> result = null;
  switch (column.getDataType()) {
    case Boolean:
      if (!inlineEditing) {
        final CheckBox checkBox = new CheckBox();
        GLFieldUtils.initializeCheckBox(checkBox, column);
        if (gridEditing != null) {
          gridEditing.addEditor((ColumnConfig<GLRecord, Boolean>)columnConfig, checkBox);
        }
        result = checkBox;
      }
      break;
    case Currency:
      final BigDecimalField currencyField = new BigDecimalField();
      GLFieldUtils.initializeBigDecimalField(currencyField, column);
      if (gridEditing != null) {
        gridEditing.addEditor((ColumnConfig<GLRecord, BigDecimal>)columnConfig, currencyField);
      }
      result = currencyField;
      break;
    case Date:
      final DateField dateField = new DateField();
      GLFieldUtils.initializeDateField(dateField, column);
      if (gridEditing != null) {
        gridEditing.addEditor((ColumnConfig<GLRecord, Date>)columnConfig, dateField);
      }
      result = dateField;
      break;
    case DateTime:
      final DateField dateTimeField = new DateField();
      GLFieldUtils.initializeDateField(dateTimeField, column);
      if (gridEditing != null) {
        gridEditing.addEditor((ColumnConfig<GLRecord, Date>)columnConfig, dateTimeField);
      }
      result = dateTimeField;
      break;
    case Decimal:
      final BigDecimalField bigDecimalField = new BigDecimalField();
      GLFieldUtils.initializeBigDecimalField(bigDecimalField, column);
      if (gridEditing != null) {
        gridEditing.addEditor((ColumnConfig<GLRecord, BigDecimal>)columnConfig, bigDecimalField);
      }
      result = bigDecimalField;
      break;
    case Int:
      final IntegerField integerField = new IntegerField();
      GLFieldUtils.initializeIntegerField(integerField, column);
      if (gridEditing != null) {
        gridEditing.addEditor((ColumnConfig<GLRecord, Integer>)columnConfig, integerField);
      }
      result = integerField;
      break;
    case String:
      final TextField textField = new TextField();
      GLFieldUtils.initializeTextField(textField, column);
      if (gridEditing != null) {
        gridEditing.addEditor((ColumnConfig<GLRecord, String>)columnConfig, textField);
      }
      result = textField;
      break;
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
public static SimpleComboBox<String> createFixedCombobox(final IGLColumn column) {
  final SimpleComboBox<String> result = new SimpleComboBox<>(new StringLabelProvider<>());
  result.setClearValueOnParseError(false);
  result.setTriggerAction(TriggerAction.ALL);
  result.add(GLClientUtil.getLookupCache().getAbbrevList(column.getLookupType()));
  result.setForceSelection(true);
  final Validator<String> validator;
  validator = (Validator<String>)GLClientUtil.getValidators().getColumnValidator(column);
  if (validator != null) {
    result.addValidator(validator);
  }
  addComboboxExpandHandler(result);
  return result;
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
public static ComboBox<GLRecord> createForeignKeyCombobox(final IGLColumn column) {
  final ComboBox<GLRecord> result;
  final IGLTable parentTable = column.getLookupType().getTable();
  final GLListStore lookupListStore = GLClientUtil.getLookupCache().getListStore(parentTable);
  if (lookupListStore == null) {
    GLLog.popup(10, "Lookup list store not found for column:" + column);
    return null;
  }
  final LabelProvider<GLRecord> labelProvider = new LabelProvider<GLRecord>() {
    @Override
    public String getLabel(final GLRecord record) {
      return record.asString(parentTable.getComboboxColumnMap().get(1));
    }
  };
  result = new ComboBox<>(lookupListStore, labelProvider);
  result.setForceSelection(true);
  result.setTriggerAction(TriggerAction.ALL);
  result.setTypeAhead(true);
  final Validator<GLRecord> validator;
  validator = (Validator<GLRecord>)GLClientUtil.getValidators().getColumnValidator(column);
  if (validator != null) {
    result.addValidator(validator);
  }
  GLFieldUtils.addComboboxExpandHandler(result);
  return result;
}
//--------------------------------------------------------------------------------------------------
public static void initializeBigDecimalField(final BigDecimalField bigDecimalField,
                                             final IGLColumn column) {
  final NumberFormat format;
  if (column.getDataType() == EGLColumnDataType.Currency) {
    format = NumberFormat.getFormat("0.00");
  }
  else {
    format = NumberFormat.getFormat("#0." + //
                                    Zeroes.substring(0, column.getDecimalPlacesOrLength()));
  }
  bigDecimalField.setPropertyEditor(new BigDecimalPropertyEditor(format));
  final Validator<?> validator = GLClientUtil.getValidators().getColumnValidator(column);
  if (validator != null) {
    bigDecimalField.addValidator((Validator<BigDecimal>)validator);
  }
  bigDecimalField.setAllowBlank(column.getNullable());
  bigDecimalField.setClearValueOnParseError(false);
}
//--------------------------------------------------------------------------------------------------
public static void initializeCheckBox(final CheckBox checkBox, final IGLColumn column) {
  final Validator<?> validator = GLClientUtil.getValidators().getColumnValidator(column);
  if (validator != null) {
    checkBox.addValidator((Validator<Boolean>)validator);
  }
}
//--------------------------------------------------------------------------------------------------
public static void initializeDateField(final DateField dateField, final IGLColumn column) {
  final PropertyEditor<Date> propertyEditor;
  propertyEditor = column.getDataType() == EGLColumnDataType.Date ? DatePropertyEditor //
                                                                 : DateTimePropertyEditor;
  dateField.setPropertyEditor(propertyEditor);
  final Validator<?> validator = GLClientUtil.getValidators().getColumnValidator(column);
  if (validator != null) {
    dateField.addValidator((Validator<Date>)validator);
  }
  dateField.setAllowBlank(column.getNullable());
  dateField.setClearValueOnParseError(false);
}
//--------------------------------------------------------------------------------------------------
public static void initializeIntegerField(final IntegerField integerField, final IGLColumn column) {
  final Validator<?> validator = GLClientUtil.getValidators().getColumnValidator(column);
  if (validator != null) {
    integerField.addValidator((Validator<Integer>)validator);
  }
  integerField.setAllowBlank(column.getNullable());
  integerField.setClearValueOnParseError(false);
}
//--------------------------------------------------------------------------------------------------
public static void initializeTextField(final TextField textField, final IGLColumn column) {
  final Validator<?> validator = GLClientUtil.getValidators().getColumnValidator(column);
  if (validator != null) {
    textField.addValidator((Validator<String>)validator);
  }
  textField.setAllowBlank(column.getNullable());
  textField.setClearValueOnParseError(false);
}
//--------------------------------------------------------------------------------------------------
}