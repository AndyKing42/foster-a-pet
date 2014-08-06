package org.greatlogic.glgwt.server;

import org.apache.commons.lang3.StringUtils;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.Person;
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLDBUtil;
import com.greatlogic.glbase.gldb.GLSQL;
import com.greatlogic.glbase.gldb.IGLColumn;
import com.greatlogic.glbase.gldb.IGLDBEnums.EGLDBOp;
import com.greatlogic.glbase.gldb.IGLTable;
import com.greatlogic.glbase.gllib.BCrypt;
import com.greatlogic.glbase.gllib.GLLog;

class GLLogin {
//--------------------------------------------------------------------------------------------------
private int                          _personId;
private final GLRemoteServiceServlet _remoteServiceServlet;
private String                       _sessionToken;
private int                          _sessionTokenId;
private boolean                      _succeeded;
//--------------------------------------------------------------------------------------------------
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
//--------------------------------------------------------------------------------------------------
GLLogin(final GLRemoteServiceServlet remoteServiceServlet, final String loginName,
        final String password, final String currentSessionToken) {
  _remoteServiceServlet = remoteServiceServlet;
  Exception savedException = null;
  try {
    if (!StringUtils.isEmpty(currentSessionToken)) {
      _succeeded = loginUsingSessionToken(currentSessionToken);
    }
    if (!_succeeded) {
      _succeeded = loginUsingNameAndPassword(loginName, password);
    }
    if (_succeeded) {
      updateSessionToken();
    }
  }
  catch (final Exception e) {
    savedException = e;
  }
  if (_succeeded) {
    GLLog.infoSummary("Login succeeded for personId:" + _personId);
  }
  else {
    GLLog.infoSummary("Login failed for login name:" + loginName, savedException);
  }
}
//--------------------------------------------------------------------------------------------------
int getPersonId() {
  return _personId;
}
//--------------------------------------------------------------------------------------------------
String getSessionToken() {
  return _sessionToken;
}
//--------------------------------------------------------------------------------------------------
boolean getSucceeded() {
  return _succeeded;
}
//--------------------------------------------------------------------------------------------------
private boolean loginUsingNameAndPassword(final String loginName, final String password)
  throws GLDBException {
  final GLSQL personSQL = GLSQL.select();
  personSQL.from(EFAPTable.Person.name());
  personSQL.whereAnd(0, Person.LoginName + "='" + loginName + "'", 0);
  personSQL.open();
  try {
    if (personSQL.next() &&
        BCrypt.checkpw(password, personSQL.asString(Person.PasswordHash.name()))) {
      _personId = personSQL.asInt(Person.PersonId.name());
      return true;
    }
  }
  finally {
    personSQL.close();
  }
  return false;
}
//--------------------------------------------------------------------------------------------------
private boolean loginUsingSessionToken(final String sessionToken) throws GLDBException {
  GLSQL sessionTokenSQL;
  sessionTokenSQL = GLSQL.select();
  sessionTokenSQL.from(ELoginTable.SessionToken);
  sessionTokenSQL.whereAnd(SessionToken.SessionToken, EGLDBOp.Equals, sessionToken);
  sessionTokenSQL.open();
  try {
    if (sessionTokenSQL.next()) {
      _personId = sessionTokenSQL.asInt(SessionToken.PersonId);
      _sessionTokenId = sessionTokenSQL.asInt(SessionToken.SessionTokenId);
      // todo: check for expiration
      return true;
    }
  }
  finally {
    sessionTokenSQL.close();
  }
  return false;
}
//--------------------------------------------------------------------------------------------------
private void updateSessionToken() throws GLDBException {
  _sessionToken = _remoteServiceServlet.getSessionId();
  final GLSQL sessionTokenSQL;
  if (_sessionTokenId == 0) {
    sessionTokenSQL = GLSQL.insert(ELoginTable.SessionToken, false);
    sessionTokenSQL.setValue(SessionToken.PersonId, _personId);
    sessionTokenSQL.setValue(SessionToken.SessionTokenId,
                             GLServerUtil.getNextIdValue(ELoginTable.SessionToken.name(), 1));
  }
  else {
    sessionTokenSQL = GLSQL.update(ELoginTable.SessionToken);
    sessionTokenSQL.whereAnd(SessionToken.SessionTokenId, EGLDBOp.Equals, _sessionTokenId);
  }
  sessionTokenSQL.setValue(SessionToken.SessionToken, _sessionToken);
  sessionTokenSQL.execute();
}
//--------------------------------------------------------------------------------------------------
}