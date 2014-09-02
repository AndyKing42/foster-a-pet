package org.greatlogic.glgwt.client.widget.grid;

import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.core.IGLClientEnums.EGLGridContentPanelButtonType;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.db.IGLCreateNewRecordCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;

public class GLGridButtonContainer extends Composite {
//--------------------------------------------------------------------------------------------------
@UiField
HorizontalLayoutContainer  buttonContainer;

private final GLGridWidget _gridWidget;
//--------------------------------------------------------------------------------------------------
interface IGLGridButtonContainerUiBinder extends UiBinder<Widget, GLGridButtonContainer> { //
}
//--------------------------------------------------------------------------------------------------
public GLGridButtonContainer(final GLGridWidget gridWidget) {
  _gridWidget = gridWidget;
  final IGLGridButtonContainerUiBinder uiBinder = GWT.create(IGLGridButtonContainerUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
}
//--------------------------------------------------------------------------------------------------
public void addButton(final String buttonLabel,
                      final EGLGridContentPanelButtonType contentPanelButtonType) {
  SelectHandler selectHandler;
  switch (contentPanelButtonType) {
    case Delete:
      selectHandler = createDeleteButtonHandler();
      break;
    case New:
      selectHandler = createNewButtonHandler();
      break;
    case Save:
      selectHandler = createSaveButtonHandler();
      break;
    case Undo:
      selectHandler = createUndoButtonHandler();
      break;
    default:
      selectHandler = null;
      break;
  }
  if (selectHandler == null) {
    GLLog.popup(30, "There is no standard button for the button type:" + contentPanelButtonType);
    return;
  }
  addButton(buttonLabel, selectHandler);
}
//--------------------------------------------------------------------------------------------------
protected final void addButton(final String buttonLabel, final SelectHandler selectHandler) {
  if (selectHandler == null) {
    GLLog.popup(30, "Missing select handler for content panel button:" + buttonLabel);
    return;
  }
  buttonContainer.add(new TextButton(buttonLabel, selectHandler));
}
//--------------------------------------------------------------------------------------------------
private SelectHandler createDeleteButtonHandler() {
  return new SelectHandler() {
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
  };
}
//--------------------------------------------------------------------------------------------------
private SelectHandler createNewButtonHandler() {
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
  return new SelectHandler() {
    @Override
    public void onSelect(final SelectEvent event) {
      GLClientUtil.createNewRecord(_gridWidget.getListStore().getRecordDef(), newRecordCallback);
    }
  };
}
//--------------------------------------------------------------------------------------------------
private SelectHandler createSaveButtonHandler() {
  return new SelectHandler() {
    @Override
    public void onSelect(final SelectEvent event) {
      GLClientUtil.getDBUpdater().saveAllChanges();
    }
  };
}
//--------------------------------------------------------------------------------------------------
private SelectHandler createUndoButtonHandler() {
  return new SelectHandler() {
    @Override
    public void onSelect(final SelectEvent event) {
      _gridWidget.getListStore().rejectChanges();
    }
  };
}
//--------------------------------------------------------------------------------------------------
}