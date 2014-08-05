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
import org.apache.commons.lang3.StringUtils;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.Person;
import org.greatlogic.glgwt.shared.IGLRemoteService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.greatlogic.glbase.gldb.GLColumnMetadata;
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLDBUtil;
import com.greatlogic.glbase.gldb.GLDataSource;
import com.greatlogic.glbase.gldb.GLResultSetMetadata;
import com.greatlogic.glbase.gldb.GLSQL;
import com.greatlogic.glbase.gldb.IGLColumn;
import com.greatlogic.glbase.gldb.IGLDBEnums.EGLDBOp;
import com.greatlogic.glbase.gldb.IGLTable;
import com.greatlogic.glbase.gllib.BCrypt;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.IGLLibEnums.EGLLogLevel;

@SuppressWarnings("serial")
public class GLRemoteServiceServlet extends RemoteServiceServlet implements IGLRemoteService {
//==================================================================================================
private enum ELoginTable implements IGLTable {
SessionToken(SessionToken.class);
private final Class<? extends Enum<?>> _columnEnumClass;
private ELoginTable(final Class<? extends Enum<?>> columnEnumClass) {
  _columnEnumClass = columnEnumClass;
  GLDBUtil.registerTable(this);
}
@Override
public String getAbbrev() {
  return _columnEnumClass.getSimpleName();
}
@Override
public Class<? extends Enum<?>> getColumnEnumClass() {
  return _columnEnumClass;
}
@Override
public String getDataSourceName() {
  return null;
}
}
private enum SessionToken implements IGLColumn {
PersonId,
SessionToken,
SessionTokenId
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
 * @return If the login attempt is successful then the session token will be returned; if the
 * attempt is unsuccessful then the return value will be an empty string.
 */
@Override
public String login(final String loginName, final String password, final String currentSessionToken) {
  boolean loginSucceeded = false;
  int sessionTokenId = 0;
  int personId;
  if (StringUtils.isEmpty(currentSessionToken)) {
    final GLSQL personSQL = GLSQL.select();
    personSQL.from(EFAPTable.Person.name());
    personSQL.whereAnd(0, Person.LoginName + "='" + loginName + "'", 0);
    if (personSQL.next() &&
        BCrypt.checkpw(password, personSQL.asString(Person.PasswordHash.name()))) {
      loginSucceeded = true;
      personId = personSQL.asInt(Person.PersonId.name());
    }
  }
  else {
    GLSQL sessionTokenSQL;
    try {
      sessionTokenSQL = GLSQL.select();
      sessionTokenSQL.from(ELoginTable.SessionToken);
      sessionTokenSQL.whereAnd(SessionToken.SessionToken, EGLDBOp.Equals, currentSessionToken);
      sessionTokenSQL.open();
      try {
        if (sessionTokenSQL.next()) {
          loginSucceeded = true;
          personId = sessionTokenSQL.asInt(SessionToken.PersonId);
          sessionTokenId = sessionTokenSQL.asInt(SessionToken.SessionTokenId);
          // todo: check for expiration
        }
      }
      finally {
        sessionTokenSQL.close();
      }
    }
    catch (final GLDBException e) {
      GLLog.major("Login attempt failed for user:" + loginName, e);
    }
  }
  String sessionToken;
  if (loginSucceeded) {
    sessionToken = getThreadLocalRequest().getSession().getId();
    final GLSQL sessionTokenSQL;
    if (sessionTokenId == 0) {
      sessionTokenSQL = GLSQL.insert(ELoginTable.SessionToken, false);
      sessionTokenSQL.setValue(SessionToken.PersonId, personId);
      sessionTokenSQL.setValue(SessionToken.SessionTokenId, next_id_value());
    }
    else {
      sessionTokenSQL = GLSQL.update(ELoginTable.SessionToken);
    }
    sessionTokenSQL.setValue(SessionToken.SessionToken, sessionToken);
    sessionTokenSQL.execute();
  }
  return loginSucceeded ? sessionToken + "~" + personId : "";
}
//--------------------------------------------------------------------------------------------------
@Override
public String select(final String xmlRequest) {
  return GLDBStatement.select(xmlRequest);
}
//--------------------------------------------------------------------------------------------------
}