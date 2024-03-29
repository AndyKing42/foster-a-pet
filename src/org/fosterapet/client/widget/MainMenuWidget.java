package org.fosterapet.client.widget;
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
import org.fosterapet.client.FAPUtil;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.db.GLDBException;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.TextMetrics;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.menu.CheckMenuItem;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class MainMenuWidget extends Composite {
//--------------------------------------------------------------------------------------------------
@UiField
CheckMenuItem checkBoxSelectionModelCheckMenuItem;
@UiField
Menu          genericGridMenu;
@UiField
CheckMenuItem inlineEditingCheckMenuItem;
@UiField
TextButton    petsButton;
@UiField
CheckMenuItem rowLevelCommitsCheckMenuItem;
//==================================================================================================
interface MainMenuWidgetUiBinder extends UiBinder<Widget, MainMenuWidget> { //
}
//==================================================================================================
public MainMenuWidget() {
  super();
  final MainMenuWidgetUiBinder uiBinder = GWT.create(MainMenuWidgetUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
  addPetsContextMenuHandler();
  addTableNamesToGenericGridMenu();
}
//--------------------------------------------------------------------------------------------------
private void addPetsContextMenuHandler() {
  petsButton.sinkEvents(Event.ONCONTEXTMENU);
  petsButton.addHandler(new ContextMenuHandler() {
    @Override
    public void onContextMenu(final ContextMenuEvent event) {
      try {
        createPetGrid();
      }
      catch (final GLDBException e) {
        // TODO Auto-generated catch block
      }
      event.preventDefault();
    }
  }, ContextMenuEvent.getType());
}
//--------------------------------------------------------------------------------------------------
private void addTableNamesToGenericGridMenu() {
  TextMetrics textMetrics = null;
  int maxWidth = 0;
  for (final IGLTable table : EFAPTable.values()) {
    final MenuItem menuItem = new MenuItem(table.toString());
    if (textMetrics == null) {
      textMetrics = TextMetrics.get();
      textMetrics.bind(menuItem.getElement().getClassName());
    }
    final int width = textMetrics.getWidth(table.toString());
    if (width > maxWidth) {
      maxWidth = width;
    }
    genericGridMenu.add(menuItem);
  }
  genericGridMenu.setWidth(maxWidth + 35);
}
//--------------------------------------------------------------------------------------------------
protected void createPersonGrid() throws GLDBException {
  FAPUtil.getClientFactory()
         .getAppTabPanelWidget()
         .createPersonGrid(inlineEditingCheckMenuItem.isChecked(),
                           checkBoxSelectionModelCheckMenuItem.isChecked(),
                           rowLevelCommitsCheckMenuItem.isChecked());
}
//--------------------------------------------------------------------------------------------------
protected void createPetGrid() throws GLDBException {
  FAPUtil.getClientFactory()
         .getAppTabPanelWidget()
         .createPetGrid(inlineEditingCheckMenuItem.isChecked(),
                        checkBoxSelectionModelCheckMenuItem.isChecked(),
                        rowLevelCommitsCheckMenuItem.isChecked());
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"genericGridMenu"})
public void onGenericTableGridMenuSelection(final SelectionEvent<Item> event) {
  final String tableName = ((MenuItem)event.getSelectedItem()).getText();
  if (tableName == null) {
    GLLog.popup(10, "You must select a table");
    return;
  }
  final IGLTable table = EFAPTable.valueOf(tableName);
  try {
    FAPUtil.getClientFactory().getAppTabPanelWidget().createGenericTableGrid(table);
  }
  catch (final GLDBException dbe) {
    // TODO Auto-generated catch block
  }
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"logOutButton"})
public void onLogOutButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  GLClientUtil.logOut();
  GLClientUtil.logIn();
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"peopleButton"})
public void onPeopleButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  try {
    createPersonGrid();
  }
  catch (final GLDBException e) {
    // TODO Auto-generated catch block
  }
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"petsButton"})
public void onPetsButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  try {
    createPetGrid();
  }
  catch (final GLDBException e) {
    // TODO Auto-generated catch block
  }
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"reloadCacheButton"})
public void onReloadCacheButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  GLClientUtil.getLookupCache().reloadAll();
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"reloadTestDataButton"})
public void onReloadTestDataButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  final ConfirmMessageBox messageBox;
  messageBox = new ConfirmMessageBox("Reload Test Data", //
                                     "Are you sure you want to erase and reload all test data?");
  messageBox.addDialogHideHandler(new DialogHideHandler() {
    @Override
    public void onDialogHide(final DialogHideEvent hideEvent) {
      if (hideEvent.getHideButton() == PredefinedButton.YES) {
        FAPUtil.reloadTestData();
      }
    }
  });
  messageBox.show();
}
//--------------------------------------------------------------------------------------------------
}