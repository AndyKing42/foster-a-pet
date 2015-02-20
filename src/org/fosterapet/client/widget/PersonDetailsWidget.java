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
import org.fosterapet.shared.IDBEnums.Person;
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

public class PersonDetailsWidget extends Composite {
//--------------------------------------------------------------------------------------------------
@UiField
ContentPanel                 detailPanel;
@UiField
HBoxLayoutContainer          mainRecordContainer;

private GLGridWidget         _fosterHistoryGridWidget;
private GLGridWidget         _orgPersonGridWidget;
private final GLRecord       _person;
private GLGridWidget         _personRelationshipGridWidget;
private final GLRecordEditor _recordEditor;
//==================================================================================================
interface PersonDetailsWidgetUiBinder extends UiBinder<Widget, PersonDetailsWidget> { //
}
//==================================================================================================
public PersonDetailsWidget(final GLRecord person) {
  _person = person;
  final PersonDetailsWidgetUiBinder uiBinder = GWT.create(PersonDetailsWidgetUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
  _recordEditor = new GLRecordEditor(person, true, mainRecordContainer);
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"fosterHistoryButton"})
public void onFosterHistoryButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  if (_fosterHistoryGridWidget == null) {
    try {
      _fosterHistoryGridWidget =
                                 GridWidgetManager.getFosterHistoryGridUsingPersonId(_person.asInt(Person.PersonId));
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
@UiHandler({"orgPersonButton"})
public void onOrgPersonButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  if (_orgPersonGridWidget == null) {
    try {
      _orgPersonGridWidget = GridWidgetManager.getOrgPersonGrid(_person.asInt(Person.PersonId));
      _orgPersonGridWidget.loadData(detailPanel);
    }
    catch (final GLDBException e) {
      GLLog.popup(20, "Creation of the OrgPerson grid failed:" + e.getMessage());
    }
  }
  else {
    detailPanel.setWidget(_orgPersonGridWidget);
  }
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"personRelationshipButton"})
public void onPersonRelationshipButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  if (_personRelationshipGridWidget == null) {
    try {
      _personRelationshipGridWidget =
                                      GridWidgetManager.getPersonRelationshipGrid(_person.asInt(Person.PersonId));
      _personRelationshipGridWidget.loadData(detailPanel);
    }
    catch (final GLDBException e) {
      GLLog.popup(20, "Creation of the PersonRelationship grid failed:" + e.getMessage());
    }
  }
  else {
    detailPanel.setWidget(_personRelationshipGridWidget);
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