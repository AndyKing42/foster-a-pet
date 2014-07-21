package org.greatlogic.glgwt.client.event;

import org.greatlogic.glgwt.client.event.GLCommitCompleteEvent.IGLCommitCompleteEventHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

public class GLCommitCompleteEvent extends Event<IGLCommitCompleteEventHandler> {
//--------------------------------------------------------------------------------------------------
public static final Type<IGLCommitCompleteEventHandler> CommitCompleteEventType;
//==================================================================================================
public interface IGLCommitCompleteEventHandler extends EventHandler {
public void onCommitCompleteEvent(final GLCommitCompleteEvent commitCompleteEvent);
}
//==================================================================================================
static {
  CommitCompleteEventType = new Type<IGLCommitCompleteEventHandler>();
}
//--------------------------------------------------------------------------------------------------
public GLCommitCompleteEvent() {

}
//--------------------------------------------------------------------------------------------------
@Override
protected void dispatch(final IGLCommitCompleteEventHandler handler) {
  handler.onCommitCompleteEvent(this);
}
//--------------------------------------------------------------------------------------------------
@Override
public Type<IGLCommitCompleteEventHandler> getAssociatedType() {
  return CommitCompleteEventType;
}
//--------------------------------------------------------------------------------------------------
@Override
public String toString() {
  return "CommitCompleteEvent";
}
//--------------------------------------------------------------------------------------------------
}