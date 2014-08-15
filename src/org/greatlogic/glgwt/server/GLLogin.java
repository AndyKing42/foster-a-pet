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
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLDBUtil;
import com.greatlogic.glbase.gldb.GLSQL;
import com.greatlogic.glbase.gldb.IGLColumn;
import com.greatlogic.glbase.gldb.IGLDBEnums.EGLDBOp;
import com.greatlogic.glbase.gldb.IGLTable;
import com.greatlogic.glbase.gllib.BCrypt;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.GLUtil;

public abstract class GLLogin {
//--------------------------------------------------------------------------------------------------
private static final long TenDays = 10 * GLUtil.SecondsInADay;

private String            _loginName;
private String            _password;
protected String          _passwordHash;
private String            _sessionToken;
private String            _sessionTokenFromClient;
private int               _sessionTokenId;
private boolean           _succeeded;
protected int             _userId;
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
SessionToken,
SessionTokenId,
UserId,
Version
}
//==================================================================================================
public static void updateSessionToken(final int userId, final int sessionTokenId,
                                      final String sessionToken) throws GLDBException {
  final GLSQL sessionTokenSQL;
  if (sessionTokenId == 0) {
    sessionTokenSQL = GLSQL.insert(ELoginTable.SessionToken, false);
    sessionTokenSQL.setValue(SessionToken.UserId, userId);
    sessionTokenSQL.setValue(SessionToken.SessionTokenId,
                             GLServerUtil.getNextIdValue(ELoginTable.SessionToken + "Id", 1));
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
protected void deleteOtherSessionTokens() {
  try {
    final GLSQL sessionTokenSQL = GLSQL.delete(ELoginTable.SessionToken);
    sessionTokenSQL.whereAnd(SessionToken.SessionToken, EGLDBOp.Equals, _sessionToken);
    sessionTokenSQL.whereAnd(SessionToken.SessionTokenId, EGLDBOp.NotEqual, _sessionTokenId);
    sessionTokenSQL.execute();
  }
  catch (final GLDBException dbe) {
    GLLog.major("deleteOtherSessionTokens() failed", dbe);
  }
}
//--------------------------------------------------------------------------------------------------
public String getSessionToken() {
  return _sessionToken;
}
//--------------------------------------------------------------------------------------------------
public boolean getSucceeded() {
  return _succeeded;
}
//--------------------------------------------------------------------------------------------------
public void login() {
  Exception savedException = null;
  try {
    if (!StringUtils.isEmpty(_sessionTokenFromClient)) {
      _succeeded = loginUsingSessionToken(_sessionTokenFromClient);
    }
    if (!_succeeded) {
      _succeeded = loginUsingNameAndPassword(_loginName, _password);
    }
    if (_succeeded) {
      GLLogin.updateSessionToken(_userId, _sessionTokenId, _sessionToken);
    }
  }
  catch (final Exception e) {
    savedException = e;
  }
  if (_succeeded) {
    GLLog.infoSummary("Login succeeded for userId:" + _userId);
  }
  else {
    GLLog.infoSummary("Login failed for login name:" + _loginName, savedException);
  }
}
//--------------------------------------------------------------------------------------------------
private boolean loginUsingNameAndPassword(final String loginName, final String password) {
  if (loginName.isEmpty() || password.isEmpty()) {
    return false;
  }
  setUserIdAndPasswordHash(loginName, password);
  if (_passwordHash.isEmpty()) {
    final String newPasswordHash = BCrypt.hashpw(password, BCrypt.gensalt());
    return setNewPassword(newPasswordHash);
  }
  if (BCrypt.checkpw(password, _passwordHash)) {
    deleteOtherSessionTokens();
    return true;
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
      _userId = sessionTokenSQL.asInt(SessionToken.UserId);
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
public void setLoginNameAndPassword(final String loginName, final String password) {
  _loginName = loginName;
  _password = password;
}
//--------------------------------------------------------------------------------------------------
protected abstract boolean setNewPassword(final String newPassword);
//--------------------------------------------------------------------------------------------------
public void setSessionIdAndToken(final String sessionId, final String sessionTokenFromClient) {
  _sessionToken = sessionId;
  _sessionTokenFromClient = sessionTokenFromClient;
}
//--------------------------------------------------------------------------------------------------
protected abstract void setUserIdAndPasswordHash(final String loginName, final String password);
//--------------------------------------------------------------------------------------------------
}