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
@Override
public String toString() {
  return "Person id:" + _personId;
}
//--------------------------------------------------------------------------------------------------
}