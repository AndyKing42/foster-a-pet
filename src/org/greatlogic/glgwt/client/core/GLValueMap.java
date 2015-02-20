package org.greatlogic.glgwt.client.core;
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
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import org.greatlogic.glgwt.shared.IGLColumn;
/**
 * Instances of this class represent a map of keys to values. The benefit of using this class over
 * the Properties or a simple {@link Map} object are that the values can be accessed using
 * {@link IGLColumn} enum entries, in addition to using simple String keys. Also, the values can be
 * retrieved and stored using simple data types, and values can be retrieved using a different data
 * type than the data type of the original value, where the returned value will be automatically
 * converted (with a best guess attempt in some cases).
 */
public class GLValueMap {
//--------------------------------------------------------------------------------------------------
private final boolean                 _skipKeyUnderscores;
private StringBuffer                  _toStringSB;
private final TreeMap<String, Object> _valueMap;
//--------------------------------------------------------------------------------------------------
/**
 * Creates a new value map for storing values by key. The keys can be <code>String</code> values or
 * <code>IGLXMLAttribute</code> values.
 * @param skipKeyUnderscores If the key values contain underscores then the key will be stripped up
 * to and including the last underscore.
 */
public GLValueMap(final boolean skipKeyUnderscores) {
  _skipKeyUnderscores = skipKeyUnderscores;
  _valueMap = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
}
//--------------------------------------------------------------------------------------------------
/**
 * Creates a new value map by copying the entries from an existing <code>GLValueMap</code>.
 * @param valueMap The existing value map.
 */
public GLValueMap(final GLValueMap valueMap) {
  _skipKeyUnderscores = valueMap._skipKeyUnderscores;
  _valueMap = new TreeMap<String, Object>(valueMap._valueMap);
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
public boolean asBoolean(final String key) {
  return asBoolean(key, false);
}
//--------------------------------------------------------------------------------------------------
public boolean asBoolean(final String key, final boolean defaultValue) {
  return GLClientUtil.stringToBoolean(asString(key), defaultValue);
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
public BigDecimal asDec(final String key) {
  return asDec(key, BigDecimal.ZERO);
}
//--------------------------------------------------------------------------------------------------
public BigDecimal asDec(final String key, final BigDecimal defaultValue) {
  return GLClientUtil.stringToDec(asString(key), defaultValue);
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
public double asDouble(final String key) {
  return asDouble(key, 0);
}
//--------------------------------------------------------------------------------------------------
public double asDouble(final String key, final double defaultValue) {
  return GLClientUtil.stringToDouble(asString(key), defaultValue);
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
public float asFloat(final String key) {
  return asFloat(key, 0);
}
//--------------------------------------------------------------------------------------------------
public float asFloat(final String key, final float defaultValue) {
  return (float)asDouble(key, defaultValue);
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
public int asInt(final String key) {
  return asInt(key, 0);
}
//--------------------------------------------------------------------------------------------------
public int asInt(final String key, final int defaultValue) {
  return (int)asLong(key, defaultValue);
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
public long asLong(final String key) {
  return asLong(key, 0);
}
//--------------------------------------------------------------------------------------------------
public long asLong(final String key, final long defaultValue) {
  return GLClientUtil.stringToLong(asString(key), defaultValue);
}
//--------------------------------------------------------------------------------------------------
public Object asObject(final IGLColumn column) {
  return asObject(column.toString());
}
//--------------------------------------------------------------------------------------------------
public Object asObject(final String key) {
  return _valueMap.get(key);
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
public String asString(final String key) {
  return asString(key, "");
}
//--------------------------------------------------------------------------------------------------
/**
 * Returns a string representation of the value retrieved using <code>key</code>. If the value is a
 * <code>Date</code> type value then the result will be returned as a date/time in the format
 * YYYYMMDDHHMMSS.
 * @param key The key of the value that is to be returned.
 * @param defaultValue The default value that will be returned if there is no entry in the map for
 * the specified key.
 * @return A string representation of the requested value.
 */
public String asString(final String key, final String defaultValue) {
  return GLClientUtil.formatObjectSpecial(_valueMap.get(getAdjustedKey(key)), defaultValue);
}
//--------------------------------------------------------------------------------------------------
public void clear() {
  _valueMap.clear();
}
//--------------------------------------------------------------------------------------------------
public boolean containsKey(final String key) {
  return _valueMap.containsKey(getAdjustedKey(key));
}
//--------------------------------------------------------------------------------------------------
public Set<Entry<String, Object>> entrySet() {
  return _valueMap.entrySet();
}
//--------------------------------------------------------------------------------------------------
public String firstKey() {
  return _valueMap.firstKey();
}
//--------------------------------------------------------------------------------------------------
public String getAdjustedKey(final String key) {
  String result;
  if (_skipKeyUnderscores) {
    final int underscoreIndex = key.lastIndexOf('_');
    if (underscoreIndex < 0 || underscoreIndex == key.length() - 1) {
      result = key;
    }
    else {
      result = key.substring(underscoreIndex + 1);
    }
  }
  else {
    result = key;
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
public TreeMap<String, Object> getValueMap() {
  return _valueMap;
}
//--------------------------------------------------------------------------------------------------
public boolean isEmpty() {
  return _valueMap.isEmpty();
}
//--------------------------------------------------------------------------------------------------
/**
 * Returns the set of keys for the value map.
 * @return The set of keys for the value map.
 */
public Set<String> keySet() {
  return _valueMap.keySet();
}
//--------------------------------------------------------------------------------------------------
public Object put(final String key, final Object value) {
  return _valueMap.put(getAdjustedKey(key), value);
}
//--------------------------------------------------------------------------------------------------
public Object put(final IGLColumn column, final Object value) {
  return _valueMap.put(column.toString(), value);
}
//--------------------------------------------------------------------------------------------------
public void putAll(final Map<String, ? extends Object> map) {
  if (_skipKeyUnderscores) {
    for (final Map.Entry<String, ? extends Object> mapEntry : map.entrySet()) {
      _valueMap.put(getAdjustedKey(mapEntry.getKey()), mapEntry.getValue());
    }
  }
  else {
    _valueMap.putAll(map);
  }
}
//--------------------------------------------------------------------------------------------------
public Object remove(final String key) {
  return _valueMap.remove(getAdjustedKey(key));
}
//--------------------------------------------------------------------------------------------------
public int size() {
  return _valueMap.size();
}
//--------------------------------------------------------------------------------------------------
@Override
public String toString() {
  if (_toStringSB == null) {
    _toStringSB = new StringBuffer(100);
  }
  _toStringSB.setLength(0);
  for (final Map.Entry<String, Object> mapEntry : _valueMap.entrySet()) {
    if (_toStringSB.length() > 0) {
      _toStringSB.append(";");
    }
    _toStringSB.append(mapEntry.getKey()).append(":").append(mapEntry.getValue());
  }
  return _toStringSB.toString();
}
//--------------------------------------------------------------------------------------------------
public Collection<Object> values() {
  return _valueMap.values();
}
//--------------------------------------------------------------------------------------------------
}
