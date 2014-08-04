package org.greatlogic.glgwt.client.event;
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