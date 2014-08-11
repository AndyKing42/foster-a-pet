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
import java.util.ArrayList;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.Person;
import org.greatlogic.glgwt.shared.GLChangePasswordResponse;
import org.greatlogic.glgwt.shared.GLLoginResponse;
import org.greatlogic.glgwt.shared.IGLRemoteService;
import org.greatlogic.glgwt.shared.IGLTable;
import org.greatlogic.glgwt.shared.requestresponse.GLApplyDBChangesServiceRequest;
import org.greatlogic.glgwt.shared.requestresponse.GLApplyDBChangesServiceResponse;
import org.greatlogic.glgwt.shared.requestresponse.GLGetNextIdServiceRequest;
import org.greatlogic.glgwt.shared.requestresponse.GLGetNextIdServiceResponse;
import org.greatlogic.glgwt.shared.requestresponse.GLGetTableMetadataServiceRequest;
import org.greatlogic.glgwt.shared.requestresponse.GLGetTableMetadataServiceResponse;
import org.greatlogic.glgwt.shared.requestresponse.GLSelectServiceRequest;
import org.greatlogic.glgwt.shared.requestresponse.GLSelectServiceResponse;
import org.greatlogic.glgwt.shared.requestresponse.GLServiceRequest;
import org.greatlogic.glgwt.shared.requestresponse.GLServiceResponse;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.greatlogic.glbase.gldb.GLColumnMetadata;
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLDataSource;
import com.greatlogic.glbase.gldb.GLResultSetMetadata;
import com.greatlogic.glbase.gldb.GLSQL;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.IGLLibEnums.EGLLogLevel;

@SuppressWarnings("serial")
public class GLRemoteServiceServlet extends RemoteServiceServlet implements IGLRemoteService {
//--------------------------------------------------------------------------------------------------
@Override
public GLChangePasswordResponse changePassword(final int personId, final String oldPassword,
                                               final String newPassword) {
  final GLChangePasswordResponse result = new GLChangePasswordResponse();
  try {
    final GLLogin login;
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
      login = new GLLogin(getSessionId(), personSQL.asString(Person.LoginName.name()), //
                          oldPassword, sessionToken);
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
String getSessionId() {
  return getThreadLocalRequest().getSession().getId();
}
//--------------------------------------------------------------------------------------------------
// todo: this should return an array of GLMetadata (or something similar), not an array of String
private ArrayList<String> getTableMetadataList(final ArrayList<IGLTable> tableList) {
  for (final IGLTable table : tableList) {
    try {
      final GLResultSetMetadata tableMetadata;
      tableMetadata = GLDataSource.getDefaultDataSource().getTableMetadata(table.toString());
      for (final GLColumnMetadata columnMetadata : tableMetadata.getColumnMetadataList()) {
        columnMetadata.getName();
        columnMetadata.getColumnDataType();
        columnMetadata.getPrecision();
        columnMetadata.getScale();
        columnMetadata.getCanBeNull();
      }
    }
    catch (final GLDBException e) {
      GLLog.major("Request for metadata failed for table:" + table);
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
@Override
public GLLoginResponse login(final String loginName, final String password,
                             final String sessionTokenFromClient) {
  final GLLoginResponse result = new GLLoginResponse();
  final GLLogin login = new GLLogin(getSessionId(), loginName, password, sessionTokenFromClient);
  result.setResultValues(login.getSucceeded(), login.getSessionToken(), login.getPersonId());
  return result;
}
//--------------------------------------------------------------------------------------------------
@Override
public GLServiceResponse processRequest(final GLServiceRequest serviceRequest) {
  GLServiceResponse result = null;
  GLLog.debug("Request:" + serviceRequest);
  final GLLogin login = new GLLogin(getSessionId(), null, null, serviceRequest.getSessionToken());
  if (!login.getSucceeded()) {
    return null;
  }
  switch (serviceRequest.getRemoteServiceRequestType()) {
    case ApplyDBChanges: {
      final GLApplyDBChangesServiceRequest request = (GLApplyDBChangesServiceRequest)serviceRequest;
      GLDBStatement.applyDBChanges(request.getDeletedKeyValueMap(), request.getDBUpdateList());
      result = new GLApplyDBChangesServiceResponse();
      break;
    }
    case GetNextId: {
      final GLGetNextIdServiceRequest request = (GLGetNextIdServiceRequest)serviceRequest;
      final int nextId = GLServerUtil.getNextIdValue(request.getTable().toString(), //
                                                     request.getNumberOfValues());
      result = new GLGetNextIdServiceResponse(nextId);
      break;
    }
    case GetTableMetadata: {
      final GLGetTableMetadataServiceRequest request;
      request = (GLGetTableMetadataServiceRequest)serviceRequest;
      result = new GLGetTableMetadataServiceResponse(getTableMetadataList(request.getTableList()));
      break;
    }
    case Select: {
      final GLSelectServiceRequest request = (GLSelectServiceRequest)serviceRequest;
      result = new GLSelectServiceResponse(GLDBStatement.select(request.getXMLString()));
      break;
    }
  }
  if (result != null) {
    result.setSessionToken(getSessionId());
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
}