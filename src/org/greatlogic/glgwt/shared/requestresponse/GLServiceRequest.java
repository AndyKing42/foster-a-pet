package org.greatlogic.glgwt.shared.requestresponse;

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
@SuppressWarnings("unused")
private GLServiceRequest() {}
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