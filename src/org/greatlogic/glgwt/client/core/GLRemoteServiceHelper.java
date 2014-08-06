package org.greatlogic.glgwt.client.core;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import org.greatlogic.glgwt.client.db.GLDBUpdate;
import org.greatlogic.glgwt.shared.GLRemoteServiceRequest;
import org.greatlogic.glgwt.shared.GLRemoteServiceResponse;
import org.greatlogic.glgwt.shared.IGLSharedEnums.EGLRemoteServiceRequestType;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GLRemoteServiceHelper {
//--------------------------------------------------------------------------------------------------
private GLRemoteServiceRequest createRequest(final EGLRemoteServiceRequestType requestType) {
  final GLRemoteServiceRequest result = new GLRemoteServiceRequest(GLClientUtil.getSessionToken(), //
                                                                   requestType);
  return result;
}
//--------------------------------------------------------------------------------------------------
public void applyDBChanges(final TreeMap<IGLTable, TreeSet<String>> deletedKeyValueMap,
                           final ArrayList<GLDBUpdate> dbUpdateList,
                           final AsyncCallback<GLRemoteServiceResponse> callback) {
  final GLRemoteServiceRequest request = createRequest(EGLRemoteServiceRequestType.ApplyDBChanges);
  request.setDBUpdateList(dbUpdateList);
  request.setDeletedKeyValueMap(deletedKeyValueMap);
  sendRequest(request, callback);
}
//--------------------------------------------------------------------------------------------------
public void getNextId(final IGLTable table, final int numberOfValues,
                      final AsyncCallback<GLRemoteServiceResponse> callback) {
  final GLRemoteServiceRequest request = createRequest(EGLRemoteServiceRequestType.GetNextId);
  request.setTableName(table.toString());
  request.setNumberOfValues(numberOfValues);
  sendRequest(request, callback);
}
//--------------------------------------------------------------------------------------------------
public void getTableMetadata(final ArrayList<String> tableNameList,
                             final AsyncCallback<GLRemoteServiceResponse> callback) {
  final GLRemoteServiceRequest request =
                                         createRequest(EGLRemoteServiceRequestType.GetTableMetadata);
  request.setTableNameList(tableNameList);
  sendRequest(request, callback);

}
//--------------------------------------------------------------------------------------------------
public void select(final String xmlString, final AsyncCallback<GLRemoteServiceResponse> callback) {
  final GLRemoteServiceRequest request = createRequest(EGLRemoteServiceRequestType.Select);
  request.setXMLString(xmlString);
  sendRequest(request, callback);

}
//--------------------------------------------------------------------------------------------------
private void sendRequest(final GLRemoteServiceRequest request,
                         final AsyncCallback<GLRemoteServiceResponse> callback) {
  GLClientUtil.getRemoteService().processRequest(request, callback);
}
//--------------------------------------------------------------------------------------------------
}