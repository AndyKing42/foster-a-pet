package org.greatlogic.glgwt.shared.requestresponse;

import java.io.Serializable;

public class GLGetNextIdServiceResponse extends GLServiceResponse implements Serializable {
//--------------------------------------------------------------------------------------------------
private static final long serialVersionUID = 1122L;

private final int         _nextId;
//--------------------------------------------------------------------------------------------------
public GLGetNextIdServiceResponse(final int nextId) {
  super();
  _nextId = nextId;
}
//--------------------------------------------------------------------------------------------------
public int getNextId() {
  return _nextId;
}
//--------------------------------------------------------------------------------------------------
}