package org.greatlogic.glgwt.server;

import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.NextId;
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLDBUtil;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.GLUtil;

public class GLServerUtil {
//--------------------------------------------------------------------------------------------------
public static String generateVersion() {
  return GLUtil.currentTimeYYYYMMDDHHMMSSmmm();
}
//--------------------------------------------------------------------------------------------------
public static int getNextIdValue(final String tableName, final int numberOfValues) {
  final String idName = tableName + "Id";
  try {
    return (int)GLDBUtil.getNextSequenceValue(idName, EFAPTable.NextId.name(), //
                                              NextId.NextIdValue.name(), //
                                              NextId.NextIdName + "='" + idName + "'", //
                                              numberOfValues);
  }
  catch (final GLDBException dbe) {
    GLLog.major("Error attempting to get the next value for id:" + idName, dbe);
    return -1;
  }
}
//--------------------------------------------------------------------------------------------------
}