package org.greatlogic.glgwt.client.widget;

public class GLContextMenuEntry {
//--------------------------------------------------------------------------------------------------
private final IGLContextMenuSelectionHandler _selectionHandler;
private final String                         _text;
//--------------------------------------------------------------------------------------------------
public GLContextMenuEntry(final String text, final IGLContextMenuSelectionHandler selectionHandler) {
  _text = text;
  _selectionHandler = selectionHandler;
}
//--------------------------------------------------------------------------------------------------
public IGLContextMenuSelectionHandler getSelectionHandler() {
  return _selectionHandler;
}
//--------------------------------------------------------------------------------------------------
public String getText() {
  return _text;
}
//--------------------------------------------------------------------------------------------------
}