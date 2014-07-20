package org.fosterapet.databaseupdate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.fosterapet.databaseupdate.IDatabaseUpdateEnums.EDBUConfigAD;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLSQL;
import com.greatlogic.glbase.gllib.GLConfig;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.GLUtil;
import com.greatlogic.glbase.gllib.IGLLibEnums.EGLLogLevel;
import com.greatlogic.glbase.glreflect.GLReflectUtil;

class DatabaseUpdater {
//--------------------------------------------------------------------------------------------------
public DatabaseUpdater() throws GLDBException {
  ensureDBUpdateNoteExists();
  final Map<String, Method> allMethodsMap = getAllMethodsMap();
  final Set<String> alreadyAppliedRevNumbersSet = getAlreadyAppliedRevNumbersSet();
  final TreeMap<String, Method> unappliedMethodsMap = getUnappliedMethodsMap(allMethodsMap,
                                                                             alreadyAppliedRevNumbersSet);
  if (unappliedMethodsMap.size() == 0) {
    GLLog.toSystemOut("The database is up to date", GLUtil.LineSeparator);
  }
  else {
    final String maxDBRevNumberToBeApplied = getMaxDBRevNumberToBeApplied(unappliedMethodsMap);
    applyRevs(unappliedMethodsMap, maxDBRevNumberToBeApplied);
  }
  GLUtil.sleepAndExit(200, false);
} // DatabaseUpdater()
//--------------------------------------------------------------------------------------------------
private boolean applyRev(final String revNumber, final Method method) {
  boolean result;
  try {
    GLLog.toSystemOut("=====> Applying update:" + revNumber, GLUtil.LineSeparator);
    final String updateNote = (String)method.invoke(null, revNumber, true);
    GLLog.toSystemOut("=====> Applied update:" + revNumber, GLUtil.LineSeparator);
    GLLog.minor("=====> Applied update:" + revNumber + " (" + updateNote + ")");
    result = true;
  }
  catch (final Exception e) {
    Throwable throwable = e;
    if (throwable instanceof InvocationTargetException) {
      throwable = ((InvocationTargetException)e).getTargetException();
    }
    GLLog.toErrorOut("Error executing revision " + revNumber + " (" + throwable.getMessage() + ")",
                     GLUtil.LineSeparator);
    result = false;
  }
  return result;
} // applyRev()
//--------------------------------------------------------------------------------------------------
private void applyRevs(final Map<String, Method> unappliedMethodsMap,
                       final String maxDBRevNumberToBeApplied) throws GLDBException {
  String maxDBRevNumberApplied = "";
  for (final Map.Entry<String, Method> unappliedMethodsEntry : unappliedMethodsMap.entrySet()) {
    if (unappliedMethodsEntry.getKey().compareTo(maxDBRevNumberToBeApplied) > 0) {
      break;
    }
    if (!applyRev(unappliedMethodsEntry.getKey(), unappliedMethodsEntry.getValue())) {
      break;
    }
    maxDBRevNumberApplied = unappliedMethodsEntry.getKey();
    DBUUtil_old.clearResultSB();
  }
  if (!maxDBRevNumberApplied.isEmpty()) {
    DBUUtil_old.addNextIds();
  }
} // applyRevs()
//--------------------------------------------------------------------------------------------------
private void ensureDBUpdateNoteExists() throws GLDBException {
  try {
    final EGLLogLevel saveLogLevel = GLLog.setThreadLogLevel(EGLLogLevel.Critical);
    try {
      final GLSQL dbUpdateNoteSQL = GLSQL.select();
      dbUpdateNoteSQL.from(EFAPTable.DBUpdateNote);
      dbUpdateNoteSQL.open();
    }
    finally {
      GLLog.setThreadLogLevel(saveLogLevel);
    }
  }
  catch (final GLDBException dbe) {
    if (dbe.getMessage().contains("doesn't exist")) {
      DatabaseRevs_0_00.rev_0_00_000("0.00.000", true);
    }
  }
} // ensureDBUpdateNoteExists()
//--------------------------------------------------------------------------------------------------
/**
 * Returns a map containing all database revision methods (i.e., all methods that start with "rev_"
 * from classes that have a name starting with "DatabaseRevs_").
 */
private Map<String, Method> getAllMethodsMap() {
  final Map<String, Method> result = Maps.newTreeMap();
  final String level2Tag = " abcdefghijklmnopqrstuvwxyz";
  for (int major = 0; major < 10; ++major) {
    for (int minor = 0; minor < 10; ++minor) {
      for (int level2TagIndex = 0; level2TagIndex < level2Tag.length(); ++level2TagIndex) {
        final String className = "DatabaseRevs_" + major + "_" + (minor < 10 ? "0" : "") + minor +
                                 level2Tag.substring(level2TagIndex, level2TagIndex + 1).trim();
        Class<? extends Object> databaseRevClass;
        try {
          databaseRevClass = GLReflectUtil.getClass(GLUtil.getPackageName(), className);
        }
        catch (final Exception e) {
          databaseRevClass = null;
        }
        if (databaseRevClass != null) {
          final Method[] methods = databaseRevClass.getDeclaredMethods();
          for (final Method method : methods) {
            final String methodName = method.getName();
            if (methodName.startsWith("rev_")) {
              result.put(method.getName().substring(4).replace('_', '.'), method);
            }
          }
        }
      }
    }
  }
  return result;
} // getAllMethodsMap()
//--------------------------------------------------------------------------------------------------
private Set<String> getAlreadyAppliedRevNumbersSet() throws GLDBException {
  final Set<String> result = Sets.newTreeSet();
  if (GLConfig.getTopConfigElement().attributeAsBoolean(EDBUConfigAD.RecreateAllTables)) {
    return result;
  }
  final EGLLogLevel saveLogLevel = GLLog.setThreadLogLevel(EGLLogLevel.Minor);
  try {
    final GLSQL dbUpdateNoteSQL = GLSQL.select();
    dbUpdateNoteSQL.from(EFAPTable.DBUpdateNote);
    dbUpdateNoteSQL.open();
    try {
      while (dbUpdateNoteSQL.next()) {
        final String dbRevNumber = dbUpdateNoteSQL.asString(EDBUpdateNote.DBRevNumber);
        if (dbRevNumber.contains(".")) {
          result.add(dbUpdateNoteSQL.asString(EDBUpdateNote.DBRevNumber));
        }
      }
    }
    finally {
      dbUpdateNoteSQL.close();
    }
  }
  finally {
    GLLog.setThreadLogLevel(saveLogLevel);
  }
  return result;
} // getAlreadyAppliedRevNumbersSet()
//--------------------------------------------------------------------------------------------------
private String getMaxDBRevNumberToBeApplied(final TreeMap<String, Method> unappliedMethodsMap) {
  String result = "";
  logMethodsToBeApplied(unappliedMethodsMap);
  boolean validResponse = false;
  do {
    result = DBUUtil_old.getResponse("Maximum database revision to be applied "
                                 + "(revision number or 'a' to apply all)? ");
    if (result.equalsIgnoreCase("a")) {
      result = unappliedMethodsMap.lastKey();
      validResponse = true;
    }
    else if (!result.isEmpty()) {
      for (final String dbRevNumber : unappliedMethodsMap.keySet()) {
        validResponse = result.equalsIgnoreCase(dbRevNumber);
        if (validResponse) {
          break;
        }
      }
    }
    if (!validResponse) {
      GLLog.toSystemOut("Please enter a valid revision number or 'a' for all revisions",
                        GLUtil.LineSeparator);
    }
  } while (!validResponse);
  return result;
} // getMaxDBRevNumberToBeApplied()
//--------------------------------------------------------------------------------------------------
private TreeMap<String, Method> getUnappliedMethodsMap(final Map<String, Method> allMethodsMap,
                                                       final Set<String> alreadyAppliedRevNumbersSet) {
  final TreeMap<String, Method> result = Maps.newTreeMap();
  for (final Map.Entry<String, Method> allMethodsEntry : allMethodsMap.entrySet()) {
    if (!alreadyAppliedRevNumbersSet.contains(allMethodsEntry.getKey())) {
      result.put(allMethodsEntry.getKey(), allMethodsEntry.getValue());
    }
  }
  return result;
} // getUnappliedMethodsMap()
//--------------------------------------------------------------------------------------------------
private void logMethodsToBeApplied(final Map<String, Method> unappliedMethodsMap) {
  for (final Map.Entry<String, Method> unappliedMethodsEntry : unappliedMethodsMap.entrySet()) {
    final String revNumber = unappliedMethodsEntry.getKey();
    final Method method = unappliedMethodsEntry.getValue();
    try {
      final String updateDesc = (String)method.invoke(null, revNumber, false);
      GLLog.toSystemOut("******* Revision " + revNumber + ":", GLUtil.LineSeparator);
      GLLog.toSystemOut(updateDesc, GLUtil.LineSeparator);
      GLLog.toSystemOut("", GLUtil.LineSeparator);
    }
    catch (final Exception e) {
      Throwable throwable = e;
      if (throwable instanceof InvocationTargetException) {
        throwable = ((InvocationTargetException)e).getTargetException();
      }
      GLLog.toErrorOut("Error executing revision " + revNumber + " (" + throwable.getMessage() +
                       ")", GLUtil.LineSeparator);
    }
  }
  GLLog.toSystemOut("==================================================", GLUtil.LineSeparator);
} // logMethodsToBeApplied()
//--------------------------------------------------------------------------------------------------
}