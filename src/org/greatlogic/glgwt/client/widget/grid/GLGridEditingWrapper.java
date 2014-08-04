package org.greatlogic.glgwt.client.widget.grid;

import java.util.ArrayList;
import java.util.TreeMap;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.ui.GLFieldUtils;
import org.greatlogic.glgwt.client.widget.GLValidationRecord;
import org.greatlogic.glgwt.shared.GLRecordValidator;
import org.greatlogic.glgwt.shared.GLValidationError;
import org.greatlogic.glgwt.shared.IGLColumn;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent.BeforeSelectHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;

class GLGridEditingWrapper {
//--------------------------------------------------------------------------------------------------
private GridEditing<GLRecord>   _gridEditing;
private final GLGridWidget      _gridWidget;
private final GLRecordValidator _recordValidator;
//--------------------------------------------------------------------------------------------------
GLGridEditingWrapper(final GLGridWidget gridWidget, final boolean inlineEditing,
                     final GLRecordValidator recordValidator) {
  _gridWidget = gridWidget;
  _recordValidator = recordValidator;
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
@SuppressWarnings({"unchecked"})
private void createFields(final boolean inlineEditing) {
  for (final GLColumnConfig<?> columnConfig : _gridWidget.getColumnModel().getColumnConfigs()) {
    final IGLColumn column = columnConfig.getColumn();
    if (column == null) { // if column == null then this is the "select" checkbox column
      final Field<Boolean> checkBox = new CheckBox();
      checkBox.setEnabled(false);
      _gridEditing.addEditor((ColumnConfig<GLRecord, Boolean>)columnConfig, checkBox);
    }
    else {
      final Field<?> field = GLFieldUtils.createField(_gridEditing, columnConfig, inlineEditing);
      if (field != null) {
        columnConfig.setField(field);
      }
    }
  }
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
        GLClientUtil.getDBUpdater().saveAllChanges();
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