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
import org.fosterapet.shared.IDBEnums.Pet;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.db.GLDBException;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.db.GLRecordEditor;
import org.greatlogic.glgwt.client.widget.grid.GLGridWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

public class PetDetailsWidget extends Composite {
//--------------------------------------------------------------------------------------------------
@UiField
ContentPanel                 detailPanel;
@UiField
HBoxLayoutContainer          mainRecordContainer;

private GLGridWidget         _fosterHistoryGridWidget;
private final GLRecord       _pet;
private GLGridWidget         _planEntryGridWidget;
private final GLRecordEditor _recordEditor;
private GLGridWidget         _treatmentGridWidget;
//==================================================================================================
interface PetDetailsWidgetUiBinder extends UiBinder<Widget, PetDetailsWidget> { //
}
//==================================================================================================
public PetDetailsWidget(final GLRecord pet) {
  _pet = pet;
  final PetDetailsWidgetUiBinder uiBinder = GWT.create(PetDetailsWidgetUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
  _recordEditor = new GLRecordEditor(pet, true, mainRecordContainer);
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"fosterHistoryButton"})
public void onFosterHistoryButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  if (_fosterHistoryGridWidget == null) {
    try {
      _fosterHistoryGridWidget =
                                 GridWidgetManager.getFosterHistoryGridUsingPetId(_pet.asInt(Pet.PetId));
      _fosterHistoryGridWidget.loadData(detailPanel);
    }
    catch (final GLDBException e) {
      GLLog.popup(20, "Creation of the FosterHistory grid failed:" + e.getMessage());
    }
  }
  else {
    detailPanel.setWidget(_fosterHistoryGridWidget);
  }
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"planEntryButton"})
public void onPlanEntryButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  if (_planEntryGridWidget == null) {
    try {
      _planEntryGridWidget = GridWidgetManager.getPlanEntryGridUsingPetId(_pet.asInt(Pet.PetId));
      _planEntryGridWidget.loadData(detailPanel);
    }
    catch (final GLDBException e) {
      GLLog.popup(20, "Creation of the PlanEntry grid failed:" + e.getMessage());
    }
  }
  else {
    detailPanel.setWidget(_planEntryGridWidget);
  }
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"treatmentButton"})
public void onTreatmentButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  if (_treatmentGridWidget == null) {
    try {
      _treatmentGridWidget = GridWidgetManager.getTreatmentGridUsingPetId(_pet.asInt(Pet.PetId));
      _treatmentGridWidget.loadData(detailPanel);
    }
    catch (final GLDBException e) {
      GLLog.popup(20, "Creation of the Treatment grid failed:" + e.getMessage());
    }
  }
  else {
    detailPanel.setWidget(_treatmentGridWidget);
  }
}
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