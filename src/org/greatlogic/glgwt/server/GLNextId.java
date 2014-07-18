package org.greatlogic.glgwt.server;

import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLDBUtil;
import com.greatlogic.glbase.gldb.IGLColumn;
import com.greatlogic.glbase.gldb.IGLTable;
import com.greatlogic.glbase.gllib.GLLog;

public class GLNextId {
//==================================================================================================
public enum EGLIdTable implements IGLTable {
NextId;
@Override
public String getAbbrev() {
  return null;
}
@Override
public Class<? extends Enum<?>> getColumnEnumClass() {
  return null;
}
@Override
public String getDataSourceName() {
  return null;
}
}
//==================================================================================================
public enum NextId implements IGLColumn {
NextId,
NextIdName,
NextIdTable,
NextIdValue
}
//==================================================================================================
public static int getNextIdValue(final String tableName, final int numberOfValues) {
  final String idName = tableName + "Id";
  try {
    return (int)GLDBUtil.getNextSequenceValue(idName, EGLIdTable.NextId, NextId.NextIdValue,
                                              NextId.NextIdName + "='" + idName + "'",
                                              numberOfValues);
  }
  catch (final GLDBException dbe) {
    GLLog.major("Error attempting to get the next value for id:" + idName, dbe);
    return -1;
  }
}
//--------------------------------------------------------------------------------------------------
}