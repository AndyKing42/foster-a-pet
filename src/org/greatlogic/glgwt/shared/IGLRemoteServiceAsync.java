package org.greatlogic.glgwt.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IGLRemoteServiceAsync {
//--------------------------------------------------------------------------------------------------
void applyDBChanges(final String dbChanges, final AsyncCallback<Void> callback);
void getNextId(final String tableName, final int numberOfValues,
               final AsyncCallback<Integer> callback);
void getTableMetadata(final String tableNames, final AsyncCallback<String> callback);
void log(final int priority, final String location, final String message,
         final AsyncCallback<Void> callback);
void login(final String loginName, final String password, final AsyncCallback<Integer> callback);
void select(final String xmlRequest, final AsyncCallback<String> asyncCallback);
//--------------------------------------------------------------------------------------------------
}