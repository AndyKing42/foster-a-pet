package org.greatlogic.glgwt.client.event;

import org.greatlogic.glgwt.client.event.GLLookupTableLoadedEvent.IGLLookupTableLoadedEventHandler;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

public class GLLookupTableLoadedEvent extends Event<IGLLookupTableLoadedEventHandler> {
//--------------------------------------------------------------------------------------------------
public static final Type<IGLLookupTableLoadedEventHandler> LookupTableLoadedEventType;

private final boolean                                      _reloadedFromServer;
private final IGLTable                                     _table;
//==================================================================================================
public interface IGLLookupTableLoadedEventHandler extends EventHandler {
public void onLookupTableLoadedEvent(final GLLookupTableLoadedEvent lookupTableLoadedEvent);
}
//==================================================================================================
static {
  LookupTableLoadedEventType = new Type<IGLLookupTableLoadedEventHandler>();
}
//--------------------------------------------------------------------------------------------------
public GLLookupTableLoadedEvent(final IGLTable table, final boolean reloadedFromServer) {
  _table = table;
  _reloadedFromServer = reloadedFromServer;
}
//--------------------------------------------------------------------------------------------------
@Override
protected void dispatch(final IGLLookupTableLoadedEventHandler handler) {
  handler.onLookupTableLoadedEvent(this);
}
//--------------------------------------------------------------------------------------------------
@Override
public Type<IGLLookupTableLoadedEventHandler> getAssociatedType() {
  return LookupTableLoadedEventType;
}
//--------------------------------------------------------------------------------------------------
public boolean getReloadedFromServer() {
  return _reloadedFromServer;
}
//--------------------------------------------------------------------------------------------------
public IGLTable getTable() {
  return _table;
}
//--------------------------------------------------------------------------------------------------
@Override
public String toString() {
  return "LookupTableLoadedEvent - table:" + _table;
}
//--------------------------------------------------------------------------------------------------
}