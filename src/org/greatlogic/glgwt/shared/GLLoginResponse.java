package org.greatlogic.glgwt.shared;

import java.io.Serializable;

public class GLLoginResponse implements Serializable {
//--------------------------------------------------------------------------------------------------
private static final long serialVersionUID = 1122L;
private int               _personId;
private boolean           _succeeded;
private String            _sessionToken;
//--------------------------------------------------------------------------------------------------
public void setResultValues(final boolean succeeded, final String sessionToken, final int personId) {
  _succeeded = succeeded;
  if (_succeeded) {
    _sessionToken = sessionToken;
    _personId = personId;
  }
}
//--------------------------------------------------------------------------------------------------
public int getPersonId() {
  return _personId;
}
//--------------------------------------------------------------------------------------------------
public boolean getSucceeded() {
  return _succeeded;
}
//--------------------------------------------------------------------------------------------------
public String getSessionToken() {
  return _sessionToken;
}
//--------------------------------------------------------------------------------------------------
}