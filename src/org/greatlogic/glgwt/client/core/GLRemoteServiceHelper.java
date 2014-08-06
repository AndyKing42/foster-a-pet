package org.greatlogic.glgwt.client.core;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import org.greatlogic.glgwt.client.db.GLDBUpdate;
import org.greatlogic.glgwt.shared.IGLTable;
import org.greatlogic.glgwt.shared.requestresponse.GLApplyDBChangesServiceRequest;
import org.greatlogic.glgwt.shared.requestresponse.GLGetNextIdServiceRequest;
import org.greatlogic.glgwt.shared.requestresponse.GLGetTableMetadataServiceRequest;
import org.greatlogic.glgwt.shared.requestresponse.GLSelectServiceRequest;
import org.greatlogic.glgwt.shared.requestresponse.GLServiceRequest;
import org.greatlogic.glgwt.shared.requestresponse.GLServiceResponse;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GLRemoteServiceHelper {
//--------------------------------------------------------------------------------------------------
public void applyDBChanges(final TreeMap<IGLTable, TreeSet<String>> deletedKeyValueMap,
                           final ArrayList<GLDBUpdate> dbUpdateList,
                           final AsyncCallback<GLServiceResponse> callback) {
  final GLServiceRequest request = new GLApplyDBChangesServiceRequest(deletedKeyValueMap, //
                                                                      dbUpdateList);
  sendRequest(request, callback);
}
//--------------------------------------------------------------------------------------------------
public void getNextId(final IGLTable table, final int numberOfValues,
                      final AsyncCallback<GLServiceResponse> callback) {
  final GLServiceRequest request = new GLGetNextIdServiceRequest(table, numberOfValues);
  sendRequest(request, callback);
}
//--------------------------------------------------------------------------------------------------
public void getTableMetadata(final ArrayList<IGLTable> tableList,
                             final AsyncCallback<GLServiceResponse> callback) {
  final GLServiceRequest request = new GLGetTableMetadataServiceRequest(tableList);
  sendRequest(request, callback);

}
//--------------------------------------------------------------------------------------------------
public void select(final String xmlString, final AsyncCallback<GLServiceResponse> callback) {
  final GLServiceRequest request = new GLSelectServiceRequest(xmlString);
  sendRequest(request, callback);

}
//--------------------------------------------------------------------------------------------------
private void sendRequest(final GLServiceRequest request,
                         final AsyncCallback<GLServiceResponse> callback) {
  GLClientUtil.getRemoteService().processRequest(request, callback);
}
//--------------------------------------------------------------------------------------------------
}