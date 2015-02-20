package org.greatlogic.glgwt.client.widget.grid;
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
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.core.IGLClientEnums.EGLGridContentPanelButtonType;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.db.IGLCreateNewRecordCallback;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;

public class Old_GLGridContainer extends VerticalLayoutContainer {
//--------------------------------------------------------------------------------------------------
private final Old_GLGridWidget _gridWidget;
//--------------------------------------------------------------------------------------------------
Old_GLGridContainer(final Old_GLGridWidget gridWidget) {
  super();
  _gridWidget = gridWidget;
}
//--------------------------------------------------------------------------------------------------
public void addContentPanelButton(final String buttonLabel,
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
  addButtonToPanel(new TextButton(buttonLabel, selectHandler));
}
//--------------------------------------------------------------------------------------------------
private void addButtonToPanel(final TextButton textButton) {

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
      GLClientUtil.createNewRecord(_gridWidget.getListStore().getRecordDef(), null,
                                   newRecordCallback);
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