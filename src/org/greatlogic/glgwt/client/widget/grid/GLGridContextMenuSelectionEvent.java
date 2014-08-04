package org.greatlogic.glgwt.client.widget.grid;
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
import org.greatlogic.glgwt.client.db.GLRecord;
import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class GLGridContextMenuSelectionEvent extends GwtEvent<IGLGridContextMenuSelectionHandler> {
//--------------------------------------------------------------------------------------------------
private static Type<IGLGridContextMenuSelectionHandler> TYPE;

private final MenuItem                                  _selectedMenuItem;
private final GLRecord                                  _selectedRecord;
//--------------------------------------------------------------------------------------------------
public static Type<IGLGridContextMenuSelectionHandler> getType() {
  if (TYPE == null) {
    TYPE = new Type<IGLGridContextMenuSelectionHandler>();
  }
  return TYPE;
}
//--------------------------------------------------------------------------------------------------
public GLGridContextMenuSelectionEvent(final MenuItem selectedMenuItem,
                                       final GLRecord selectedRecord) {
  _selectedMenuItem = selectedMenuItem;
  _selectedRecord = selectedRecord;
}
//--------------------------------------------------------------------------------------------------
@Override
public final Type<IGLGridContextMenuSelectionHandler> getAssociatedType() {
  return TYPE;
}
//--------------------------------------------------------------------------------------------------
public MenuItem getSelectedItem() {
  return _selectedMenuItem;
}
//--------------------------------------------------------------------------------------------------
public GLRecord getSelectedRecord() {
  return _selectedRecord;
}
//--------------------------------------------------------------------------------------------------
@Override
protected void dispatch(final IGLGridContextMenuSelectionHandler handler) {
  handler.onContextMenuSelectionEvent(this);
}
//--------------------------------------------------------------------------------------------------
}