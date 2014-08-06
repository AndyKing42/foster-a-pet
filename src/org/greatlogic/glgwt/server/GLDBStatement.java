package org.greatlogic.glgwt.server;
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
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.lang3.StringUtils;
import org.greatlogic.glgwt.client.db.GLDBUpdate;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLSharedEnums.EGLSQLAttribute;
import org.greatlogic.glgwt.shared.IGLTable;
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLSQL;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.GLUtil;
import com.greatlogic.glbase.glxml.GLXML;
import com.greatlogic.glbase.glxml.GLXMLException;

class GLDBStatement {
//==================================================================================================
private enum EChangeType {
Delete,
Insert,
Unknown,
Update;
private static EChangeType lookup(final char changeTypeChar) {
  switch (changeTypeChar) {
    case 'D':
      return Delete;
    case 'I':
      return Insert;
    case 'U':
      return Update;
  }
  return Unknown;
}
}
//==================================================================================================
/**
 * Inserts, updates, or deletes rows. The entries in the "dbChanges" parameter are broken into
 * linefeed-separated lines. Each line represents an insert (one per line), update (one per line),
 * or deletes (any number per line). Each line begins with a single character that indicates whether
 * this is an insert ("I"), update ("U"), or delete ("D") line. This character is followed by a
 * hyphen, and then the table name. The table name is followed by a forward slash, and then the key
 * column name, and then an equals sign. On insert and update lines the equals sign is followed by
 * the key value for the row that is to be inserted or updated, followed by a colon and then a
 * comma-delimited list of column name, equals sign, and value; on delete lines the equals sign is
 * followed by a comma-delimited list of key values for the rows that are to be deleted. An example
 * containing each type of line:
 * 
 * <pre>
 * I-table_name1/key_column_name1=key-value:column=value;column=value;column=value<linefeed>
 * U-table_name1/key_column_name1=key-value:column=value;column=value;column=value<linefeed>
 * D-table_name1/key_column_name1=key-value,key-value,key-value<linefeed>
 * </pre>
 */
public static void applyDBChanges(final String dbChanges) {
  try {
    final String[] changeLines = dbChanges.split("\n");
    for (final String changeLine : changeLines) {
      if (changeLine.length() > 6) {
        final EChangeType changeType = EChangeType.lookup(changeLine.charAt(0));
        if (changeType != EChangeType.Unknown) {
          final int changeLineLength = changeLine.length();
          final int slashIndex = changeLine.indexOf('/', 3);
          if (slashIndex > 0 && slashIndex < changeLineLength - 3) {
            final String tableName = changeLine.substring(2, slashIndex);
            final int equalsIndex = changeLine.indexOf('=', slashIndex + 2);
            if (equalsIndex > 0 && equalsIndex < changeLineLength - 1) {
              final String keyColumnName = changeLine.substring(slashIndex + 1, equalsIndex);
              final String restOfLine = changeLine.substring(equalsIndex + 1);
              switch (changeType) {
                case Delete:
                  delete(tableName, keyColumnName, restOfLine);
                  break;
                case Insert:
                  insertOrUpdateRows(changeType, tableName, keyColumnName, restOfLine);
                  break;
                case Unknown:
                  break;
                case Update:
                  insertOrUpdateRows(changeType, tableName, keyColumnName, restOfLine);
                  break;
              }
            }
          }
        }
      }
    }
  }
  catch (final Exception e) {
    GLLog.major("Error executing SQL for:" + dbChanges, e);
  }
}
//--------------------------------------------------------------------------------------------------
public static void applyDBChanges(final TreeMap<IGLTable, TreeSet<String>> deletedKeyValueMap,
                                  final ArrayList<GLDBUpdate> dbUpdateList) {
  try {
    if (deletedKeyValueMap != null) {
      for (final Entry<IGLTable, TreeSet<String>> deletedKeyEntry : deletedKeyValueMap.entrySet()) {
        delete(deletedKeyEntry.getKey(), deletedKeyEntry.getValue());
      }
    }
    if (dbUpdateList != null) {
      for (final GLDBUpdate dbUpdate : dbUpdateList) {
        insertOrUpdateRows(dbUpdate);
      }
    }
  }
  catch (final Exception e) {
    GLLog.major("Error executing SQL for:" + dbUpdateList, e);
  }
}
//--------------------------------------------------------------------------------------------------
private static void delete(final IGLTable table, final TreeSet<String> keyValueSet)
  throws GLDBException {
  final GLSQL sql = GLSQL.update(table.toString());
  sql.setValue("ArchiveDate", GLUtil.currentTimeYYYYMMDDHHMMSS());
  sql.setValue("Version", GLServerUtil.generateVersion());
  sql.whereAnd(0, table.getPrimaryKeyColumn() + " in (" + //
                  GLUtil.iterableAsCommaDelim(keyValueSet, null) + ")", 0);
  sql.execute();
}
//--------------------------------------------------------------------------------------------------
private static void delete(final String tableName, final String keyColumnName,
                           final String restOfLine) throws GLDBException {
  final GLSQL sql = GLSQL.update(tableName);
  sql.setValue("ArchiveDate", GLUtil.currentTimeYYYYMMDDHHMMSS());
  sql.setValue("Version", GLServerUtil.generateVersion());
  sql.whereAnd(0, keyColumnName + " in (" + restOfLine + ")", 0);
  sql.execute();
}
//--------------------------------------------------------------------------------------------------
private static void insertOrUpdateRows(final GLDBUpdate dbUpdate) throws GLDBException {
  final IGLTable table = dbUpdate.getTable();
  final IGLColumn primaryKeyColumn = table.getPrimaryKeyColumn();
  final String primaryKeyValue = dbUpdate.getPrimaryKeyValue();
  final GLSQL sql;
  if (dbUpdate.getInserted()) {
    sql = GLSQL.insert(table.toString(), false);
    sql.setValue(primaryKeyColumn.toString(), primaryKeyValue);
  }
  else {
    sql = GLSQL.update(table.toString());
  }
  for (final Entry<IGLColumn, String> modifiedColumnEntry : dbUpdate.getModifiedColumnMap()
                                                                    .entrySet()) {
    final String value = modifiedColumnEntry.getValue();
    sql.setValue(modifiedColumnEntry.getKey().toString(), value.isEmpty() ? null : value);
  }
  sql.setValue("Version", GLServerUtil.generateVersion());
  if (!dbUpdate.getInserted()) {
    sql.whereAnd(0, primaryKeyColumn + "=" + primaryKeyValue, 0);
  }
  sql.execute();
}
//--------------------------------------------------------------------------------------------------
private static void insertOrUpdateRows(final EChangeType changeType, final String tableName,
                                       final String keyColumnName, final String restOfLine)
  throws GLDBException {
  final int colonIndex = restOfLine.indexOf(':', 1);
  if (colonIndex > 0 && colonIndex < restOfLine.length() - 3) {
    final String keyValue = restOfLine.substring(0, colonIndex);
    final String[] columnNamesAndValues = restOfLine.substring(colonIndex + 1).split(";");
    final GLSQL sql;
    if (changeType == EChangeType.Insert) {
      sql = GLSQL.insert(tableName, false);
      sql.setValue(keyColumnName, keyValue);
    }
    else {
      sql = GLSQL.update(tableName);
    }
    for (final String columnNameAndValue : columnNamesAndValues) {
      final int equalsIndex = columnNameAndValue.indexOf('=');
      if (equalsIndex > 0) {
        final String columnName = columnNameAndValue.substring(0, equalsIndex);
        final String value = columnNameAndValue.substring(equalsIndex + 1);
        sql.setValue(columnName, value.isEmpty() ? null : value);
      }
    }
    sql.setValue("Version", GLServerUtil.generateVersion());
    if (changeType == EChangeType.Update) {
      sql.whereAnd(0, keyColumnName + "=" + keyValue, 0);
    }
    sql.execute();
  }
}
//--------------------------------------------------------------------------------------------------
public static ArrayList<String> select(final String xmlRequest) {
  GLLog.debug(xmlRequest);
  final ArrayList<String> result = new ArrayList<>();
  try {
    final GLXML xml = new GLXML(xmlRequest);
    final GLSQL sql = GLSQL.selectUsingXML(xml);
    if (!xml.getTopElement().attributeAsBoolean(EGLSQLAttribute.IncludeArchivedRows.name())) {
      sql.whereAnd(0, "ArchiveDate is null", 0);
    }
    sql.open();
    try {
      result.add(StringUtils.join(sql.getColumnNameIterable(), ','));
      final StringBuilder rowSB = new StringBuilder(100);
      while (sql.next(false)) {
        rowSB.setLength(0);
        result.add(sql.getRowAsCSV(rowSB).toString());
      }
    }
    finally {
      sql.close();
    }
  }
  catch (final GLDBException dbe) {
    GLLog.major("Error executing 'select'", dbe);
    result.clear();
  }
  catch (final GLXMLException xmle) {
    GLLog.major("Error processing XML for 'select'", xmle);
    result.clear();
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
}