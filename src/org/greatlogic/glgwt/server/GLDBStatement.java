package org.greatlogic.glgwt.server;

import org.apache.commons.lang3.StringUtils;
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
private static void delete(final String tableName, final String keyColumnName,
                           final String restOfLine) throws GLDBException {
  final GLSQL sql = GLSQL.update(tableName);
  sql.setValue("ArchiveDate", GLUtil.currentTimeYYYYMMDDHHMMSS());
  sql.setValue("Version", GLServerUtil.generateVersion());
  sql.whereAnd(0, keyColumnName + " in (" + restOfLine + ")", 0);
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
public static String select(final String xmlRequest, final boolean includeArchivedRows) {
  GLLog.debug(xmlRequest);
  final StringBuilder result = new StringBuilder();
  try {
    final GLXML xml = new GLXML(xmlRequest);
    final GLSQL sql = GLSQL.selectUsingXML(xml);
    if (!includeArchivedRows) {
      sql.whereAnd(0, "ArchiveDate is null", 0);
    }
    sql.open();
    try {
      result.append(StringUtils.join(sql.getColumnNameIterable(), ',')).append('\n');
      while (sql.next(false)) {
        sql.getRowAsCSV(result);
        result.append('\n');
      }
    }
    finally {
      sql.close();
    }
  }
  catch (final GLDBException dbe) {
    GLLog.major("Error executing 'select'", dbe);
    result.setLength(0);
    result.append("error");
  }
  catch (final GLXMLException xmle) {
    GLLog.major("Error processing XML for 'select'", xmle);
    result.setLength(0);
    result.append("error");
  }
  return result.toString();
}
//--------------------------------------------------------------------------------------------------
}