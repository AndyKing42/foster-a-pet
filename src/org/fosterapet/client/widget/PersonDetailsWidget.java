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
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.db.GLRecordEditor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

public class PersonDetailsWidget extends Composite {
//--------------------------------------------------------------------------------------------------
//@UiField
//CardLayoutContainer          cardLayoutContainer;
@UiField
FlowLayoutContainer          flowLayoutContainer;
//@UiField
//ContentPanel                 orgPanel;
@UiField
VBoxLayoutContainer          mainContainer;

private final GLRecordEditor _recordEditor;
//==================================================================================================
interface PersonDetailsWidgetUiBinder extends UiBinder<Widget, PersonDetailsWidget> { //
}
//==================================================================================================
public PersonDetailsWidget(final GLRecord person) {
  final PersonDetailsWidgetUiBinder uiBinder = GWT.create(PersonDetailsWidgetUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
  flowLayoutContainer.setScrollMode(ScrollMode.AUTO);
  _recordEditor = new GLRecordEditor(person, true, flowLayoutContainer);
  GLLog.popup(20, "flowLayoutContainer.height:" + flowLayoutContainer.getOffsetHeight(true));
  GLLog.popup(20, "mainContainer.height:" + mainContainer.getOffsetHeight(true));
}
//--------------------------------------------------------------------------------------------------
//@UiHandler({"orgButton"})
//public void onOrgButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
//  cardLayoutContainer.setActiveWidget(orgPanel);
//}
//--------------------------------------------------------------------------------------------------
@UiHandler({"saveButton"})
public void onSaveButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  GLClientUtil.getDBUpdater().saveAllChanges();
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"undoButton"})
public void onUndoButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  _recordEditor.undoChanges();
}
//--------------------------------------------------------------------------------------------------
}