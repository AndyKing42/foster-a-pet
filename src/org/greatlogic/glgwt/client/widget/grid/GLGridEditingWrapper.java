package org.greatlogic.glgwt.client.widget.grid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.db.GLListStore;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.ui.GLFieldInitializers;
import org.greatlogic.glgwt.client.widget.GLValidationRecord;
import org.greatlogic.glgwt.shared.GLRecordValidator;
import org.greatlogic.glgwt.shared.GLValidationError;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
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
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
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
  return null;
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
    else {
      Field<?> field = null;
      if (column.getLookupType() != null) {
        if (column.getLookupType().getTable() == null) {
          field = createFixedComboboxEditor(columnConfig);
        }
        else {
          field = createForeignKeyComboboxEditor(columnConfig);
        }
      }
      else {
        field = createFieldUsingDataType(columnConfig, column, inlineEditing);
      }
      if (field != null) {
        columnConfig.setField(field);
      }
    }
  }
}
//--------------------------------------------------------------------------------------------------
private Field<?> createFieldUsingDataType(final GLColumnConfig<?> columnConfig,
                                          final IGLColumn column, final boolean inlineEditing) {
  Field<?> result = null;
  switch (column.getDataType()) {
    case Boolean:
      if (!inlineEditing) {
        final CheckBox checkBox = new CheckBox();
        GLFieldInitializers.initialize(checkBox, columnConfig.getColumn());
        _gridEditing.addEditor((ColumnConfig<GLRecord, Boolean>)columnConfig, checkBox);
        result = checkBox;
      }
      break;
    case Currency:
      final BigDecimalField currencyField = new BigDecimalField();
      GLFieldInitializers.initialize(currencyField, columnConfig.getColumn());
      _gridEditing.addEditor((ColumnConfig<GLRecord, BigDecimal>)columnConfig, currencyField);
      result = currencyField;
      break;
    case Date:
      final DateField dateField = new DateField();
      GLFieldInitializers.initialize(dateField, columnConfig.getColumn());
      _gridEditing.addEditor((ColumnConfig<GLRecord, Date>)columnConfig, dateField);
      result = dateField;
      break;
    case DateTime:
      final DateField dateTimeField = new DateField();
      GLFieldInitializers.initialize(dateTimeField, columnConfig.getColumn());
      _gridEditing.addEditor((ColumnConfig<GLRecord, Date>)columnConfig, dateTimeField);
      result = dateTimeField;
      break;
    case Decimal:
      final BigDecimalField bigDecimalField = new BigDecimalField();
      GLFieldInitializers.initialize(bigDecimalField, columnConfig.getColumn());
      _gridEditing.addEditor((ColumnConfig<GLRecord, BigDecimal>)columnConfig, bigDecimalField);
      result = bigDecimalField;
      break;
    case Int:
      final IntegerField integerField = new IntegerField();
      GLFieldInitializers.initialize(integerField, columnConfig.getColumn());
      _gridEditing.addEditor((ColumnConfig<GLRecord, Integer>)columnConfig, integerField);
      result = integerField;
      break;
    case String:
      final TextField textField = new TextField();
      GLFieldInitializers.initialize(textField, columnConfig.getColumn());
      _gridEditing.addEditor((ColumnConfig<GLRecord, String>)columnConfig, textField);
      result = textField;
      break;
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
private SimpleComboBox<String> createFixedComboboxEditor(final GLColumnConfig<?> columnConfig) {
  final SimpleComboBox<String> result = new SimpleComboBox<>(new StringLabelProvider<>());
  result.setClearValueOnParseError(false);
  result.setTriggerAction(TriggerAction.ALL);
  result.add(GLClientUtil.getLookupCache().getAbbrevList(columnConfig.getColumn().getLookupType()));
  result.setForceSelection(true);
  if (columnConfig.getValidator() != null) {
    result.addValidator((Validator<String>)columnConfig.getValidator());
  }
  addComboboxExpandHandler(result);
  _gridEditing.addEditor((ColumnConfig<GLRecord, String>)columnConfig, result);
  return result;
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
GridEditing<GLRecord> getGridEditing() {
  return _gridEditing;
}
//--------------------------------------------------------------------------------------------------
}