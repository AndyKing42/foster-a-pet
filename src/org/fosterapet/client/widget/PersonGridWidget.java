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
import org.fosterapet.client.FAPUtil;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.Person;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.IGLClientEnums.EGLContextMenuItemType;
import org.greatlogic.glgwt.client.core.IGLClientEnums.EGLGridContentPanelButtonType;
import org.greatlogic.glgwt.client.db.GLDBException;
import org.greatlogic.glgwt.client.db.GLSQL;
import org.greatlogic.glgwt.client.widget.grid.GLGridContextMenuSelectionEvent;
import org.greatlogic.glgwt.client.widget.grid.GLGridWidget;
import org.greatlogic.glgwt.client.widget.grid.IGLGridContextMenuSelectionHandler;
import org.greatlogic.glgwt.shared.GLRecordValidator;
import org.greatlogic.glgwt.shared.IGLColumn;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class PersonGridWidget extends GLGridWidget {
//--------------------------------------------------------------------------------------------------
public PersonGridWidget(final GLRecordValidator recordValidator, final boolean inlineEditing,
                        final boolean useCheckBoxSelectionModel, final boolean rowLevelCommits,
                        final IGLColumn... columns) throws GLDBException {
  super(EFAPTable.Person, null, "There are no people", recordValidator, inlineEditing,
        useCheckBoxSelectionModel, rowLevelCommits, columns);
}
//--------------------------------------------------------------------------------------------------
@Override
protected void addContentPanelButtons() {
  addContentPanelButton("View Details", new SelectHandler() {
    @Override
    public void onSelect(final SelectEvent event) {
      FAPUtil.getClientFactory().getAppTabPanelWidget()
             .createPersonDetails(_grid.getSelectionModel().getSelectedItem());
    }
  });
  addContentPanelButton("New Person", EGLGridContentPanelButtonType.New);
  addContentPanelButton("Save Changes", EGLGridContentPanelButtonType.Save);
  addContentPanelButton("Undo Changes", EGLGridContentPanelButtonType.Undo);
  addContentPanelButton("Delete Selected", EGLGridContentPanelButtonType.Delete);
}
//--------------------------------------------------------------------------------------------------
@Override
protected void addContextMenuItems() {
  addContextMenuItem("View Details", new IGLGridContextMenuSelectionHandler() {
    @Override
    public void onContextMenuSelectionEvent(final GLGridContextMenuSelectionEvent event) {
      FAPUtil.getClientFactory().getAppTabPanelWidget()
             .createPersonDetails(event.getSelectedRecord());
    }
  });
  addContextMenuItem("Select All", EGLContextMenuItemType.SelectAll);
  addContextMenuItem("Clear Selections", EGLContextMenuItemType.ClearAll);
  addContextMenuItem("Delete", EGLContextMenuItemType.Delete);
  addContextMenuItem("Change Password", new IGLGridContextMenuSelectionHandler() {
    @Override
    public void onContextMenuSelectionEvent(final GLGridContextMenuSelectionEvent event) {
      GLClientUtil.changePassword(event.getSelectedRecord().asInt(Person.PersonId), null);
    }
  });
}
//--------------------------------------------------------------------------------------------------
@Override
protected void addFilters() {
  addFilter(Person.DateOfBirth);
  addFilter(Person.DisplayName);
  addFilter(Person.EmailAddress);
  addFilter(Person.FirstName);
  addFilter(Person.LastName);
  addFilter(Person.LoginName);
  addFilter(Person.PhoneNumberHome);
  addFilter(Person.PhoneNumberMobile);
  addFilter(Person.PhoneNumberOffice);
}
//--------------------------------------------------------------------------------------------------
@Override
public GLSQL getSQL() throws GLDBException {
  if (_sql == null) {
    _sql = GLSQL.select();
    _sql.from(EFAPTable.Person);
    FAPUtil.addStandardSQLWhere(_sql);
    final String orgPersonExistsSQL;
    orgPersonExistsSQL = "exists (select 'x' from " + EFAPTable.OrgPerson + //
                         " where OrgPerson.OrgId = " + //
                         FAPUtil.getClientFactory().getLoginPersonRecord(). //
                                asInt(Person.CurrentOrgId) + //
                         " and OrgPerson.PersonId = Person.PersonId)";
    _sql.whereAnd(0, orgPersonExistsSQL, 0);
    _sql.orderBy(Person.DisplayName.name());
  }
  return _sql;
}
//--------------------------------------------------------------------------------------------------
}