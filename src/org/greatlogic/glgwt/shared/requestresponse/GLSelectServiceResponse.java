package org.greatlogic.glgwt.shared.requestresponse;

import java.io.Serializable;
import java.util.ArrayList;

public class GLSelectServiceResponse extends GLServiceResponse implements Serializable {
//--------------------------------------------------------------------------------------------------
private static final long       serialVersionUID = 1122L;

private final ArrayList<String> _resultList;
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