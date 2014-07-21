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
import java.util.ArrayList;

public class GLCSV {
//--------------------------------------------------------------------------------------------------
public static final char DefaultDelimiter = '"';
public static final char DefaultSeparator = ',';
//==================================================================================================
public enum EGLCSVException {
InvalidCharacter,
MissingEndDelimiter;
} // enum EGLCSVException
//==================================================================================================
public static StringBuilder appendCSVValue(final StringBuilder sb, final Object value) {
  return appendCSVValue(sb, value, DefaultSeparator, DefaultDelimiter);
} // appendCSVValue()
//--------------------------------------------------------------------------------------------------
public static StringBuilder appendCSVValue(final StringBuilder sb, final Object value,
                                           final char separator, final char delimiter) {
  if (sb == null) {
    throw new NullPointerException("A StringBuilder object is required");
  }
  String stringValue = value == null ? "" : value.toString();
  if (stringValue.indexOf(delimiter) >= 0) {
    stringValue = delimiter + stringValue.replace("" + delimiter, "" + delimiter + delimiter) + //
                  delimiter;
  }
  else if (stringValue.indexOf(separator) >= 0 ||
           (!stringValue.isEmpty() && (stringValue.charAt(0) == ' ' || //
           stringValue.charAt(stringValue.length() - 1) == ' '))) {
    stringValue = delimiter + stringValue + delimiter;
  }
  sb.append(stringValue);
  return sb;
} // appendCSVValue()
//--------------------------------------------------------------------------------------------------
public static StringBuilder buildCSV(final Iterable<? extends Object> values) {
  return buildCSV(null, values, DefaultSeparator, DefaultDelimiter);
} // buildCSV()
//--------------------------------------------------------------------------------------------------
public static StringBuilder buildCSV(final Iterable<? extends Object> values, final char separator,
                                     final char delimiter) {
  return buildCSV(null, values, separator, delimiter);
} // buildCSV()
//--------------------------------------------------------------------------------------------------
public static StringBuilder buildCSV(final StringBuilder sb, final Iterable<? extends Object> values) {
  return buildCSV(sb, values, DefaultSeparator, DefaultDelimiter);
} // buildCSV()
//--------------------------------------------------------------------------------------------------
public static StringBuilder buildCSV(final StringBuilder sb,
                                     final Iterable<? extends Object> values, final char separator,
                                     final char delimiter) {
  final StringBuilder result = sb == null ? new StringBuilder(100) : sb;
  boolean appendSeparator = false;
  for (final Object value : values) {
    if (appendSeparator) {
      result.append(separator);
    }
    appendSeparator = true;
    appendCSVValue(result, value, separator, delimiter);
  }
  return result;
} // buildCSV()
//--------------------------------------------------------------------------------------------------
/**
 * Extracts a string in CSV format into a list of strings.
 */
public static ArrayList<String> extract(final String csv) throws GLCSVException {
  return extract(null, csv, DefaultSeparator, DefaultDelimiter);
} // extract()
//--------------------------------------------------------------------------------------------------
/**
 * Extracts a string in CSV format into a list of strings.
 */
public static ArrayList<String> extract(final ArrayList<String> origList, final String csv,
                                        final char separator, final char delimiter)
  throws GLCSVException {
  final ArrayList<String> result = origList == null ? new ArrayList<String>() : origList;
  result.clear();
  int beginIndex = 0;
  while (beginIndex < csv.length()) {
    while (beginIndex < csv.length() && csv.charAt(beginIndex) == ' ') { // skip whitespace
      ++beginIndex;
    }
    String value;
    if (beginIndex == csv.length()) {
      value = "";
    }
    else {
      int endIndex;
      final boolean delimited = csv.charAt(beginIndex) == delimiter;
      if (delimited) {
        ++beginIndex;
        endIndex = beginIndex;
        boolean endOfValue = false;
        boolean doubleDelimiter = false;
        do {
          if (endIndex == csv.length()) {
            throw new GLCSVException(EGLCSVException.MissingEndDelimiter, //
                                     "At position:" + beginIndex);
          }
          if (csv.charAt(endIndex) == delimiter) {
            endOfValue = endIndex == csv.length() - 1 || csv.charAt(endIndex + 1) != delimiter;
            doubleDelimiter = doubleDelimiter || !endOfValue;
            endIndex += doubleDelimiter ? 2 : 0;
          }
          else {
            ++endIndex;
          }
        } while (!endOfValue);
        value = csv.substring(beginIndex, endIndex);
        value = doubleDelimiter ? value.replace("" + delimiter + delimiter, "" + delimiter) : value;
        ++endIndex;
        while (endIndex < csv.length() && csv.charAt(endIndex) != separator) {
          if (csv.charAt(endIndex) != ' ') {
            throw new GLCSVException(EGLCSVException.InvalidCharacter, //
                                     "At position:" + endIndex + 1);
          }
          ++endIndex;
        }
      }
      else { // the CSV string does not begin with a delimiter
        endIndex = csv.indexOf(separator, beginIndex);
        endIndex = endIndex == -1 ? endIndex = csv.length() : endIndex;
        value = csv.substring(beginIndex, endIndex).trim();
      }
      beginIndex = endIndex + 1;
    }
    result.add(value);
  }
  if (csv.length() == 0 || csv.charAt(csv.length() - 1) == separator) {
    result.add("");
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
}