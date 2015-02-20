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
import java.util.TreeMap;
import java.util.TreeSet;
import org.greatlogic.glgwt.client.db.GLDBUpdate;
import org.greatlogic.glgwt.shared.IGLSharedEnums.EGLServiceRequestType;
import org.greatlogic.glgwt.shared.IGLTable;

public class GLApplyDBChangesServiceRequest extends GLServiceRequest implements Serializable {
//--------------------------------------------------------------------------------------------------
private static final long                  serialVersionUID = 1122L;

private ArrayList<GLDBUpdate>              _dbUpdateList;
private TreeMap<IGLTable, TreeSet<String>> _deletedKeyValueMap;
//--------------------------------------------------------------------------------------------------
/**
 * Provide a default constructor so that objects can be passed to the server using GWT RPC.
 */
@SuppressWarnings("unused")
private GLApplyDBChangesServiceRequest() {}
//--------------------------------------------------------------------------------------------------
public GLApplyDBChangesServiceRequest(final TreeMap<IGLTable, TreeSet<String>> deletedKeyValueMap,
                                      final ArrayList<GLDBUpdate> dbUpdateList) {
  super(EGLServiceRequestType.ApplyDBChanges);
  _deletedKeyValueMap = deletedKeyValueMap;
  _dbUpdateList = dbUpdateList;
}
//--------------------------------------------------------------------------------------------------
public ArrayList<GLDBUpdate> getDBUpdateList() {
  return _dbUpdateList;
}
//--------------------------------------------------------------------------------------------------
public TreeMap<IGLTable, TreeSet<String>> getDeletedKeyValueMap() {
  return _deletedKeyValueMap;
}
//--------------------------------------------------------------------------------------------------
}