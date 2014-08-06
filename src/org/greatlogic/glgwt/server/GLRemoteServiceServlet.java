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
import java.util.TreeMap;
import java.util.TreeSet;
import org.greatlogic.glgwt.client.db.GLDBUpdate;
import org.greatlogic.glgwt.shared.GLLoginResponse;
import org.greatlogic.glgwt.shared.GLRemoteServiceRequest;
import org.greatlogic.glgwt.shared.GLRemoteServiceResponse;
import org.greatlogic.glgwt.shared.IGLRemoteService;
import org.greatlogic.glgwt.shared.IGLTable;
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
private void applyDBChanges(final TreeMap<IGLTable, TreeSet<String>> deletedKeyValueMap,
                            final ArrayList<GLDBUpdate> dbUpdateList) {
  GLDBStatement.applyDBChanges(deletedKeyValueMap, dbUpdateList);
}
//--------------------------------------------------------------------------------------------------
private int getNextId(final String tableName, final int numberOfValues) {
  return GLServerUtil.getNextIdValue(tableName, numberOfValues);
}
//--------------------------------------------------------------------------------------------------
String getSessionId() {
  return getThreadLocalRequest().getSession().getId();
}
//--------------------------------------------------------------------------------------------------
// todo: this should return an array of GLMetadata (or something similar), not an array of String
private ArrayList<String> getTableMetadataList(final ArrayList<String> tableNameList) {
  for (final String tableName : tableNameList) {
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
@Override
public GLLoginResponse login(final String loginName, final String password,
                             final String currentSessionToken) {
  final GLLoginResponse result = new GLLoginResponse();
  final GLLogin login = new GLLogin(this, loginName, password, currentSessionToken);
  result.setResultValues(login.getSucceeded(), login.getSessionToken(), login.getPersonId());
  return result;
}
//--------------------------------------------------------------------------------------------------
@Override
public GLRemoteServiceResponse processRequest(final GLRemoteServiceRequest request) {
  final GLRemoteServiceResponse result = new GLRemoteServiceResponse();
  GLLog.debug("Request:" + request);
  switch (request.getRemoteServiceRequestType()) {
    case ApplyDBChanges:
      applyDBChanges(request.getDeletedKeyValueMap(), request.getDBUpdateList());
      break;
    case GetNextId:
      result.setNextId(getNextId(request.getTableName(), request.getNumberOfValues()));
      break;
    case GetTableMetadata:
      result.setTableMetadataList(getTableMetadataList(request.getTableNameList()));
      break;
    case Select:
      result.setSelectResultList(select(request.getXMLString()));
      break;
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
public ArrayList<String> select(final String xmlRequest) {
  return GLDBStatement.select(xmlRequest);
}
//--------------------------------------------------------------------------------------------------
}