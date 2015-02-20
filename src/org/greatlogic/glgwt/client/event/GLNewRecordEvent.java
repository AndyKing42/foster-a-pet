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
import org.greatlogic.glgwt.client.db.GLRecord;
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