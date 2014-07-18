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
import org.fosterapet.client.ClientFactory;
import org.fosterapet.client.DBAccess;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.widget.GLGridWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;

public class MainLayoutWidget extends Composite {
//--------------------------------------------------------------------------------------------------
@UiField
ContentPanel                centerPanel;
@UiField
CheckBox                    checkBoxSelectionModelCheckBox;
@UiField
TextButton                  gaeStuffButton;
@UiField
CheckBox                    inlineEditingCheckBox;
@UiField
TextButton                  recreateGridButton;
@UiField
TextButton                  recreateTablesButton;
@UiField
TextButton                  reloadTestDataButton;
@UiField
CheckBox                    rowLevelCommitsCheckBox;
private final ClientFactory _clientFactory;
//==================================================================================================
interface MainLayoutWidgetUiBinder extends UiBinder<Widget, MainLayoutWidget> { //
}
//==================================================================================================
public MainLayoutWidget(final ClientFactory clientFactory) {
  super();
  _clientFactory = clientFactory;
  final MainLayoutWidgetUiBinder uiBinder = GWT.create(MainLayoutWidgetUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"gaeStuffButton"})
public void onGAEStuffButtonClick(@SuppressWarnings("unused") final SelectEvent event) {
  ClientFactory.Instance.getRemoteService().gaeTest(new AsyncCallback<String>() {
    @Override
    public void onFailure(final Throwable caught) {
      GLLog.popup(30, "GAE test failed:" + caught.getMessage());
    }
    @Override
    public void onSuccess(final String result) {
      GLLog.popup(30, "GAE test:" + result);
    }
  });
}
//--------------------------------------------------------------------------------------------------
public ContentPanel getCenterPanel() {
  return centerPanel;
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"recreateGridButton"})
public void onRecreateGridButtonClick(@SuppressWarnings("unused") final SelectEvent event) {
  GLLog.popup(10, "Recreating grid ...");
  final GLGridWidget gridWidget;
  gridWidget = GridWidgetManager.getPetGrid("Main", inlineEditingCheckBox.getValue(), //
                                            checkBoxSelectionModelCheckBox.getValue(), //
                                            rowLevelCommitsCheckBox.getValue());
  _clientFactory.getCenterPanel().setWidget(gridWidget);
  DBAccess.loadPets(gridWidget.getListStore());
  _clientFactory.getCenterPanel().forceLayout();
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"recreateTablesButton"})
public void onRecreateTablesButtonClick(@SuppressWarnings("unused") final SelectEvent event) {
  final ConfirmMessageBox messageBox;
  messageBox = new ConfirmMessageBox("Recreate Database Tables", //
                                     "Are you sure you want to drop and recreate the " //
                                             + "database tables?");
  messageBox.addDialogHideHandler(new DialogHideHandler() {
    @Override
    public void onDialogHide(final DialogHideEvent hideEvent) {
      if (hideEvent.getHideButton() == PredefinedButton.YES) {
        DBAccess.recreateTables();
      }
    }
  });
  messageBox.show();
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"reloadTestDataButton"})
public void onReloadTestDataButtonClick(@SuppressWarnings("unused") final SelectEvent event) {
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
}