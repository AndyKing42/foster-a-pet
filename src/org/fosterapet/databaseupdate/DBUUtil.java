package org.fosterapet.databaseupdate;
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
import java.io.IOException;
import org.fosterapet.shared.IDBEnums.DBUpdateNote;
import org.fosterapet.shared.IDBEnums.EFAPId;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.NextId;
import org.greatlogic.glgwt.server.GLServerUtil;
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLSQL;
import com.greatlogic.glbase.gldb.GLSchemaLoader;
import com.greatlogic.glbase.gldb.GLSchemaTable;
import com.greatlogic.glbase.gldb.IGLDBEnums.EGLDBException;
import com.greatlogic.glbase.gllib.GLConfig;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.GLUtil;

final class DBUUtil {
//--------------------------------------------------------------------------------------------------
private static StringBuilder _resultSB;
//--------------------------------------------------------------------------------------------------
static {
  _resultSB = new StringBuilder(1000);
}
//--------------------------------------------------------------------------------------------------
public static String addNextIds() throws GLDBException {
  String result = "";
  for (final EFAPId fapId : EFAPId.values()) {
    boolean nextIdExists;
    GLSQL sql = GLSQL.select();
    sql.from(EFAPTable.NextId.name());
    sql.whereAnd(0, NextId.NextId.name() + "=" + fapId.getNextId(), 0);
    sql.open(true, false);
    try {
      nextIdExists = sql.next(false);
    }
    finally {
      sql.close();
    }
    if (!nextIdExists) {
      sql = GLSQL.insert(EFAPTable.NextId.name(), null, false);
      sql.setValue(NextId.NextId.name(), fapId.getNextId());
      sql.setValue(NextId.NextIdValue.name(), 1000);
      sql.setValue(NextId.NextIdName.toString(), fapId.getName());
      sql.setValue(NextId.TableName.name(), fapId.getTable().name());
      sql.setValue(NextId.Version.name(), GLServerUtil.generateVersion());
      sql.execute();
      result += (result.isEmpty() ? "" : ",") + fapId.getName();
    }
  }
  if (!result.isEmpty()) {
    result = "Add sequence" + (result.contains(",") ? "s" : "") + ": " + result + ".";
    GLLog.toSystemOut(result, GLUtil.LineSeparator);
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
static void applySQLFile(final String sqlFilename) throws GLDBException {
  GLSchemaLoader schemaLoader;
  try {
    final String configFilename = GLConfig.getConfigFilename();
    final String sqlFilePath = configFilename.substring(0, configFilename.lastIndexOf('/') + 1) + //
                               sqlFilename;
    schemaLoader = GLSchemaLoader.createUsingFilename(sqlFilePath, "^ *[gG][oO] *$", "--");
    for (final GLSchemaTable table : schemaLoader.getTables()) {
      if (!table.getTableName().equalsIgnoreCase("DBUpdateNote")) {
        GLLog.infoSummary("Create table:" + table.getTableName());
        table.executeSQL(null, true, true);
      }
    }
  }
  catch (final IOException ioe) {
    throw new GLDBException(EGLDBException.ExecSQLError, //
                            "Error creating schema loader (" + ioe.getMessage() + ")");
  }
}
//--------------------------------------------------------------------------------------------------
static void clearResultSB() {
  _resultSB.setLength(0);
}
//--------------------------------------------------------------------------------------------------
public static String convertRevToNewRev(final String oldRev) {
  String result;
  if (oldRev.contains(".") || oldRev.length() < 8) {
    result = oldRev;
  }
  else if (oldRev.contains("_")) {
    result = oldRev.replace('_', '.');
  }
  else {
    result = oldRev.substring(0, 1) + "." + oldRev.substring(1, 3) + ".";
    result += oldRev.substring(3, 5) + "." + oldRev.substring(5);
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
static void createTablesFromSQL(final String sql) throws GLDBException {
  try {
    final GLSchemaLoader schemaLoader = GLSchemaLoader.createUsingString(sql, "go", "--");
    for (final GLSchemaTable table : schemaLoader.getTables()) {
      table.executeSQL(null, true, true);
    }
  }
  catch (final IOException ioe) {
    throw new GLDBException(EGLDBException.ClassCreatorError, "Error loading schema", ioe);
  }
  try {
    addNextIds();
  }
  catch (final Exception e) {
    GLLog.major("EFAPId.addNextIds() failed", e);
    throw new GLDBException(EGLDBException.ExecSQLError);
  }
}
//--------------------------------------------------------------------------------------------------
static String getResponse(final String prompt) {
  String result = "";
  GLLog.toSystemOut(prompt, null);
  try {
    boolean inputComplete = false;
    do {
      final int readResponse = System.in.read();
      final char c = (char)readResponse;
      if (c == '\r' || c == '\n') {
        inputComplete = result.length() > 0;
      }
      else {
        result += c;
      }
    } while (!inputComplete);
  }
  catch (final IOException ioe) {
    result = "";
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
static void insertDBUpdateNote(final String dbRevNumber, final String devDate,
                               final String devTime, final String additionalUpdateDesc)
  throws GLDBException {
  if (_resultSB.length() > 0) {
    _resultSB.insert(0, " =====> ");
  }
  String dbUpdateDesc = additionalUpdateDesc + _resultSB;
  dbUpdateDesc = dbUpdateDesc.length() > 2000 ? dbUpdateDesc.substring(0, 2000) : dbUpdateDesc;
  final GLSQL dbUpdateNoteSQL = GLSQL.insert(EFAPTable.DBUpdateNote.name(), false);
  dbUpdateNoteSQL.setValue(DBUpdateNote.AppliedDateTime.name(), GLUtil.currentTimeYYYYMMDDHHMMSS());
  dbUpdateNoteSQL.setValue(DBUpdateNote.DBRevNumber.name(), DBUUtil.convertRevToNewRev(dbRevNumber));
  dbUpdateNoteSQL.setValue(DBUpdateNote.DBUpdateDesc.name(), dbUpdateDesc);
  dbUpdateNoteSQL.setValue(DBUpdateNote.DBUpdateNoteId.name(),
                           GLServerUtil.getNextIdValue(EFAPTable.DBUpdateNote.name(), 1));
  dbUpdateNoteSQL.setValue(DBUpdateNote.DevDateTime.name(), devDate + devTime);
  dbUpdateNoteSQL.setValue(DBUpdateNote.Version.name(), GLServerUtil.generateVersion());
  dbUpdateNoteSQL.execute();
  _resultSB.setLength(0);
}
//--------------------------------------------------------------------------------------------------
}