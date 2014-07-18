package org.fosterapet.databaseupdate;

import java.io.IOException;
import org.fosterapet.databaseupdate.IDatabaseUpdateEnums.EDBUConfigAD;
import org.fosterapet.shared.IDBEnums;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLSQL;
import com.greatlogic.glbase.gldb.GLSchemaLoader;
import com.greatlogic.glbase.gldb.GLSchemaTable;
import com.greatlogic.glbase.gldb.IGLDBEnums.EGLDBException;
import com.greatlogic.glbase.gllib.GLConfig;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.GLUtil;
import com.greatlogic.glbase.gllib.IGLLibEnums.EGLLogLevel;

final class DBUUtil {
//--------------------------------------------------------------------------------------------------
private static final String  _dbEnumsClassName;
private static final String  _projectPackageName;
private static final String  _projectPackagePath;

private static StringBuilder _resultSB;
//--------------------------------------------------------------------------------------------------
static {
  _dbEnumsClassName = IDBEnums.class.getName();
  _projectPackageName = GLConfig.getTopConfigElement().attributeAsString(EDBUConfigAD.ProjectPackageName);
  _projectPackagePath = GLConfig.getTopConfigElement().attributeAsString(EDBUConfigAD.ProjectPackagePath);
  _resultSB = new StringBuilder(1000);
} // static initialization
//--------------------------------------------------------------------------------------------------
/**
 * Adds all database NextIds that do not already exist.
 */
static void addNextIds() throws GLDBException {
  ENextIdDef.addNextIds(_resultSB);
} // addNextIds()
//--------------------------------------------------------------------------------------------------
static void applySQLFile(final String sqlFilename) throws GLDBException {
  GLSchemaLoader schemaLoader;
  try {
    final String configFilename = GLConfig.getConfigFilename();
    final String sqlFilePath = configFilename.substring(0, configFilename.lastIndexOf('/') + 1) +
                               sqlFilename;
    schemaLoader = GLSchemaLoader.createUsingFilename(sqlFilePath, "^ *[gG][oO] *$", "--");
    for (final GLSchemaTable table : schemaLoader.getTables()) {
      if (!table.getTableName().equalsIgnoreCase("DBUpdateNote")) {
        GLLog.infoSummary("Create table:" + table.getTableName());
        table.executeSQL(null, true, true);
      }
    }
    if (!_projectPackageName.isEmpty() && !_projectPackagePath.isEmpty()) {
      outputEnumEntries(schemaLoader);
      new GWTRFClassGenerator(schemaLoader, _dbEnumsClassName, _projectPackageName,
                              _projectPackagePath);
    }
  }
  catch (final IOException ioe) {
    throw new GLDBException(EGLDBException.ExecSQLError, "Error creating schema loader (" +
                                                         ioe.getMessage() + ")");
  }
} // applySQLFile()
//--------------------------------------------------------------------------------------------------
static void clearResultSB() {
  _resultSB.setLength(0);
} // clearResultSB()
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
} // getResponse()
//--------------------------------------------------------------------------------------------------
static void insertDBUpdateNote(final String dbRevNumber, final String devDate,
                               final String devTime, final String additionalUpdateDesc)
  throws GLDBException {
  if (_resultSB.length() > 0) {
    _resultSB.insert(0, " =====> ");
  }
  String dbUpdateDesc = additionalUpdateDesc + _resultSB;
  dbUpdateDesc = dbUpdateDesc.length() > 2000 ? dbUpdateDesc.substring(0, 2000) : dbUpdateDesc;
  long nextValue;
  final EGLLogLevel saveLogLevel = GLLog.setThreadLogLevel(EGLLogLevel.Critical);
  try {
    nextValue = ENextIdDef.DBUpdateNoteId.getNextValue(1);
  }
  catch (final Exception e) {
    nextValue = 0;
  }
  finally {
    GLLog.setThreadLogLevel(saveLogLevel);
  }
  final GLSQL dbUpdateNoteSQL = GLSQL.insert(EFAPTable.DBUpdateNote, false);
  dbUpdateNoteSQL.setValue(EDBUpdateNote.AppliedDateTime, GLUtil.currentTimeYYYYMMDDHHMMSS());
  dbUpdateNoteSQL.setValue(EDBUpdateNote.DBRevNumber, dbRevNumber);
  dbUpdateNoteSQL.setValue(EDBUpdateNote.DBUpdateDesc, dbUpdateDesc);
  dbUpdateNoteSQL.setValue(EDBUpdateNote.DBUpdateNoteId, nextValue);
  dbUpdateNoteSQL.setValue(EDBUpdateNote.DevDateTime, devDate + devTime);
  dbUpdateNoteSQL.setValue(EDBUpdateNote.Version, 1);
  dbUpdateNoteSQL.execute();
  _resultSB.setLength(0);
} // insertDBUpdateNote()
//--------------------------------------------------------------------------------------------------
static void outputEnumEntries(final GLSchemaLoader schemaLoader) {
  int nextId = 1;
  for (final GLSchemaTable table : schemaLoader.getTables()) {
    GLLog.toSystemOut(table.getTableName() + "Id(" + nextId + ",EFAPTable." + table.getTableName() +
                      "),", GLUtil.LineSeparator);
    ++nextId;
  }
  GLLog.toSystemOut(schemaLoader.getTableEnum("E"), GLUtil.LineSeparator);
  for (final GLSchemaTable table : schemaLoader.getTables()) {
    GLLog.toSystemOut(table.getEnum("E"), GLUtil.LineSeparator);
  }
} // outputEnumEntries()
//--------------------------------------------------------------------------------------------------
private DBUUtil() {
  // prevent instantiation
} // DBUUtil()
//--------------------------------------------------------------------------------------------------
}