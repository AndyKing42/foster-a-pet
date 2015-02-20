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
import org.fosterapet.shared.IDBEnums.FosterHistory;
import org.fosterapet.shared.IDBEnums.Treatment;
import org.greatlogic.glgwt.client.core.IGLClientEnums.EGLContextMenuItemType;
import org.greatlogic.glgwt.client.core.IGLClientEnums.EGLGridContentPanelButtonType;
import org.greatlogic.glgwt.client.db.GLDBException;
import org.greatlogic.glgwt.client.db.GLSQL;
import org.greatlogic.glgwt.client.widget.grid.GLGridWidget;
import org.greatlogic.glgwt.shared.GLRecordValidator;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLSharedEnums.EGLDBOp;

public class TreatmentGridWidget extends GLGridWidget {
//--------------------------------------------------------------------------------------------------
private static int _createPetId; /* this is only used during creation of the grid */
//--------------------------------------------------------------------------------------------------
public TreatmentGridWidget(final GLRecordValidator recordValidator, final boolean inlineEditing,
                           final boolean useCheckBoxSelectionModel, final boolean rowLevelCommits,
                           final IGLColumn... columns) throws GLDBException {
  super(EFAPTable.FosterHistory, "There are no treatments for this person", recordValidator,
        inlineEditing, useCheckBoxSelectionModel, rowLevelCommits, columns);
  _createPetId = -1;
}
//--------------------------------------------------------------------------------------------------
@Override
protected void addButtons() {
  addButton("New Treatment Entry", EGLGridContentPanelButtonType.New);
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
  addFilter(Treatment.LocId);
  addFilter(Treatment.ScheduledDate);
  addFilter(Treatment.TreatmentDate);
  addFilter(Treatment.TreatmentDesc);
  addFilter(Treatment.TreatmentTypeId);
}
//--------------------------------------------------------------------------------------------------
@Override
public GLSQL getSQL() throws GLDBException {
  if (_sql == null) {
    _sql = GLSQL.select();
    _sql.from(EFAPTable.Treatment);
    _sql.whereAnd(FosterHistory.PetId, EGLDBOp.Equals, _createPetId);
    _sql.whereAnd(FosterHistory.ArchiveDate, EGLDBOp.IsNull, null);
  }
  return _sql;
}
//--------------------------------------------------------------------------------------------------
public static void setCreatePetId(final int createPetId) {
  _createPetId = createPetId;
}
//--------------------------------------------------------------------------------------------------
}