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
import org.greatlogic.glgwt.client.event.GLRecordChangeEvent.IGLRecordChangeEventHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

public class GLRecordChangeEvent extends Event<IGLRecordChangeEventHandler> {
//--------------------------------------------------------------------------------------------------
public static final Type<IGLRecordChangeEventHandler> RecordChangeEventType;

private final String                                  _columnName;
private final Object                                  _newValue;
private final Object                                  _oldValue;
private final GLRecord                                _record;
//==================================================================================================
public interface IGLRecordChangeEventHandler extends EventHandler {
public void onRecordChangeEvent(final GLRecordChangeEvent recordChangeEvent);
}
//==================================================================================================
static {
  RecordChangeEventType = new Type<IGLRecordChangeEventHandler>();
}
//--------------------------------------------------------------------------------------------------
public GLRecordChangeEvent(final GLRecord record, final String columnName, final Object oldValue,
                           final Object newValue) {
  super();
  _record = record;
  _columnName = columnName;
  _oldValue = oldValue;
  _newValue = newValue;
}
//--------------------------------------------------------------------------------------------------
@Override
protected void dispatch(final IGLRecordChangeEventHandler handler) {
  handler.onRecordChangeEvent(this);
}
//--------------------------------------------------------------------------------------------------
@Override
public Type<IGLRecordChangeEventHandler> getAssociatedType() {
  return RecordChangeEventType;
}
//--------------------------------------------------------------------------------------------------
public String getColumnName() {
  return _columnName;
}
//--------------------------------------------------------------------------------------------------
public Object getNewValue() {
  return _newValue;
}
//--------------------------------------------------------------------------------------------------
public Object getOldValue() {
  return _oldValue;
}
//--------------------------------------------------------------------------------------------------
public GLRecord getRecord() {
  return _record;
}
//--------------------------------------------------------------------------------------------------
}