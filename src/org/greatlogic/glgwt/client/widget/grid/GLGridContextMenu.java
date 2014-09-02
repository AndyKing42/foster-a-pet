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
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.core.IGLClientEnums.EGLContextMenuItemType;
import org.greatlogic.glgwt.client.db.GLRecord;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class GLGridContextMenu extends Menu {
//--------------------------------------------------------------------------------------------------
private final GLGridWidget _gridWidget;
//--------------------------------------------------------------------------------------------------
GLGridContextMenu(final GLGridWidget gridWidget) {
  super();
  _gridWidget = gridWidget;
}
//--------------------------------------------------------------------------------------------------
final void addContextMenuItem(final String menuLabel,
                              final EGLContextMenuItemType contextMenuItemType) {
  IGLGridContextMenuSelectionHandler selectionHandler;
  switch (contextMenuItemType) {
    case ClearAll:
      selectionHandler = new IGLGridContextMenuSelectionHandler() {
        @Override
        public void onContextMenuSelectionEvent(final GLGridContextMenuSelectionEvent event) {
          _gridWidget.clearAllRowSelectCheckboxes();
        }
      };
      break;
    case Delete:
      selectionHandler = new IGLGridContextMenuSelectionHandler() {
        @Override
        public void onContextMenuSelectionEvent(final GLGridContextMenuSelectionEvent event) {
          _gridWidget.requestRowDeleteConfirmation(null);
        }
      };
      break;
    case SelectAll:
      selectionHandler = new IGLGridContextMenuSelectionHandler() {
        @Override
        public void onContextMenuSelectionEvent(final GLGridContextMenuSelectionEvent event) {
          _gridWidget.selectAllRowSelectCheckboxes();
        }
      };
      break;
    default:
      selectionHandler = null;
      break;
  }
  if (selectionHandler == null) {
    GLLog.popup(30, "There is no default context menu for type:" + contextMenuItemType);
    return;
  }
  addContextMenuItem(menuLabel, selectionHandler);
}
//--------------------------------------------------------------------------------------------------
final void addContextMenuItem(final String menuLabel,
                              final IGLGridContextMenuSelectionHandler selectionHandler) {
  final MenuItem menuItem = new MenuItem(menuLabel, new SelectionHandler<MenuItem>() {
    @Override
    public void onSelection(final SelectionEvent<MenuItem> event) {
      final GLRecord selectedRecord = _gridWidget.getSelectionModel().getSelectedItem();
      final GLGridContextMenuSelectionEvent selectionEvent;
      selectionEvent = new GLGridContextMenuSelectionEvent(event.getSelectedItem(), selectedRecord);
      selectionHandler.onContextMenuSelectionEvent(selectionEvent);
    }
  });
  add(menuItem);
}
//--------------------------------------------------------------------------------------------------
GLGridContextMenu build() {
  _gridWidget.addContextMenuItems();
  return getChildren().size() > 0 ? this : null;
}
//--------------------------------------------------------------------------------------------------
}