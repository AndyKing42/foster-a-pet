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
import org.greatlogic.glgwt.client.core.GLLog;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

public class MainMenuWidget extends Composite {
//--------------------------------------------------------------------------------------------------
@UiField
TextButton            petsButton;

private PlainTabPanel _appTabPanel;
//==================================================================================================
interface MainMenuWidgetUiBinder extends UiBinder<Widget, MainMenuWidget> { //
}
//==================================================================================================
public MainMenuWidget() {
  super();
  final MainMenuWidgetUiBinder uiBinder = GWT.create(MainMenuWidgetUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
  addPetsContextMenuHandler();
}
//--------------------------------------------------------------------------------------------------
private void addPetsContextMenuHandler() {
  petsButton.sinkEvents(Event.ONCONTEXTMENU);
  petsButton.addHandler(new ContextMenuHandler() {
    @Override
    public void onContextMenu(final ContextMenuEvent event) {
      GLLog.popup(20, "hey");
      event.preventDefault();
    }
  }, ContextMenuEvent.getType());
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"petsButton"})
public void onPetsButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  //createPetGrid(-1);
}
//--------------------------------------------------------------------------------------------------
public void setAppTabPanel(final PlainTabPanel appTabPanel) {
  _appTabPanel = appTabPanel;
}
//--------------------------------------------------------------------------------------------------
}