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
import org.fosterapet.shared.IDBEnums.Pet;
import org.greatlogic.glgwt.client.core.IGLClientEnums.EGLContextMenuItemType;
import org.greatlogic.glgwt.client.core.IGLClientEnums.EGLGridContentPanelButtonType;
import org.greatlogic.glgwt.client.widget.grid.GLGridContextMenuSelectionEvent;
import org.greatlogic.glgwt.client.widget.grid.GLGridWidget;
import org.greatlogic.glgwt.client.widget.grid.IGLGridContextMenuSelectionHandler;
import org.greatlogic.glgwt.shared.GLRecordValidator;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class PetGridWidget extends GLGridWidget {
//--------------------------------------------------------------------------------------------------
public PetGridWidget(final GLRecordValidator recordValidator, final boolean inlineEditing,
                     final boolean useCheckBoxSelectionModel, final boolean rowLevelCommits,
                     final Pet... petColumns) {
  super(null, "There are no pets", recordValidator, inlineEditing, useCheckBoxSelectionModel,
        rowLevelCommits, petColumns);
}
//--------------------------------------------------------------------------------------------------
@Override
protected void addContentPanelButtons() {
  addContentPanelButton("View Details", new SelectHandler() {
    @Override
    public void onSelect(final SelectEvent event) {
      FAPUtil.getClientFactory().getAppTabPanelWidget()
             .createPetDetails(_grid.getSelectionModel().getSelectedItem());
    }
  });
  addContentPanelButton("New Pet", EGLGridContentPanelButtonType.New);
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
      FAPUtil.getClientFactory().getAppTabPanelWidget().createPetDetails(event.getSelectedRecord());
    }
  });
  addContextMenuItem("Select All", EGLContextMenuItemType.SelectAll);
  addContextMenuItem("Clear Selections", EGLContextMenuItemType.ClearAll);
  addContextMenuItem("Delete", EGLContextMenuItemType.Delete);
}
//--------------------------------------------------------------------------------------------------
@Override
protected void addFilters() {
  addFilter(Pet.AdoptionFee);
  addFilter(Pet.DateOfBirth);
  addFilter(Pet.IntakeDate, new DateWrapper().addYears(-20).asDate(), //
            new DateWrapper().addDays(1).asDate());
  addFilter(Pet.PetName);
  addFilter(Pet.PetTypeId);
  addFilter(Pet.Sex);
}
//--------------------------------------------------------------------------------------------------
}