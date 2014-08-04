package org.fosterapet.login;

import java.util.Set;
import org.fosterapet.server.IDBEnums.EFAPTable;
import org.fosterapet.server.IServerEnums.EFAPConfigAttribute;
import org.fosterapet.server.IServerEnums.ESessionAttribute;
import org.fosterapet.server.model.dto.Person;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLDBType;
import com.greatlogic.glbase.gldb.GLDBUtil;
import com.greatlogic.glbase.gldb.GLDataSource;
import com.greatlogic.glbase.gllib.GLConfig;
import com.greatlogic.glbase.gllib.GLUtil;
import com.greatlogic.glbase.glxml.GLXMLElement;

public final class FAPUtil {
//--------------------------------------------------------------------------------------------------
private static GLDBType      _dbType;
private static StringBuilder _tempSB;
private static String        _testLoginName;
//--------------------------------------------------------------------------------------------------
public static Person getRFLoginPerson() {
  return (Person)RequestFactoryServlet.getThreadLocalRequest().getSession().getAttribute(ESessionAttribute.LoginPerson.name());
} // getRFLoginPerson()
//--------------------------------------------------------------------------------------------------
public static String getNextVersion(final String oldVersion) {
  // oldVersion could be null
  return GLUtil.currentTimeYYYYMMDD_HH_MM_SS_SSS();
} // getNextVersion()
//--------------------------------------------------------------------------------------------------
public static String getTestLoginName() {
  return _testLoginName;
} // getTestLoginName()
//--------------------------------------------------------------------------------------------------
static void initialize() {
  _dbType = GLDataSource.getDefaultDataSource().getDBType();
  _tempSB = new StringBuilder(1000);
  final GLXMLElement configElement = GLConfig.getTopConfigElement();
  _testLoginName = configElement.attributeAsString(EFAPConfigAttribute.TestLoginName);
} // initialize()
//--------------------------------------------------------------------------------------------------
/**
 * Sets the ArchiveDate column to the current date and time.
 * @param table The table that contains the row to be updated.
 * @param id The Id of the row to be updated.
 */
public static void setArchiveDate(final EFAPTable table, final int id) throws GLDBException {
  synchronized (_tempSB) {
    _tempSB.setLength(0);
    _tempSB.append("update ").append(table.name());
    _tempSB.append("   set ArchiveDate = ").append(_dbType.formatDateForSQL(GLUtil.currentTimeYYYYMMDDHHMMSS(),
                                                                            true)).append(",");
    _tempSB.append("       Version = '").append(FAPUtil.getNextVersion(null)).append("'");
    _tempSB.append(" where ").append(table.name()).append("Id = ").append(id);
    GLDBUtil.execSQL(_tempSB);
  }
} // setArchiveDate()
//--------------------------------------------------------------------------------------------------
/**
 * Sets the ArchiveDate columns for a set of entity ids to the current date and time.
 * @param table The table that contains the rows to be updated.
 * @param idSet The list of entity ids that are to be updated.
 */
public static void setArchiveDatesUsingIdSet(final EFAPTable table, final Set<Integer> idSet)
  throws GLDBException {
  synchronized (_tempSB) {
    _tempSB.setLength(0);
    for (final int id : idSet) {
      _tempSB.append(_tempSB.length() == 0 ? "" : ",").append(id);
    }
    final String ids = _tempSB.toString();
    _tempSB.setLength(0);
    _tempSB.append("update ").append(table.name());
    _tempSB.append("   set ArchiveDate = ").append(_dbType.formatDateForSQL(GLUtil.currentTimeYYYYMMDDHHMMSS(),
                                                                            true)).append(",");
    _tempSB.append("       Version = '").append(FAPUtil.getNextVersion(null)).append("'");
    _tempSB.append(" where ").append(table.name()).append("Id in (").append(ids).append(")");
    GLDBUtil.execSQL(_tempSB);
  }
} // setArchiveDatesUsingIdSet()
//--------------------------------------------------------------------------------------------------
private FAPUtil() {
  // prevent instantiation
} // FAPUtil()
//--------------------------------------------------------------------------------------------------
}