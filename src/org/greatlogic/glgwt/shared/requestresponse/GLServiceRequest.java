package org.greatlogic.glgwt.shared.requestresponse;
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
import java.io.Serializable;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.shared.IGLSharedEnums.EGLServiceRequestType;
public abstract class GLServiceRequest implements Serializable {
//--------------------------------------------------------------------------------------------------
private static final long     serialVersionUID = 1122L;

private EGLServiceRequestType _remoteServiceRequestType;
private String                _sessionToken;
//--------------------------------------------------------------------------------------------------
/**
 * Provide a default constructor so that objects can be passed to the server using GWT RPC.
 */
protected GLServiceRequest() {}
//--------------------------------------------------------------------------------------------------
public GLServiceRequest(final EGLServiceRequestType remoteServiceRequestType) {
  _sessionToken = GLClientUtil.getSessionToken();
  _remoteServiceRequestType = remoteServiceRequestType;
}
//--------------------------------------------------------------------------------------------------
public EGLServiceRequestType getRemoteServiceRequestType() {
  return _remoteServiceRequestType;
}
//--------------------------------------------------------------------------------------------------
public String getSessionToken() {
  return _sessionToken;
}
//--------------------------------------------------------------------------------------------------
@Override
public String toString() {
  return "Type:" + _remoteServiceRequestType + "Token:" + _sessionToken;
}
//--------------------------------------------------------------------------------------------------
}