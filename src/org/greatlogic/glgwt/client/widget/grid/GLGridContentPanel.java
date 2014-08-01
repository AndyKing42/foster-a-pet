package org.greatlogic.glgwt.client.widget.grid;

import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.db.IGLCreateNewRecordCallback;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;

public class GLGridContentPanel extends ContentPanel {
//--------------------------------------------------------------------------------------------------
private final GLGridWidget _gridWidget;
//--------------------------------------------------------------------------------------------------
GLGridContentPanel(final GLGridWidget gridWidget, final String headingText) {
  super();
  _gridWidget = gridWidget;
  if (GLClientUtil.isBlank(headingText)) {
    setHeaderVisible(false);
  }
  else {
    setHeaderVisible(true);
    setHeadingText(headingText);
  }
  setButtonAlign(BoxLayoutPack.START);
  createButtons();
}
//--------------------------------------------------------------------------------------------------
private void createButtons() {
  addNewButton();
  addUndoChangesButton();
  addSaveButton();
  addButton(addDeleteButton());
}
//--------------------------------------------------------------------------------------------------
private TextButton addDeleteButton() {
  return new TextButton("Delete Selected", new SelectHandler() {
    @Override
    public void onSelect(final SelectEvent selectEvent) {
      if (_gridWidget.getSelectedRecordSet().size() == 0) {
        final AlertMessageBox messageBox;
        messageBox = new AlertMessageBox("Delete Rows", "You haven't selected any rows to delete");
        messageBox.show();
        return;
      }
      final String rowMessage;
      if (_gridWidget.getSelectedRecordSet().size() == 1) {
        rowMessage = "this row";
      }
      else {
        rowMessage = "these " + _gridWidget.getSelectedRecordSet().size() + " rows";
      }
      final ConfirmMessageBox messageBox;
      messageBox = new ConfirmMessageBox("Delete Rows", //
                                         "Are you sure you want to delete " + rowMessage + "?");
      messageBox.addDialogHideHandler(new DialogHideHandler() {
        @Override
        public void onDialogHide(final DialogHideEvent hideEvent) {
          if (hideEvent.getHideButton() == PredefinedButton.YES) {
            for (final GLRecord record : _gridWidget.getSelectedRecordSet()) {
              _gridWidget.getListStore().delete(record);
            }
            _gridWidget.getSelectedRecordSet().clear();
          }
        }
      });
      messageBox.show();
    }
  });
}
//--------------------------------------------------------------------------------------------------
private void addNewButton() {
  final IGLCreateNewRecordCallback newRecordCallback = new IGLCreateNewRecordCallback() {
    @Override
    public void onFailure(final Throwable t) {
      // TODO: handle failure gracefully
    }
    @Override
    public void onSuccess(final GLRecord record) {
      _gridWidget.getGridEditingWrapper().getGridEditing().cancelEditing();
      _gridWidget.getListStore().add(0, record);
      final int row = _gridWidget.getListStore().indexOf(record);
      _gridWidget.getGridEditingWrapper().getGridEditing().startEditing(new GridCell(row, 0));
    }
  };
  final SelectHandler selectHandler = new SelectHandler() {
    @Override
    public void onSelect(final SelectEvent event) {
      GLClientUtil.createNewRecord(_gridWidget.getListStore().getRecordDef(), newRecordCallback);
    }
  };
  addButton(new TextButton("New", selectHandler));
}
//--------------------------------------------------------------------------------------------------
private void addSaveButton() {
  addButton(new TextButton("Save", new SelectHandler() {
    @Override
    public void onSelect(final SelectEvent event) {
      GLClientUtil.getDBUpdater().saveAllChanges();
    }
  }));
}
//--------------------------------------------------------------------------------------------------
private void addUndoChangesButton() {
  addButton(new TextButton("Undo Changes", new SelectHandler() {
    @Override
    public void onSelect(final SelectEvent event) {
      _gridWidget.getListStore().rejectChanges();
    }
  }));
}
//--------------------------------------------------------------------------------------------------
}