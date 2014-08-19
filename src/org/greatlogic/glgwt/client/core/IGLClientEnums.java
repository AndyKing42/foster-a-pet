package org.greatlogic.glgwt.client.core;

import java.util.Map;
import java.util.TreeMap;
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
public interface IGLClientEnums {
//--------------------------------------------------------------------------------------------------
public enum EGLContextMenuItemType {
ClearAll,
Delete,
SelectAll,
ViewDetails
}
//--------------------------------------------------------------------------------------------------
public enum EGLGridContentPanelButtonType {
Delete,
New,
Save,
Undo,
ViewDetails
}
//--------------------------------------------------------------------------------------------------
public enum EGLXMLEscapeSequence {
Ampersand("amp", "#38", '&'),
Apostrophe("apos", "#39", '\''),
GreaterThan("gt", "#62", '>'),
LessThan("lt", "#60", '<'),
Quote("quot", "#34", '"'),
Unknown("", "", ' ');
private static Map<String, EGLXMLEscapeSequence> _escapeSequenceByValueMap;
private final char                               _escapeChar;
private final String                             _escapeCharAsString;
private final String                             _escapeNumeric;
private final String                             _escapeString;
public static EGLXMLEscapeSequence lookupUsingValue(final String value) {
  if (_escapeSequenceByValueMap == null) {
    _escapeSequenceByValueMap = new TreeMap<>();
    for (final EGLXMLEscapeSequence escapeSequence : EGLXMLEscapeSequence.values()) {
      _escapeSequenceByValueMap.put(escapeSequence._escapeString, escapeSequence);
      _escapeSequenceByValueMap.put(escapeSequence._escapeNumeric, escapeSequence);
    }
  }
  return _escapeSequenceByValueMap.get(value);
}
EGLXMLEscapeSequence(final String escapeString, final String escapeNumeric, final char escapeChar) {
  _escapeString = escapeString;
  _escapeNumeric = escapeNumeric;
  _escapeChar = escapeChar;
  _escapeCharAsString = Character.toString(_escapeChar);
}
public char getEscapeChar() {
  return _escapeChar;
}
public String getEscapeCharAsString() {
  return _escapeCharAsString;
}
public String getEscapeString() {
  return _escapeString;
}
}
//--------------------------------------------------------------------------------------------------
}