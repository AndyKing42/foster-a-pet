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
import org.fosterapet.shared.IDBEnums.OrgPerson;
import org.greatlogic.glgwt.client.core.IGLClientEnums.EGLContextMenuItemType;
import org.greatlogic.glgwt.client.core.IGLClientEnums.EGLGridContentPanelButtonType;
import org.greatlogic.glgwt.client.db.GLDBException;
import org.greatlogic.glgwt.client.db.GLSQL;
import org.greatlogic.glgwt.client.db.IGLColumnInitializer;
import org.greatlogic.glgwt.client.widget.grid.GLGridWidget;
import org.greatlogic.glgwt.shared.GLRecordValidator;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLSharedEnums.EGLDBOp;

public class OrgPersonGridWidget extends GLGridWidget implements IGLColumnInitializer {
//--------------------------------------------------------------------------------------------------
private static int _createPersonId; /* this is only used during creation of the grid */
//--------------------------------------------------------------------------------------------------
public OrgPersonGridWidget(final GLRecordValidator recordValidator, final boolean inlineEditing,
                           final boolean useCheckBoxSelectionModel, final boolean rowLevelCommits,
                           final IGLColumn... columns) throws GLDBException {
  super(EFAPTable.OrgPerson, "There are no organizations for this person", recordValidator,
        inlineEditing, useCheckBoxSelectionModel, rowLevelCommits, columns);
  _createPersonId = -1;
}
//--------------------------------------------------------------------------------------------------
@Override
protected void addButtons() {
  addButton("New Organization Entry", EGLGridContentPanelButtonType.New, this);
  addButton("Delete Selected", EGLGridContentPanelButtonType.Delete);
}
//--------------------------------------------------------------------------------------------------
@Override
protected void addContextMenuItems() {
  addContextMenuItem("Select All", EGLContextMenuItemType.SelectAll);
  addContextMenuItem("Clear Selections", EGLContextMenuItemType.ClearAll);
  addContextMenuItem("Delete", EGLContextMenuItemType.Delete);
}
//--------------------------------------------------------------------------------------------------
@Override
protected void addFilters() {
  addFilter(OrgPerson.OrgId);
  addFilter(OrgPerson.PersonRoleId);
}
//--------------------------------------------------------------------------------------------------
@Override
public GLSQL getSQL() throws GLDBException {
  if (_sql == null) {
    _sql = GLSQL.select();
    _sql.from(EFAPTable.OrgPerson);
    _sql.whereAnd(OrgPerson.PersonId, EGLDBOp.Equals, _createPersonId);
    _sql.whereAnd(OrgPerson.ArchiveDate, EGLDBOp.IsNull, null);
  }
  return _sql;
}
//--------------------------------------------------------------------------------------------------
@Override
public Object initializeColumn(final IGLColumn column) {
  if (column == OrgPerson.PersonId) {
    return 1;
  }
  return null;
}
//--------------------------------------------------------------------------------------------------
public static void setCreatePersonId(final int createPersonId) {
  _createPersonId = createPersonId;
}
//--------------------------------------------------------------------------------------------------
}