package org.greatlogic.glgwt.shared;


public class GLValidationError {
//--------------------------------------------------------------------------------------------------
private final IGLColumn _column;
private final String    _message;
//--------------------------------------------------------------------------------------------------
public GLValidationError(final IGLColumn column, final String message) {
  _column = column;
  _message = message;
}
//--------------------------------------------------------------------------------------------------
public IGLColumn getColumn() {
  return _column;
}
//--------------------------------------------------------------------------------------------------
public String getMessage() {
  return _message;
}
//--------------------------------------------------------------------------------------------------
}