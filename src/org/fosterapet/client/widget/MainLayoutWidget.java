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
import org.fosterapet.client.DBAccess;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.widget.GLGenericGridWidget;
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
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

public class MainLayoutWidget extends Composite {
//--------------------------------------------------------------------------------------------------
@UiField
AppTabPanelWidget              appTabPanelWidget;
@UiField
CheckBox                       checkBoxSelectionModelCheckBox;
@UiField
CheckBox                       inlineEditingCheckBox;
@UiField
MainMenuWidget                 mainMenuWidget;
@UiField
CheckBox                       rowLevelCommitsCheckBox;
@UiField
SimpleContainer                tableNameComboBoxContainer;

private SimpleComboBox<String> _tableNameCombobox;
//==================================================================================================
interface MainLayoutWidgetUiBinder extends UiBinder<Widget, MainLayoutWidget> { //
}
//==================================================================================================
public MainLayoutWidget() {
  super();
  final MainLayoutWidgetUiBinder uiBinder = GWT.create(MainLayoutWidgetUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
  mainMenuWidget.setAppTabPanelWidget(appTabPanelWidget);
  createTableNameCombobox();
}
//--------------------------------------------------------------------------------------------------
public void createGenericTableGrid(final IGLTable table) {
  final ContentPanel contentPanel = appTabPanelWidget.addTab(table.toString());
  final GLGenericGridWidget gridWidget;
  gridWidget = GLGenericGridWidget.createGenericGridWidget(table.toString());
  contentPanel.setWidget(gridWidget.asWidget());
  DBAccess.load(gridWidget.getListStore(), table, null, true);
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
public AppTabPanelWidget getAppTabPanelWidget() {
  return appTabPanelWidget;
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"genericTableGridButton"})
public void onGenericTableGridButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  final String tableName = _tableNameCombobox.getCurrentValue();
  if (tableName == null) {
    GLLog.popup(10, "You must select a table");
    return;
  }
  final IGLTable table = EFAPTable.valueOf(tableName);
  createGenericTableGrid(table);
}
//--------------------------------------------------------------------------------------------------
}