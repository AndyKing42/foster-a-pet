package org.greatlogic.glgwt.client.core;
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
import org.greatlogic.glgwt.shared.IGLTable;
import org.greatlogic.glgwt.shared.requestresponse.GLApplyDBChangesServiceRequest;
import org.greatlogic.glgwt.shared.requestresponse.GLGetNextIdServiceRequest;
import org.greatlogic.glgwt.shared.requestresponse.GLGetTableMetadataServiceRequest;
import org.greatlogic.glgwt.shared.requestresponse.GLSelectServiceRequest;
import org.greatlogic.glgwt.shared.requestresponse.GLServiceRequest;
import org.greatlogic.glgwt.shared.requestresponse.GLServiceResponse;
import com.google.gwt.user.client.Cookies;
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
  GLClientUtil.getRemoteService().processRequest(request, new AsyncCallback<GLServiceResponse>() {
    @Override
    public void onFailure(final Throwable t) {
      // todo Auto-generated method stub
    }
    @Override
    public void onSuccess(final GLServiceResponse response) {
      if (response == null) { // the user isn't logged in
        GLClientUtil.logIn(createLoginSuccessfulCallback(request, callback));
        return;
      }
      if (!response.getSessionToken().equals(GLClientUtil.getSessionToken())) {
        GLClientUtil.setSessionToken(response.getSessionToken());
        Cookies.setCookie(GLClientUtil.SessionTokenCookie, response.getSessionToken());
      }
      callback.onSuccess(response);
    }
  });
}
//--------------------------------------------------------------------------------------------------
private AsyncCallback<Void> createLoginSuccessfulCallback(final GLServiceRequest request,
                                                          final AsyncCallback<GLServiceResponse> callback) {
  final AsyncCallback<Void> result = new AsyncCallback<Void>() {
    @Override
    public void onFailure(final Throwable caught) {
      // todo Auto-generated method stub
    }
    @Override
    public void onSuccess(final Void response) {
      sendRequest(request, callback);
    }
  };
  return result;
}
//--------------------------------------------------------------------------------------------------
}