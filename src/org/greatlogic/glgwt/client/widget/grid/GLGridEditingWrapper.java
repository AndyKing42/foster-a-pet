package org.greatlogic.glgwt.client.widget.grid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLListStore;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.core.GLRecord;
import org.greatlogic.glgwt.client.widget.GLValidationRecord;
import org.greatlogic.glgwt.shared.GLRecordValidator;
import org.greatlogic.glgwt.shared.GLValidationError;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.cell.core.client.form.NumberInputCell;
import com.sencha.gxt.core.client.util.TextMetrics;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent.BeforeSelectHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.BigDecimalField;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.BigDecimalPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;

class GLGridEditingWrapper {
//--------------------------------------------------------------------------------------------------
private static final String                        Zeroes;

private static TextMetrics                         _textMetrics;

private final HashMap<Widget, HandlerRegistration> _comboboxExpandHandlerMap;
private GridEditing<GLRecord>                      _gridEditing;
private final GLGridWidget                         _gridWidget;
private final GLRecordValidator                    _recordValidator;
//--------------------------------------------------------------------------------------------------
static {
  Zeroes = "0000000000000000000000000000000000000000";
}
//--------------------------------------------------------------------------------------------------
GLGridEditingWrapper(final GLGridWidget gridWidget, final boolean inlineEditing,
                     final GLRecordValidator recordValidator) {
  _gridWidget = gridWidget;
  _recordValidator = recordValidator;
  _comboboxExpandHandlerMap = new HashMap<>();
  createGridEditing(inlineEditing);
  createFields(inlineEditing);
}
//--------------------------------------------------------------------------------------------------
private void addButtonBarDeleteButton(final GridRowEditing<GLRecord> gridRowEditing) {
  final TextButton deleteButton = new TextButton("Delete");
  deleteButton.addSelectHandler(new SelectEvent.SelectHandler() {
    @Override
    public void onSelect(final SelectEvent event) {
      _gridWidget.requestRowDeleteConfirmation(gridRowEditing);
    }
  });
  gridRowEditing.getButtonBar().add(deleteButton);
}
//--------------------------------------------------------------------------------------------------
private void addComboboxExpandHandler(final ComboBox<?> combobox) {
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
@SuppressWarnings("unchecked")
private Field<Boolean> createBooleanEditor(final GLColumnConfig<?> columnConfig) {
  final Field<Boolean> result = new CheckBox();
  if (columnConfig.getValidator() != null) {
    result.addValidator((Validator<Boolean>)columnConfig.getValidator());
  }
  _gridEditing.addEditor((ColumnConfig<GLRecord, Boolean>)columnConfig, result);
  return result;
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
private Field<Date> createDateEditor(final GLColumnConfig<?> columnConfig) {
  DateField result;
  final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("MM/dd/yy");
  final DateTimePropertyEditor propertyEditor = new DateTimePropertyEditor(dateTimeFormat);
  result = new DateField(propertyEditor);
  if (columnConfig.getValidator() != null) {
    result.addValidator((Validator<Date>)columnConfig.getValidator());
  }
  _gridEditing.addEditor((ColumnConfig<GLRecord, Date>)columnConfig, result);
  return result;
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
private DateField createDateTimeEditor(final GLColumnConfig<?> columnConfig) {
  /*
   * In 3, I'd probably start by making an Editor instance with two sub-editors, one DateField and
   * one TimeField, each using @Path("") to have them bind to the same value.
   * 
   * Or make the new class implement IsField, and use setValue() and getValue() to modify/read both
   * sub-editors.
   * 
   * IsField is what is being used in 3 to replace most MultiField cases - it allows a widget to
   * supply methods that are helpful for most fields, and as it extends LeafValueEditor, it can be
   * used in GWT Editor framework, and subfields will be ignored, leaving the dev to write their own
   * logic for binding the values.
   */
  DateField result;
  final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("MM/dd/yy hh:mma");
  final DateTimePropertyEditor propertyEditor = new DateTimePropertyEditor(dateTimeFormat);
  result = new DateField(propertyEditor);
  if (columnConfig.getValidator() != null) {
    result.addValidator((Validator<Date>)columnConfig.getValidator());
  }
  _gridEditing.addEditor((ColumnConfig<GLRecord, Date>)columnConfig, result);
  return result;
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
private BigDecimalField createDecimalEditor(final GLColumnConfig<?> columnConfig,
                                            final int numberOfDecimalPlaces) {
  final BigDecimalField result;
  final NumberFormat format = NumberFormat.getFormat("#0." + //
                                                     Zeroes.substring(0, numberOfDecimalPlaces));
  final BigDecimalPropertyEditor propertyEditor;
  propertyEditor = new BigDecimalPropertyEditor(format);
  final NumberInputCell<BigDecimal> cell = new NumberInputCell<>(propertyEditor);
  result = new BigDecimalField(cell);
  result.setFormat(format);
  if (columnConfig.getValidator() != null) {
    result.addValidator((Validator<BigDecimal>)columnConfig.getValidator());
  }
  _gridEditing.addEditor((ColumnConfig<GLRecord, BigDecimal>)columnConfig, result);
  return result;
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
private void createFields(final boolean inlineEditing) {
  for (final GLColumnConfig<?> columnConfig : _gridWidget.getColumnModel().getColumnConfigs()) {
    final IGLColumn column = columnConfig.getColumn();
    if (column == null) { // if column == null then this is the "select" checkbox column
      final Field<Boolean> checkBox = new CheckBox();
      checkBox.setEnabled(false);
      _gridEditing.addEditor((ColumnConfig<GLRecord, Boolean>)columnConfig, checkBox);
    }
    else if (column.getLookupType() != null && column.getLookupType().getTable() == null) {
      createFixedComboboxEditor(columnConfig);
    }
    else {
      Field<?> field = null;
      switch (column.getDataType()) {
        case Boolean:
          if (!inlineEditing) {
            field = createBooleanEditor(columnConfig);
          }
          break;
        case Currency:
          field = createDecimalEditor(columnConfig, 2);
          break;
        case Date: {
          field = createDateEditor(columnConfig);
          break;
        }
        case DateTime: {
          field = createDateTimeEditor(columnConfig);
          break;
        }
        case Decimal:
          field = createDecimalEditor(columnConfig, column.getDecimalPlacesOrLength());
          break;
        case Int:
          if (column.getLookupType() == null || column.getLookupType().getTable() == null) {
            field = createIntegerEditor(columnConfig);
          }
          else {
            field = createForeignKeyComboboxEditor(columnConfig);
          }
          break;
        case String:
          field = createStringEditor(columnConfig);
          break;
      }
      if (field != null) {
        columnConfig.setField(field);
        if (field instanceof ValueBaseField) {
          final ValueBaseField<GLRecord> valueBaseField = (ValueBaseField<GLRecord>)field;
          valueBaseField.setAllowBlank(column.getNullable());
          valueBaseField.setClearValueOnParseError(false);
        }
      }
    }
  }
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
private void createFixedComboboxEditor(final GLColumnConfig<?> columnConfig) {
  final SimpleComboBox<String> combobox = new SimpleComboBox<>(new StringLabelProvider<>());
  combobox.setClearValueOnParseError(false);
  combobox.setTriggerAction(TriggerAction.ALL);
  combobox.add(GLClientUtil.getLookupCache()
                           .getAbbrevList(columnConfig.getColumn().getLookupType()));
  combobox.setForceSelection(true);
  if (columnConfig.getValidator() != null) {
    combobox.addValidator((Validator<String>)columnConfig.getValidator());
  }
  addComboboxExpandHandler(combobox);
  _gridEditing.addEditor((ColumnConfig<GLRecord, String>)columnConfig, combobox);
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
private ComboBox<GLRecord> createForeignKeyComboboxEditor(final GLColumnConfig<?> columnConfig) {
  final ComboBox<GLRecord> result;
  final IGLColumn column = columnConfig.getColumn();
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
  if (columnConfig.getValidator() != null) {
    result.addValidator((Validator<GLRecord>)columnConfig.getValidator());
  }
  final Converter<String, GLRecord> converter = new Converter<String, GLRecord>() {
    @Override
    public String convertFieldValue(final GLRecord record) {
      return record == null ? "" : record.asString(parentTable.getComboboxColumnMap().get(1));
    }
    @Override
    public GLRecord convertModelValue(final String displayValue) {
      return GLClientUtil.getLookupCache().lookupRecord(parentTable, displayValue);
    }
  };
  _gridEditing.addEditor((ColumnConfig<GLRecord, String>)columnConfig, converter, result);
  addComboboxExpandHandler(result);
  return result;
}
//--------------------------------------------------------------------------------------------------
private void createGridEditing(final boolean inlineEditing) {
  if (inlineEditing) {
    _gridEditing = new GridInlineEditing<>(_gridWidget.getGrid());
  }
  else {
    final GridRowEditing<GLRecord> gridRowEditing = new GridRowEditing<>(_gridWidget.getGrid());
    _gridEditing = gridRowEditing;
    createGridRowEditingSaveButtonHandler(gridRowEditing);
    addButtonBarDeleteButton(gridRowEditing);
  }
}
//--------------------------------------------------------------------------------------------------
private void createGridRowEditingSaveButtonHandler(final GridRowEditing<GLRecord> gridRowEditing) {
  gridRowEditing.getSaveButton().addBeforeSelectHandler(new BeforeSelectHandler() {
    @Override
    public void onBeforeSelect(final BeforeSelectEvent event) {
      _gridWidget.getColumnModel().clearInvalidColumnConfigs();
      if (_recordValidator != null) {
        final TreeMap<String, GLColumnConfig<?>> columnConfigMap;
        columnConfigMap = _gridWidget.getColumnModel().getColumnConfigMap();
        final GLValidationRecord validationRecord = new GLValidationRecord(columnConfigMap, //
                                                                           gridRowEditing);
        final ArrayList<GLValidationError> errorList = _recordValidator.validate(validationRecord);
        if (errorList != null && errorList.size() > 0) {
          for (final GLValidationError validationError : errorList) {
            final IGLColumn column = validationError.getColumn();
            validationRecord.setInvalid(column, validationError.getMessage());
          }
          event.setCancelled(true);
          return;
        }
      }
      if (_gridWidget.getRowLevelCommits()) {
        gridRowEditing.completeEditing();
        _gridWidget.getListStore().commitChanges();
      }
    }
  });
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
private Field<?> createIntegerEditor(final GLColumnConfig<?> columnConfig) {
  final IntegerField result = new IntegerField();
  if (columnConfig.getValidator() != null) {
    result.addValidator((Validator<Integer>)columnConfig.getValidator());
  }
  _gridEditing.addEditor((ColumnConfig<GLRecord, Integer>)columnConfig, result);
  return result;
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
private Field<String> createStringEditor(final GLColumnConfig<?> columnConfig) {
  final Field<String> result = new TextField();
  if (columnConfig.getValidator() != null) {
    result.addValidator((Validator<String>)columnConfig.getValidator());
  }
  _gridEditing.addEditor((ColumnConfig<GLRecord, String>)columnConfig, result);
  return result;
}
//--------------------------------------------------------------------------------------------------
GridEditing<GLRecord> getGridEditing() {
  return _gridEditing;
}
//--------------------------------------------------------------------------------------------------
}