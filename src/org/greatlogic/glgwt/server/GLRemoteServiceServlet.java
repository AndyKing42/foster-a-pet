package org.greatlogic.glgwt.server;

import org.greatlogic.glgwt.shared.IGLRemoteService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.greatlogic.glbase.gldb.GLColumnMetadata;
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLDataSource;
import com.greatlogic.glbase.gldb.GLResultSetMetadata;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.IGLLibEnums.EGLLogLevel;

@SuppressWarnings("serial")
public class GLRemoteServiceServlet extends RemoteServiceServlet implements IGLRemoteService {
//--------------------------------------------------------------------------------------------------
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
@Override
public void applyDBChanges(final String dbChanges) {
  GLDBStatement.applyDBChanges(dbChanges);
}
//--------------------------------------------------------------------------------------------------
@Override
public int getNextId(final String tableName, final int numberOfValues) {
  return GLServerUtil.getNextIdValue(tableName, numberOfValues);
}
//--------------------------------------------------------------------------------------------------
/**
 * Returns metadata for the requested tables.
 * @param tableNames A comma-delimited list of table names.
 */
@Override
public String getTableMetadata(final String tableNames) {
  for (final String tableName : tableNames.split(",")) {
    try {
      final GLResultSetMetadata tableMetadata;
      tableMetadata = GLDataSource.getDefaultDataSource().getTableMetadata(tableName);
      for (final GLColumnMetadata columnMetadata : tableMetadata.getColumnMetadataList()) {
        columnMetadata.getName();
        columnMetadata.getColumnDataType();
        columnMetadata.getPrecision();
        columnMetadata.getScale();
        columnMetadata.getCanBeNull();
      }
    }
    catch (final GLDBException e) {
      GLLog.major("Request for metadata failed for table:" + tableName);
    }
  }
  return null;
}
//--------------------------------------------------------------------------------------------------
@Override
public void log(final int priority, final String location, final String message) {
  GLLog.log(EGLLogLevel.lookupUsingPriority(priority), location + "=>" + message);
}
//--------------------------------------------------------------------------------------------------
/**
 * Attempts to log in using the supplied login name and password.
 * @param loginName The login name that will be used for the login attempt.
 * @param password The password that will be used for the login attempt (this is the plain text
 * password, not the encrypted hash value).
 * @return The id of the Person row, or zero if the login request fails.
 */
@Override
public Integer login(final String loginName, final String password) {
  // find the user using the loginName and password
  // if the user isn't found {
  //    GLLog.infoSummary("Login failed for login name:" + loginName);
  //    return 0;
  // }
  //  GLLog.infoSummary("Login succeeded for login name:" + user.getLoginName());
  //  getThreadLocalRequest().getSession().setAttribute(ESessionAttribute.LoginUser.name(), user);
  //  return user.getUserId();
  return 0;
}
//--------------------------------------------------------------------------------------------------
@Override
public String select(final String xmlRequest) {
  return GLDBStatement.select(xmlRequest);
}
//--------------------------------------------------------------------------------------------------
}