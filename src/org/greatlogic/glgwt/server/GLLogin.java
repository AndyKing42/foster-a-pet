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
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLDBUtil;
import com.greatlogic.glbase.gldb.GLSQL;
import com.greatlogic.glbase.gldb.IGLColumn;
import com.greatlogic.glbase.gldb.IGLDBEnums.EGLDBOp;
import com.greatlogic.glbase.gldb.IGLTable;
import com.greatlogic.glbase.gllib.BCrypt;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.GLUtil;

class GLLogin {
//--------------------------------------------------------------------------------------------------
private static final long TenDays = 10 * GLUtil.SecondsInADay;

private int               _personId;
private final String      _sessionToken;
private int               _sessionTokenId;
private boolean           _succeeded;
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
ExpirationTime,
PersonId,
SessionToken,
SessionTokenId,
Version
}
//==================================================================================================
static void updateSessionToken(final int personId, final int sessionTokenId,
                               final String sessionToken) throws GLDBException {
  final GLSQL sessionTokenSQL;
  if (sessionTokenId == 0) {
    sessionTokenSQL = GLSQL.insert(ELoginTable.SessionToken, false);
    sessionTokenSQL.setValue(SessionToken.PersonId, personId);
    sessionTokenSQL.setValue(SessionToken.SessionTokenId,
                             GLServerUtil.getNextIdValue(ELoginTable.SessionToken.name(), 1));
  }
  else {
    sessionTokenSQL = GLSQL.update(ELoginTable.SessionToken);
    sessionTokenSQL.whereAnd(SessionToken.SessionTokenId, EGLDBOp.Equals, sessionTokenId);
  }
  sessionTokenSQL.setValue(SessionToken.ExpirationTime,
                           GLUtil.timeAddSeconds(GLUtil.currentTimeYYYYMMDDHHMMSS(), TenDays));
  sessionTokenSQL.setValue(SessionToken.SessionToken, sessionToken);
  sessionTokenSQL.setValue(SessionToken.Version, GLServerUtil.generateVersion());
  sessionTokenSQL.execute();
}
//--------------------------------------------------------------------------------------------------
GLLogin(final String sessionId, final String loginName, final String password,
        final String sessionTokenFromClient) {
  _sessionToken = sessionId;
  Exception savedException = null;
  try {
    if (!StringUtils.isEmpty(sessionTokenFromClient)) {
      _succeeded = loginUsingSessionToken(sessionTokenFromClient);
    }
    if (!_succeeded) {
      _succeeded = loginUsingNameAndPassword(loginName, password);
    }
    if (_succeeded) {
      GLLogin.updateSessionToken(_personId, _sessionTokenId, _sessionToken);
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
    if (personSQL.next()) {
      _personId = personSQL.asInt(Person.PersonId.name());
      final String passwordHash = personSQL.asString(Person.PasswordHash.name());
      if (passwordHash.isEmpty()) {
        setNewPassword(password);
        return true;
      }
      else if (BCrypt.checkpw(password, passwordHash)) {
        return true;
      }
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
      if (GLUtil.currentTimeYYYYMMDDHHMMSS()
                .compareTo(sessionTokenSQL.asString(SessionToken.ExpirationTime)) > 0) {
        return false;
      }
      return true;
    }
  }
  finally {
    sessionTokenSQL.close();
  }
  return false;
}
//--------------------------------------------------------------------------------------------------
void setNewPassword(final String newPassword) throws GLDBException {
  final GLSQL personSQL = GLSQL.update(EFAPTable.Person.name());
  personSQL.setValue(Person.PasswordHash.name(), BCrypt.hashpw(newPassword, BCrypt.gensalt()));
  personSQL.whereAnd(0, Person.PersonId + "=" + _personId, 0);
  personSQL.execute();
}
//--------------------------------------------------------------------------------------------------
}