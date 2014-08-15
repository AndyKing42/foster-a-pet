package org.fosterapet.server;
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
import org.fosterapet.shared.FAPLoginResponse;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.Person;
import org.fosterapet.shared.IFAPRemoteService;
import org.greatlogic.glgwt.server.GLLogin;
import org.greatlogic.glgwt.server.GLRemoteServiceServlet;
import org.greatlogic.glgwt.shared.GLChangePasswordResponse;
import org.greatlogic.glgwt.shared.GLLoginResponse;
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLSQL;
import com.greatlogic.glbase.gllib.GLLog;

@SuppressWarnings("serial")
public class IFAPRemoteServiceServlet extends GLRemoteServiceServlet implements IFAPRemoteService {
//--------------------------------------------------------------------------------------------------
@Override
public GLChangePasswordResponse changePassword(final int personId, final String oldPassword,
                                               final String newPassword) {
  final GLChangePasswordResponse result = new GLChangePasswordResponse();
  try {
    final FAPLogin login;
    final GLSQL personSQL = GLSQL.select();
    personSQL.from(EFAPTable.Person.name());
    personSQL.whereAnd(0, Person.PersonId + "=" + personId, 0);
    personSQL.open();
    try {
      if (!personSQL.next()) {
        result.setFailureReason("Invalid persion id (" + personId + ")");
        return result;
      }
      String sessionToken = null;
      if (personSQL.asString(Person.PasswordHash.name()).isEmpty()) {
        sessionToken = getSessionId();
        GLLogin.updateSessionToken(personId, 0, sessionToken);
      }
      login = createLogin();
      login.setSessionIdAndToken(getSessionId(), sessionToken);
      login.setLoginNameAndPassword(personSQL.asString(Person.LoginName.name()), oldPassword);
      login.login();
      if (!login.getSucceeded()) {
        result.setFailureReason("Invalid 'old' password");
        return result;
      }
    }
    finally {
      personSQL.close();
    }
    login.setNewPassword(newPassword);
    result.setResultValues(login.getSucceeded(), login.getSessionToken());
  }
  catch (final GLDBException e) {
    GLLog.minor("Login failed for person Id:" + personId);
    result.setFailureReason("Change password failed (" + e.getMessage() + ")");
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
@Override
protected FAPLogin createLogin() {
  return new FAPLogin();
}
//--------------------------------------------------------------------------------------------------
@Override
protected GLLoginResponse createLoginResponse() {
  return new FAPLoginResponse();
}
//--------------------------------------------------------------------------------------------------
@Override
public String loadTestData(final String connectionInfo, final String testDataOptionString) {
  try {
    DBTestData.processRequest(testDataOptionString);
  }
  catch (final Exception e) {
    GLLog.major("Error loading test data", e);
  }
  return "";
}
//--------------------------------------------------------------------------------------------------
}