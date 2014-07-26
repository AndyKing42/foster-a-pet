package org.greatlogic.glgwt.client.widget.grid;
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
import java.math.RoundingMode;
import java.util.Date;
import org.greatlogic.glgwt.client.core.GLRecord;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.sencha.gxt.core.client.ValueProvider;

abstract class GLValueProviderClasses {
//==================================================================================================
public static class GLBigDecimalValueProvider implements ValueProvider<GLRecord, BigDecimal> {
private final IGLColumn _column;
private final int       _numberOfDecimalPlaces;
public GLBigDecimalValueProvider(final IGLColumn column, final int numberOfDecimalPlaces) {
  _column = column;
  _numberOfDecimalPlaces = numberOfDecimalPlaces;
}
@Override
public BigDecimal getValue(final GLRecord record) {
  return record.asDec(_column);
}
@Override
public void setValue(final GLRecord record, final BigDecimal value) {
  record.put(_column, value.setScale(_numberOfDecimalPlaces, RoundingMode.HALF_UP));
}
@Override
public String getPath() {
  return _column.toString();
}
}
//==================================================================================================
public static class GLBooleanValueProvider implements ValueProvider<GLRecord, Boolean> {
private final IGLColumn _column;
public GLBooleanValueProvider(final IGLColumn column) {
  _column = column;
}
@Override
public Boolean getValue(final GLRecord record) {
  return record.asBoolean(_column);
}
@Override
public void setValue(final GLRecord record, final Boolean value) {
  record.put(_column, value);
}
@Override
public String getPath() {
  return _column.toString();
}
}
//==================================================================================================
public static class GLDateValueProvider implements ValueProvider<GLRecord, Date> {
private final IGLColumn _column;
public GLDateValueProvider(final IGLColumn column) {
  _column = column;
}
@SuppressWarnings("deprecation")
@Override
public Date getValue(final GLRecord record) {
  final String date = record.asString(_column);
  if (date.length() < 8) {
    return null;
  }
  final int year = GLClientUtil.stringToInt(date.substring(0, 4));
  final int month = GLClientUtil.stringToInt(date.substring(4, 6));
  final int day = GLClientUtil.stringToInt(date.substring(6, 8));
  return new Date(year - 1900, month - 1, day);
}
@Override
public void setValue(final GLRecord record, final Date value) {
  record.put(_column, value == null ? null : DateTimeFormat.getFormat("yyyyMMdd").format(value));
}
@Override
public String getPath() {
  return _column.toString();
}
}
//==================================================================================================
public static class GLForeignKeyValueProvider implements ValueProvider<GLRecord, String> {
private final IGLColumn _column;
private final IGLTable  _lookupTable;
public GLForeignKeyValueProvider(final IGLTable lookupTable, final IGLColumn column) {
  _lookupTable = lookupTable;
  _column = column;
}
@Override
public String getValue(final GLRecord record) {
  return GLClientUtil.getLookupCache().lookupDisplayValue(_lookupTable, record.asInt(_column));
}
@Override
public void setValue(final GLRecord record, final String value) {
  record.put(_column, GLClientUtil.getLookupCache().lookupKeyValue(_lookupTable, value));
}
@Override
public String getPath() {
  return _column.toString();
}
}
//==================================================================================================
public static class GLIntegerValueProvider implements ValueProvider<GLRecord, Integer> {
private final IGLColumn _column;
public GLIntegerValueProvider(final IGLColumn column) {
  _column = column;
}
@Override
public Integer getValue(final GLRecord record) {
  return record.asInt(_column);
}
@Override
public void setValue(final GLRecord record, final Integer value) {
  record.put(_column, value);
}
@Override
public String getPath() {
  return _column.toString();
}
}
//==================================================================================================
public static class GLStringValueProvider implements ValueProvider<GLRecord, String> {
private final IGLColumn _column;
public GLStringValueProvider(final IGLColumn column) {
  _column = column;
}
@Override
public String getValue(final GLRecord record) {
  return record.asString(_column);
}
@Override
public void setValue(final GLRecord record, final String value) {
  record.put(_column, value);
}
@Override
public String getPath() {
  return _column.toString();
}
}
//==================================================================================================
}