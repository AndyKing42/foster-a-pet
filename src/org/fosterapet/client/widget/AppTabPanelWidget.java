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
import java.util.HashMap;
import org.fosterapet.client.DBAccess;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.Person;
import org.fosterapet.shared.IDBEnums.Pet;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.event.GLRecordChangeEvent;
import org.greatlogic.glgwt.client.event.GLRecordChangeEvent.IGLRecordChangeEventHandler;
import org.greatlogic.glgwt.client.widget.grid.GLGenericGridWidget;
import org.greatlogic.glgwt.client.widget.grid.GLGridWidget;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.event.CloseEvent;
import com.sencha.gxt.widget.core.client.event.CloseEvent.CloseHandler;

public class AppTabPanelWidget extends Composite implements ProvidesResize, RequiresResize {
//--------------------------------------------------------------------------------------------------
@UiField
PlainTabPanel                                            tabPanel;

private final ArrayList<ContentPanel>                    _appContentPanelList;
private final HashMap<ContentPanel, HandlerRegistration> _tabCloseHandlerRegistrationMap;
//==================================================================================================
interface AppTabPanelWidgetUiBinder extends UiBinder<Widget, AppTabPanelWidget> { //
}
//==================================================================================================
public AppTabPanelWidget() {
  super();
  _appContentPanelList = new ArrayList<>();
  _tabCloseHandlerRegistrationMap = new HashMap<>();
  final AppTabPanelWidgetUiBinder uiBinder = GWT.create(AppTabPanelWidgetUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
}
//--------------------------------------------------------------------------------------------------
/**
 * Changes the tab label if there is a change to the record shown on the tab. This is just to handle
 * changes to the record that affect the tab label.
 */
private void addRecordChangeEventHandler(final GLRecord record, final IGLColumn column,
                                         final ContentPanel contentPanel,
                                         final TabItemConfig tabItemConfig) {
  final IGLRecordChangeEventHandler recordChangeEventHandler = new IGLRecordChangeEventHandler() {
    @Override
    public void onRecordChangeEvent(final GLRecordChangeEvent recordChangeEvent) {
      tabItemConfig.setText(record.asString(column));
      tabPanel.update(contentPanel, tabItemConfig);
    }
  };
  final HandlerRegistration recordChangeEventHandlerRegistration;
  recordChangeEventHandlerRegistration =
                                         GLClientUtil.getEventBus()
                                                     .addHandler(GLRecordChangeEvent.RecordChangeEventType,
                                                                 recordChangeEventHandler);
  final HandlerRegistration closeHandlerRegistration;
  closeHandlerRegistration = tabPanel.addCloseHandler(new CloseHandler<Widget>() {
    @Override
    public void onClose(final CloseEvent<Widget> event) {
      if (event.getItem() == contentPanel) {
        recordChangeEventHandlerRegistration.removeHandler();
        final HandlerRegistration handlerRegistration;
        handlerRegistration = _tabCloseHandlerRegistrationMap.get(contentPanel);
        if (handlerRegistration != null) {
          handlerRegistration.removeHandler();
        }
      }
    }
  });
  _tabCloseHandlerRegistrationMap.put(contentPanel, closeHandlerRegistration);
}
//--------------------------------------------------------------------------------------------------
public ContentPanel addTab(final String tabLabel) {
  return addTab(tabLabel, null, null);
}
//--------------------------------------------------------------------------------------------------
public ContentPanel addTab(final GLRecord record, final IGLColumn column) {
  return addTab(null, record, column);
}
//--------------------------------------------------------------------------------------------------
public ContentPanel addTab(final String tabLabel, final GLRecord record, final IGLColumn column) {
  final ContentPanel result = new ContentPanel();
  result.setHeaderVisible(false);
  _appContentPanelList.add(result);
  final String text = tabLabel == null ? record.asString(column) : tabLabel;
  final TabItemConfig tabItemConfig = new TabItemConfig(text, true);
  tabPanel.add(result, tabItemConfig);
  if (record != null) {
    addRecordChangeEventHandler(record, column, result, tabItemConfig);
  }
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
public void createPersonDetails(final GLRecord person) {
  if (person != null) {
    final ContentPanel contentPanel = addTab(person, Person.DisplayName);
    contentPanel.setWidget(new PersonDetailsWidget(person));
  }
}
//--------------------------------------------------------------------------------------------------
public void createPetDetails(final GLRecord pet) {
  if (pet != null) {
    final ContentPanel contentPanel = addTab(pet, Pet.PetName);
    contentPanel.setWidget(new PetDetailsWidget(pet));
  }
}
//--------------------------------------------------------------------------------------------------
public void createPersonGrid(final boolean inlineEditing, final boolean checkBoxSelectionModel,
                             final boolean rowLevelCommits) {
  final ContentPanel contentPanel = addTab("People-" + (getNumberOfTabs() + 1));
  final GLGridWidget gridWidget;
  gridWidget = GridWidgetManager.getPersonGrid("People" + (getNumberOfTabs() + 1), inlineEditing, //
                                               checkBoxSelectionModel, rowLevelCommits);
  contentPanel.setWidget(gridWidget);
  DBAccess.load(gridWidget.getListStore(), EFAPTable.Person, Person.DisplayName.name(), false);
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