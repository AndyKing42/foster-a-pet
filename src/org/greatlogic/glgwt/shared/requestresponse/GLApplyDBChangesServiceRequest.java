package org.greatlogic.glgwt.shared.requestresponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import org.greatlogic.glgwt.client.db.GLDBUpdate;
import org.greatlogic.glgwt.shared.IGLSharedEnums.EGLServiceRequestType;
import org.greatlogic.glgwt.shared.IGLTable;

public class GLApplyDBChangesServiceRequest extends GLServiceRequest implements Serializable {
//--------------------------------------------------------------------------------------------------
private static final long                        serialVersionUID = 1122L;

private final ArrayList<GLDBUpdate>              _dbUpdateList;
private final TreeMap<IGLTable, TreeSet<String>> _deletedKeyValueMap;
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