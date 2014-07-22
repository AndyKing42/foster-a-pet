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
import java.util.ArrayList;
import org.fosterapet.client.DBAccess;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.widget.GLGenericGridWidget;
import org.greatlogic.glgwt.client.widget.GLGridContentPanel;
import org.greatlogic.glgwt.client.widget.GLGridWidget;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

public class MainLayoutWidget extends Composite {
//--------------------------------------------------------------------------------------------------
@UiField
PlainTabPanel                         appTabPanel;
@UiField
CheckBox                              checkBoxSelectionModelCheckBox;
@UiField
CheckBox                              inlineEditingCheckBox;
@UiField
CheckBox                              rowLevelCommitsCheckBox;
@UiField
SimpleContainer                       tableNameComboBoxContainer;

private final ArrayList<ContentPanel> _appContentPanelList;
private SimpleComboBox<String>        _tableNameCombobox;
//==================================================================================================
interface MainLayoutWidgetUiBinder extends UiBinder<Widget, MainLayoutWidget> { //
}
//==================================================================================================
public MainLayoutWidget() {
  super();
  _appContentPanelList = new ArrayList<>();
  final MainLayoutWidgetUiBinder uiBinder = GWT.create(MainLayoutWidgetUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
  createTableNameCombobox();
}
//--------------------------------------------------------------------------------------------------
public void createPetGrid(final int tabIndex) {
  final ContentPanel contentPanel;
  if (tabIndex < 0) {
    contentPanel = new ContentPanel();
    contentPanel.setHeaderVisible(false);
    _appContentPanelList.add(contentPanel);
    appTabPanel.add(contentPanel, new TabItemConfig("Pets", true));
  }
  else {
    contentPanel = _appContentPanelList.get(tabIndex);
  }
  final GLGridWidget gridWidget;
  gridWidget = GridWidgetManager.getPetGrid("Pets" + (_appContentPanelList.size() + 1), //
                                            inlineEditingCheckBox.getValue(), //
                                            checkBoxSelectionModelCheckBox.getValue(), //
                                            rowLevelCommitsCheckBox.getValue());
  contentPanel.setWidget(gridWidget);
  DBAccess.loadPets(gridWidget.getListStore());
}
//--------------------------------------------------------------------------------------------------
private void createTableNameCombobox() {
  _tableNameCombobox = new SimpleComboBox<>(new StringLabelProvider<>());
  _tableNameCombobox.setEmptyText("Select a table ...");
  _tableNameCombobox.setTriggerAction(TriggerAction.ALL);
  _tableNameCombobox.setTypeAhead(true);
  _tableNameCombobox.setWidth(200);
  for (final IGLTable table : EFAPTable.values()) {
    _tableNameCombobox.add(table.toString());
  }
  tableNameComboBoxContainer.add(_tableNameCombobox);
}
//--------------------------------------------------------------------------------------------------
public void createTestGrid() {
  final ContentPanel contentPanel = new ContentPanel();
  contentPanel.setHeaderVisible(false);
  _appContentPanelList.add(contentPanel);
  appTabPanel.add(contentPanel, new TabItemConfig("Test", true));
  final TestGrid testGrid = new TestGrid();
  contentPanel.setWidget(testGrid.asWidget());
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"genericTableGridButton"})
public void onGenericTableGridButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  final String tableName = _tableNameCombobox.getCurrentValue();
  if (tableName == null) {
    GLLog.popup(10, "You must select a table");
    return;
  }
  final ContentPanel contentPanel = new ContentPanel();
  contentPanel.setHeaderVisible(false);
  _appContentPanelList.add(contentPanel);
  appTabPanel.add(contentPanel, new TabItemConfig(tableName, true));
  final GLGenericGridWidget gridWidget = GLGenericGridWidget.createGenericGridWidget(tableName);
  contentPanel.setWidget(gridWidget.asWidget());
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"gridDebugButton"})
public void onGridDebugButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  final GLGridContentPanel panel = (GLGridContentPanel)_appContentPanelList.get(0).getWidget();
  final Widget childWidget = panel.getWidget();
  GLLog.popup(20, (childWidget == null ? "null" : getClass().toString()));
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"petsButton"})
public void onPetsButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  createPetGrid(-1);
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"recreateGridButton"})
public void onRecreateGridButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  GLLog.popup(10, "Recreating grid ...");
  createPetGrid(0);
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
        DBAccess.reloadTestData();
      }
    }
  });
  messageBox.show();
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"testGridButton"})
public void onTestGridButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  createTestGrid();
}
//--------------------------------------------------------------------------------------------------
}