package org.greatlogic.glgwt.client.event;

import org.greatlogic.glgwt.client.core.GLRecord;
import org.greatlogic.glgwt.client.event.GLNewRecordEvent.IGLNewRecordEventHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

public class GLNewRecordEvent extends Event<IGLNewRecordEventHandler> {
//--------------------------------------------------------------------------------------------------
public static final Type<IGLNewRecordEventHandler> NewRecordEventType;

private final GLRecord                             _record;
//==================================================================================================
public interface IGLNewRecordEventHandler extends EventHandler {
public void onNewRecordEvent(final GLNewRecordEvent newRecordEvent);
}
//==================================================================================================
static {
  NewRecordEventType = new Type<IGLNewRecordEventHandler>();
}
//--------------------------------------------------------------------------------------------------
public GLNewRecordEvent(final GLRecord record) {
  _record = record;
}
//--------------------------------------------------------------------------------------------------
@Override
protected void dispatch(final IGLNewRecordEventHandler handler) {
  handler.onNewRecordEvent(this);
}
//--------------------------------------------------------------------------------------------------
@Override
public Type<IGLNewRecordEventHandler> getAssociatedType() {
  return NewRecordEventType;
}
//--------------------------------------------------------------------------------------------------
public GLRecord getRecord() {
  return _record;
}
//--------------------------------------------------------------------------------------------------
@Override
public String toString() {
  return "NewRecordEvent - record:" + _record;
}
//--------------------------------------------------------------------------------------------------
}