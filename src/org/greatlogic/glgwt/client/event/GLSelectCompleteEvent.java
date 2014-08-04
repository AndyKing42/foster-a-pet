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