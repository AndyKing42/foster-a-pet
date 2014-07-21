package org.greatlogic.glgwt.shared;

import com.google.gwt.user.client.rpc.RemoteService;

public interface IGLRemoteService extends RemoteService {
//--------------------------------------------------------------------------------------------------
void applyDBChanges(final String dbChanges);
int getNextId(final String tableName, final int numberOfValues);
String getTableMetadata(final String tableNames);
void log(final int priority, final String location, final String message);
Integer login(final String loginName, final String password);
String select(final String xmlRequest);
//--------------------------------------------------------------------------------------------------
}