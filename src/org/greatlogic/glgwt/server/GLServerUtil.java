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
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLDBUtil;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.GLUtil;

public class GLServerUtil {
//--------------------------------------------------------------------------------------------------
private static String _nextIdNameColumnName;
private static String _nextIdTableName;
private static String _nextIdValueColumnName;
//--------------------------------------------------------------------------------------------------
public static String generateVersion() {
  return GLUtil.currentTimeYYYYMMDDHHMMSS();
}
//--------------------------------------------------------------------------------------------------
public static int getNextIdValue(final String idName, final int numberOfValues) {
  if (_nextIdTableName == null) {
    final String message = "GLServerUtil.initialize() must be called prior to using " //
                           + "getNextIdValue()";
    GLLog.major(message);
    throw new IllegalArgumentException(message);
  }
  try {
    return (int)GLDBUtil.getNextSequenceValue(idName, _nextIdTableName, _nextIdValueColumnName,
                                              _nextIdNameColumnName + "='" + idName + "'",
                                              numberOfValues);
  }
  catch (final GLDBException dbe) {
    GLLog.major("Error attempting to get the next value for id:" + idName, dbe);
    return -1;
  }
}
//--------------------------------------------------------------------------------------------------
public static void initialize(final String nextIdTableName, final String nextIdNameColumnName,
                              final String nextIdValueColumnName) {
  _nextIdTableName = nextIdTableName;
  _nextIdNameColumnName = nextIdNameColumnName;
  _nextIdValueColumnName = nextIdValueColumnName;
}
//--------------------------------------------------------------------------------------------------
}