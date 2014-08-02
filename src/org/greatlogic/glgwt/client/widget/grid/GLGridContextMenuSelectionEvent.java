package org.greatlogic.glgwt.client.widget.grid;

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