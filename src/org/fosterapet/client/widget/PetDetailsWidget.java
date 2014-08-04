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
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.db.GLRecordEditorDriver;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

public class PetDetailsWidget extends Composite {
//--------------------------------------------------------------------------------------------------
@UiField
FlowLayoutContainer                flowLayoutContainer;

private final GLRecordEditorDriver _editorDriver;
private final GLRecord             _pet;
//==================================================================================================
interface PetDetailsWidgetUiBinder extends UiBinder<Widget, PetDetailsWidget> { //
}
//==================================================================================================
public PetDetailsWidget(final GLRecord pet) {
  _pet = pet;
  final PetDetailsWidgetUiBinder uiBinder = GWT.create(PetDetailsWidgetUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
  flowLayoutContainer.setScrollMode(ScrollMode.AUTO);
  _editorDriver = new GLRecordEditorDriver(EFAPTable.class);
  _editorDriver.addWidget(flowLayoutContainer);
  _editorDriver.edit(_pet);
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"saveButton"})
public void onSaveButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  _editorDriver.flush();
}
//--------------------------------------------------------------------------------------------------
}