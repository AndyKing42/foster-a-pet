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
import java.util.ArrayList;

public class GLSelectServiceResponse extends GLServiceResponse implements Serializable {
//--------------------------------------------------------------------------------------------------
private static final long serialVersionUID = 1122L;

private ArrayList<String> _resultList;
//--------------------------------------------------------------------------------------------------
/**
 * Provide a default constructor so that objects can be passed to the server using GWT RPC.
 */
@SuppressWarnings("unused")
private GLSelectServiceResponse() {}
//--------------------------------------------------------------------------------------------------
public GLSelectServiceResponse(final ArrayList<String> resultList) {
  super();
  _resultList = resultList;
}
//--------------------------------------------------------------------------------------------------
public ArrayList<String> getResultList() {
  return _resultList;
}
//--------------------------------------------------------------------------------------------------
}