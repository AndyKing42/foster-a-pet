package org.greatlogic.glgwt.shared.requestresponse;

import java.io.Serializable;
import org.greatlogic.glgwt.shared.IGLSharedEnums.EGLServiceRequestType;
import org.greatlogic.glgwt.shared.IGLTable;

public class GLGetNextIdServiceRequest extends GLServiceRequest implements Serializable {
//--------------------------------------------------------------------------------------------------
private static final long serialVersionUID = 1122L;

private final int         _numberOfValues;
private final IGLTable    _table;
//--------------------------------------------------------------------------------------------------
public GLGetNextIdServiceRequest(final IGLTable table, final int numberOfValues) {
  super(EGLServiceRequestType.GetNextId);
  _table = table;
  _numberOfValues = numberOfValues;
}
//--------------------------------------------------------------------------------------------------
public int getNumberOfValues() {
  return _numberOfValues;
}
//--------------------------------------------------------------------------------------------------
public IGLTable getTable() {
  return _table;
}
//--------------------------------------------------------------------------------------------------
}