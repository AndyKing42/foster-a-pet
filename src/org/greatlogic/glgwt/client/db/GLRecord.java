package org.greatlogic.glgwt.client.db;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map.Entry;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.event.GLRecordChangeEvent;
import org.greatlogic.glgwt.shared.IGLColumn;
/**
 * A thin wrapper around a list of field values. The objective is to provide easy access to the
 * values in the list, converting the string values to any of a number of basic data types.
 */
public class GLRecord implements Comparable<GLRecord> {
//--------------------------------------------------------------------------------------------------
private ArrayList<String>       _changedFieldNameList;
private boolean                 _inserted;
private final GLRecordDef       _recordDef;
private final ArrayList<Object> _valueList;
//--------------------------------------------------------------------------------------------------
/**
 * Creates a new (empty) GLRecord.
 * @param recordDef The record definition associated with this record. This is used when values are
 * retrieved using the field name or column.
 */
public GLRecord(final GLRecordDef recordDef) {
  this(recordDef, null);
}
//--------------------------------------------------------------------------------------------------
/**
 * Creates a new GLRecord by copying all the values from the copy-from record.
 */
public GLRecord(final GLRecord copyFromRecord) {
  _inserted = copyFromRecord._inserted;
  _recordDef = copyFromRecord._recordDef;
  _valueList = new ArrayList<>(copyFromRecord._valueList);
}
//--------------------------------------------------------------------------------------------------
/**
 * Creates a new GLRecord.
 * @param recordDef The record definition associated with this record. This is used when values are
 * retrieved using the field name or column.
 * @param list The list of values for this record. Note that the List object is referenced from the
 * GLRecord object, and must not therefore be modified after being passed to the GLRecord
 * constructor. If the list is null then the record will be initialized based upon the default
 * values that are set in the IGLTable#initialize method.
 */
public GLRecord(final GLRecordDef recordDef, final ArrayList<Object> list) {
  _recordDef = recordDef;
  if (list == null) {
    _inserted = true;
    _valueList = new ArrayList<Object>(_recordDef.getNumberOfFields());
    _recordDef.getTable().initializeNewRecord(this);
  }
  else {
    _valueList = list;
  }
}
//--------------------------------------------------------------------------------------------------
public void addChangedField(final IGLColumn column) {
  addChangedField(column.toString());
}
//--------------------------------------------------------------------------------------------------
public void addChangedField(final String columnName) {
  getChangedFieldNameList().add(columnName);
}
//--------------------------------------------------------------------------------------------------
public boolean asBoolean(final IGLColumn column) {
  return asBoolean(column, false);
}
//--------------------------------------------------------------------------------------------------
public boolean asBoolean(final IGLColumn column, final boolean defaultValue) {
  return asBoolean(column.toString(), defaultValue);
}
//--------------------------------------------------------------------------------------------------
public boolean asBoolean(final String columnName) {
  return asBoolean(columnName, false);
}
//--------------------------------------------------------------------------------------------------
public boolean asBoolean(final String columnName, final boolean defaultValue) {
  return GLClientUtil.stringToBoolean(asString(columnName), defaultValue);
}
//--------------------------------------------------------------------------------------------------
public BigDecimal asDec(final IGLColumn column) {
  return asDec(column, BigDecimal.ZERO);
}
//--------------------------------------------------------------------------------------------------
public BigDecimal asDec(final IGLColumn column, final BigDecimal defaultValue) {
  return asDec(column.toString(), defaultValue);
}
//--------------------------------------------------------------------------------------------------
public BigDecimal asDec(final String columnName) {
  return asDec(columnName, BigDecimal.ZERO);
}
//--------------------------------------------------------------------------------------------------
public BigDecimal asDec(final String columnName, final BigDecimal defaultValue) {
  return GLClientUtil.stringToDec(asString(columnName), defaultValue);
}
//--------------------------------------------------------------------------------------------------
public double asDouble(final IGLColumn column) {
  return asDouble(column, 0);
}
//--------------------------------------------------------------------------------------------------
public double asDouble(final IGLColumn column, final double defaultValue) {
  return asDouble(column.toString(), defaultValue);
}
//--------------------------------------------------------------------------------------------------
public double asDouble(final String columnName) {
  return asDouble(columnName, 0);
}
//--------------------------------------------------------------------------------------------------
public double asDouble(final String columnName, final double defaultValue) {
  return GLClientUtil.stringToDouble(asString(columnName), defaultValue);
}
//--------------------------------------------------------------------------------------------------
public float asFloat(final IGLColumn column) {
  return asFloat(column, 0);
}
//--------------------------------------------------------------------------------------------------
public float asFloat(final IGLColumn column, final float defaultValue) {
  return asFloat(column.toString(), defaultValue);
}
//--------------------------------------------------------------------------------------------------
public float asFloat(final String columnName) {
  return asFloat(columnName, 0);
}
//--------------------------------------------------------------------------------------------------
public float asFloat(final String columnName, final float defaultValue) {
  return (float)asDouble(columnName, defaultValue);
}
//--------------------------------------------------------------------------------------------------
public int asInt(final IGLColumn column) {
  return asInt(column, 0);
}
//--------------------------------------------------------------------------------------------------
public int asInt(final IGLColumn column, final int defaultValue) {
  return asInt(column.toString(), defaultValue);
}
//--------------------------------------------------------------------------------------------------
public int asInt(final String columnName) {
  return asInt(columnName, 0);
}
//--------------------------------------------------------------------------------------------------
public int asInt(final String columnName, final int defaultValue) {
  return (int)asLong(columnName, defaultValue);
}
//--------------------------------------------------------------------------------------------------
public long asLong(final IGLColumn column) {
  return asLong(column, 0);
}
//--------------------------------------------------------------------------------------------------
public long asLong(final IGLColumn column, final long defaultValue) {
  return asLong(column.toString(), defaultValue);
}
//--------------------------------------------------------------------------------------------------
public long asLong(final String columnName) {
  return asLong(columnName, 0);
}
//--------------------------------------------------------------------------------------------------
public long asLong(final String columnName, final long defaultValue) {
  return GLClientUtil.stringToLong(asString(columnName), defaultValue);
}
//--------------------------------------------------------------------------------------------------
public Object asObject(final IGLColumn column) {
  return asObject(column.toString());
}
//--------------------------------------------------------------------------------------------------
public Object asObject(final String columnName) {
  try {
    return _valueList.get(_recordDef.getFieldIndex(columnName));
  }
  catch (final Exception e) {
    return null;
  }
}
//--------------------------------------------------------------------------------------------------
public String asString(final IGLColumn column) {
  return asString(column, "");
}
//--------------------------------------------------------------------------------------------------
public String asString(final IGLColumn column, final String defaultValue) {
  return asString(column.toString(), defaultValue);
}
//--------------------------------------------------------------------------------------------------
public String asString(final String columnName) {
  return asString(columnName, "");
}
//--------------------------------------------------------------------------------------------------
/**
 * Returns a string representation of the value retrieved using <code>columnName</code>. If the
 * value is a <code>Date</code> type value then the result will be returned as a date/time in the
 * format YYYYMMDDHHMMSS.
 * @param columnName The field name of the value that is to be returned.
 * @param defaultValue The default value that will be returned if there is no entry in the map for
 * the specified field name.
 * @return A string representation of the requested value.
 */
public String asString(final String columnName, final String defaultValue) {
  try {
    return GLClientUtil.formatObjectSpecial(_valueList.get(_recordDef.getFieldIndex(columnName)),
                                            defaultValue);
  }
  catch (final Exception e) {
    return defaultValue;
  }
}
//--------------------------------------------------------------------------------------------------
@Override
public int compareTo(final GLRecord record) {
  return getKeyValueAsString().compareTo(record.getKeyValueAsString());
}
//--------------------------------------------------------------------------------------------------
public ArrayList<String> getChangedFieldNameList() {
  if (_changedFieldNameList == null) {
    _changedFieldNameList = new ArrayList<String>();
  }
  return _changedFieldNameList;
}
//--------------------------------------------------------------------------------------------------
public boolean getInserted() {
  return _inserted;
}
//--------------------------------------------------------------------------------------------------
public String getKeyValueAsString() {
  return asString(_recordDef.getTable().getPrimaryKeyColumn());
}
//--------------------------------------------------------------------------------------------------
public GLRecordDef getRecordDef() {
  return _recordDef;
}
//--------------------------------------------------------------------------------------------------
public Object put(final IGLColumn column, final Object value) {
  return put(column.toString(), value);
}
//--------------------------------------------------------------------------------------------------
public Object put(final String columnName, final Object newValue) {
  final Object result;
  int fieldIndex;
  try {
    fieldIndex = _recordDef.getFieldIndex(columnName);
    if (fieldIndex >= _valueList.size()) {
      for (int i = _valueList.size(); i <= fieldIndex; ++i) {
        _valueList.add("");
      }
    }
  }
  catch (final GLInvalidFieldOrColumnException ifoce) {
    fieldIndex = _recordDef.addField(columnName);
    _valueList.add("");
  }
  result = _valueList.set(fieldIndex, newValue);
  GLClientUtil.getEventBus().fireEvent(new GLRecordChangeEvent(this, columnName, result, newValue));
  return result;
}
//--------------------------------------------------------------------------------------------------
public Object set(final IGLColumn column, final Object value) {
  return put(column, value);
}
//--------------------------------------------------------------------------------------------------
public Object set(final String columnName, final Object value) {
  return put(columnName, value);
}
//--------------------------------------------------------------------------------------------------
public void setInserted(final boolean inserted) {
  _inserted = inserted;
}
//--------------------------------------------------------------------------------------------------
@Override
public String toString() {
  final StringBuilder sb = new StringBuilder(_valueList.size() * 20);
  boolean firstTime = true;
  for (final Entry<String, Integer> fieldEntry : _recordDef.getFieldIndexByFieldNameMap()
                                                           .entrySet()) {
    final String columnName = fieldEntry.getKey();
    sb.append(firstTime ? "" : ";").append(columnName).append(":").append(asString(columnName));
    firstTime = false;
  }
  return sb.toString();
}
//--------------------------------------------------------------------------------------------------
}