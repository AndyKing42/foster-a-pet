package org.greatlogic.glgwt.shared.requestresponse;

import java.io.Serializable;
import java.util.ArrayList;

public class GLGetTableMetadataServiceResponse extends GLServiceResponse implements Serializable {
//--------------------------------------------------------------------------------------------------
private static final long       serialVersionUID = 1122L;

private final ArrayList<String> _metadataList;
//--------------------------------------------------------------------------------------------------
public GLGetTableMetadataServiceResponse(final ArrayList<String> metadataList) {
  super();
  _metadataList = metadataList;
}
//--------------------------------------------------------------------------------------------------
public ArrayList<String> getMetadataList() {
  return _metadataList;
}
//--------------------------------------------------------------------------------------------------
}