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
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.IGLLibEnums.EGLLogLevel;

@SuppressWarnings("serial")
public abstract class GLRemoteServiceServlet extends RemoteServiceServlet implements
                                                                         IGLRemoteService {
//--------------------------------------------------------------------------------------------------
protected abstract GLLogin createLogin();
//--------------------------------------------------------------------------------------------------
protected abstract GLLoginResponse createLoginResponse();
//--------------------------------------------------------------------------------------------------
protected String getSessionId() {
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
  final GLLoginResponse result = createLoginResponse();
  final GLLogin login = createLogin();
  result.setLogin(login);
  login.setSessionIdAndToken(getSessionId(), sessionTokenFromClient);
  login.setLoginNameAndPassword(loginName, password);
  login.login();
  result.setGLResultValues(login.getSucceeded(), login.getSessionToken());
  return result;
}
//--------------------------------------------------------------------------------------------------
@Override
public GLServiceResponse processRequest(final GLServiceRequest serviceRequest) {
  GLServiceResponse result = null;
  GLLog.debug("Request:" + serviceRequest);
  final GLLogin login = createLogin();
  login.setSessionIdAndToken(getSessionId(), serviceRequest.getSessionToken());
  login.login();
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
      final int nextId = GLServerUtil.getNextIdValue(request.getTable() + "Id", //
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