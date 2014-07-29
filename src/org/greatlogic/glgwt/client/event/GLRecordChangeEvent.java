package org.greatlogic.glgwt.client.event;

import org.greatlogic.glgwt.client.core.GLRecord;
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