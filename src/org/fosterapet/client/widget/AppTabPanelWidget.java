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
import org.fosterapet.shared.IDBEnums.Pet;
import org.greatlogic.glgwt.client.core.GLRecord;
import org.greatlogic.glgwt.client.widget.grid.GLGenericGridWidget;
import org.greatlogic.glgwt.client.widget.grid.GLGridWidget;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;

public class AppTabPanelWidget extends Composite implements ProvidesResize, RequiresResize {
//--------------------------------------------------------------------------------------------------
@UiField
PlainTabPanel                         tabPanel;

private final ArrayList<ContentPanel> _appContentPanelList;
//==================================================================================================
interface AppTabPanelWidgetUiBinder extends UiBinder<Widget, AppTabPanelWidget> { //
}
//==================================================================================================
public AppTabPanelWidget() {
  super();
  _appContentPanelList = new ArrayList<>();
  final AppTabPanelWidgetUiBinder uiBinder = GWT.create(AppTabPanelWidgetUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
}
//--------------------------------------------------------------------------------------------------
public ContentPanel addTab(final String tabLabel) {
  final ContentPanel result = new ContentPanel();
  result.setHeaderVisible(false);
  _appContentPanelList.add(result);
  tabPanel.add(result, new TabItemConfig(tabLabel, true));
  tabPanel.setActiveWidget(tabPanel.getWidget(getNumberOfTabs() - 1));
  return result;
}
//--------------------------------------------------------------------------------------------------
public void createGenericTableGrid(final IGLTable table) {
  final ContentPanel contentPanel = addTab(table.toString());
  final GLGenericGridWidget gridWidget;
  gridWidget = new GLGenericGridWidget(table);
  contentPanel.setWidget(gridWidget.asWidget());
  DBAccess.load(gridWidget.getListStore(), table, null, true);
}
//--------------------------------------------------------------------------------------------------
public void createPetDetails(final GLRecord pet) {
  final ContentPanel contentPanel = addTab(pet.asString(Pet.PetName));
  contentPanel.setWidget(new PetDetailsWidget(pet));
}
//--------------------------------------------------------------------------------------------------
public void createPetGrid(final boolean inlineEditing, final boolean checkBoxSelectionModel,
                          final boolean rowLevelCommits) {
  final ContentPanel contentPanel = addTab("Pets-" + (getNumberOfTabs() + 1));
  final GLGridWidget gridWidget;
  gridWidget = GridWidgetManager.getPetGrid("Pets" + (getNumberOfTabs() + 1), inlineEditing, //
                                            checkBoxSelectionModel, rowLevelCommits);
  contentPanel.setWidget(gridWidget);
  DBAccess.load(gridWidget.getListStore(), EFAPTable.Pet, Pet.PetName.name(), false);
}
//--------------------------------------------------------------------------------------------------
public void createTestGrid() {
  final ContentPanel contentPanel = addTab("Test");
  final TestGrid testGrid = new TestGrid();
  contentPanel.setWidget(testGrid.asWidget());
}
//--------------------------------------------------------------------------------------------------
public int getNumberOfTabs() {
  return tabPanel.getWidgetCount();
}
//--------------------------------------------------------------------------------------------------
@Override
public void onResize() {
  tabPanel.setPixelSize(tabPanel.getParent().getOffsetWidth(), //
                        tabPanel.getParent().getOffsetHeight());
  final Widget activeWidget = tabPanel.getActiveWidget();
  if (activeWidget instanceof RequiresResize) {
    ((RequiresResize)activeWidget).onResize();
  }
}
//--------------------------------------------------------------------------------------------------
}