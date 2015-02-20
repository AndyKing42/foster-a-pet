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
import org.greatlogic.glgwt.client.core.GLLog;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.Event.Type;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class GLEventBus {
//--------------------------------------------------------------------------------------------------
private final SimpleEventBus _eventBus;
//--------------------------------------------------------------------------------------------------
public GLEventBus() {
  _eventBus = new SimpleEventBus();
}
//--------------------------------------------------------------------------------------------------
public <H> HandlerRegistration addHandler(final Type<H> type, final H handler) {
  return _eventBus.addHandler(type, handler);
}
//--------------------------------------------------------------------------------------------------
public void fireEvent(final Event<?> event) {
  _eventBus.fireEvent(event);
  GLLog.infoDetail("GLEventBus.fireEvent", "Event fired-" + event.toDebugString());
}
//--------------------------------------------------------------------------------------------------
public SimpleEventBus getEventBus() {
  return _eventBus;
}
//--------------------------------------------------------------------------------------------------
}