package org.greatlogic.glgwt.shared.requestresponse;

import java.io.Serializable;
import org.greatlogic.glgwt.shared.IGLSharedEnums.EGLServiceRequestType;

public class GLSelectServiceRequest extends GLServiceRequest implements Serializable {
//--------------------------------------------------------------------------------------------------
private static final long serialVersionUID = 1122L;

private final String      _xmlString;
//--------------------------------------------------------------------------------------------------
public GLSelectServiceRequest(final String xmlString) {
  super(EGLServiceRequestType.Select);
  _xmlString = xmlString;
}
//--------------------------------------------------------------------------------------------------
public String getXMLString() {
  return _xmlString;
}
//--------------------------------------------------------------------------------------------------
}