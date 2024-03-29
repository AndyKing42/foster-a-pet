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
import org.greatlogic.glgwt.shared.requestresponse.GLServiceRequest;
import org.greatlogic.glgwt.shared.requestresponse.GLServiceResponse;
import com.google.gwt.user.client.rpc.RemoteService;

public interface IGLRemoteService extends RemoteService {
//--------------------------------------------------------------------------------------------------
GLChangePasswordResponse changePassword(final int userId, final String oldPassword,
                                        final String newPassword);
void log(final int priority, final String location, final String message);
GLLoginResponse login(final String loginName, final String password,
                      final String sessionTokenFromClient);
GLServiceResponse processRequest(final GLServiceRequest request);
//--------------------------------------------------------------------------------------------------
}