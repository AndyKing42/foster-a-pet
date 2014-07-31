package org.greatlogic.glgwt.client.widget;

import org.greatlogic.glgwt.client.db.GLRecord;
import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class GLContextMenuSelectionEvent extends GwtEvent<IGLContextMenuSelectionHandler> {
//--------------------------------------------------------------------------------------------------
private static Type<IGLContextMenuSelectionHandler> TYPE;

private final MenuItem                              _selectedMenuItem;
private final GLRecord                              _selectedRecord;
//--------------------------------------------------------------------------------------------------
public static Type<IGLContextMenuSelectionHandler> getType() {
  if (TYPE == null) {
    TYPE = new Type<IGLContextMenuSelectionHandler>();
  }
  return TYPE;
}
//--------------------------------------------------------------------------------------------------
public GLContextMenuSelectionEvent(final MenuItem selectedMenuItem, final GLRecord selectedRecord) {
  _selectedMenuItem = selectedMenuItem;
  _selectedRecord = selectedRecord;
}
//--------------------------------------------------------------------------------------------------
@Override
public final Type<IGLContextMenuSelectionHandler> getAssociatedType() {
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
protected void dispatch(final IGLContextMenuSelectionHandler handler) {
  handler.onSelection(this);
}
//--------------------------------------------------------------------------------------------------
}