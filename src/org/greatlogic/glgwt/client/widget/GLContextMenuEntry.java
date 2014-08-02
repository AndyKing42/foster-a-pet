package org.greatlogic.glgwt.client.widget;

import org.greatlogic.glgwt.client.widget.grid.IGLGridContextMenuSelectionHandler;

public class GLContextMenuEntry {
//--------------------------------------------------------------------------------------------------
private final IGLGridContextMenuSelectionHandler _selectionHandler;
private final String                         _menuLabel;
//--------------------------------------------------------------------------------------------------
public GLContextMenuEntry(final String menuLabel,
                          final IGLGridContextMenuSelectionHandler selectionHandler) {
  _menuLabel = menuLabel;
  _selectionHandler = selectionHandler;
}
//--------------------------------------------------------------------------------------------------
public String getMenuLabel() {
  return _menuLabel;
}
//--------------------------------------------------------------------------------------------------
public IGLGridContextMenuSelectionHandler getSelectionHandler() {
  return _selectionHandler;
}
//--------------------------------------------------------------------------------------------------
}