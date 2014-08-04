package org.greatlogic.glgwt.shared;
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