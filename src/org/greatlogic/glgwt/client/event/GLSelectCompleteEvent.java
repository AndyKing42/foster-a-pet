package org.greatlogic.glgwt.client.event;

import org.greatlogic.glgwt.client.db.GLListStore;
import org.greatlogic.glgwt.client.event.GLSelectCompleteEvent.IGLSelectCompleteEventHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

public class GLSelectCompleteEvent extends Event<IGLSelectCompleteEventHandler> {
//--------------------------------------------------------------------------------------------------
public static final Type<IGLSelectCompleteEventHandler> SelectCompleteEventType;
private final GLListStore                               _listStore;
//==================================================================================================
public interface IGLSelectCompleteEventHandler extends EventHandler {
public void onSelectCompleteEvent(final GLSelectCompleteEvent selectCompleteEvent);
}
//==================================================================================================
static {
  SelectCompleteEventType = new Type<IGLSelectCompleteEventHandler>();
}
//--------------------------------------------------------------------------------------------------
public GLSelectCompleteEvent(final GLListStore listStore) {
  _listStore = listStore;
}
//--------------------------------------------------------------------------------------------------
@Override
protected void dispatch(final IGLSelectCompleteEventHandler handler) {
  handler.onSelectCompleteEvent(this);
}
//--------------------------------------------------------------------------------------------------
@Override
public Type<IGLSelectCompleteEventHandler> getAssociatedType() {
  return SelectCompleteEventType;
}
//--------------------------------------------------------------------------------------------------
public GLListStore getListStore() {
  return _listStore;
}
//--------------------------------------------------------------------------------------------------
@Override
public String toString() {
  return "SelectCompleteEvent";
}
//--------------------------------------------------------------------------------------------------
}