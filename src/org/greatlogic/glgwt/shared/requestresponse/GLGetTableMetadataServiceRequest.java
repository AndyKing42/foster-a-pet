package org.greatlogic.glgwt.shared.requestresponse;

import java.io.Serializable;
import java.util.ArrayList;
import org.greatlogic.glgwt.shared.IGLSharedEnums.EGLServiceRequestType;
import org.greatlogic.glgwt.shared.IGLTable;

public class GLGetTableMetadataServiceRequest extends GLServiceRequest implements Serializable {
//--------------------------------------------------------------------------------------------------
private static final long         serialVersionUID = 1122L;

private final ArrayList<IGLTable> _tableList;
//--------------------------------------------------------------------------------------------------
public GLGetTableMetadataServiceRequest(final ArrayList<IGLTable> tableList) {
  super(EGLServiceRequestType.GetTableMetadata);
  _tableList = tableList;
}
//--------------------------------------------------------------------------------------------------
public ArrayList<IGLTable> getTableList() {
  return _tableList;
}
//--------------------------------------------------------------------------------------------------
}