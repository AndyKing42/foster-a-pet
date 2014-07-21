package org.greatlogic.glgwt.shared;

import java.util.ArrayList;
import org.greatlogic.glgwt.client.widget.GLValidationRecord;

public abstract class GLRecordValidator {
//--------------------------------------------------------------------------------------------------
private final ArrayList<GLValidationError> _validationErrorList;
//--------------------------------------------------------------------------------------------------
public GLRecordValidator() {
  _validationErrorList = new ArrayList<>();
}
//--------------------------------------------------------------------------------------------------
protected void addError(final IGLColumn column, final String message) {
  _validationErrorList.add(new GLValidationError(column, message));
}
//--------------------------------------------------------------------------------------------------
public ArrayList<GLValidationError> validate(final GLValidationRecord validationRecord) {
  _validationErrorList.clear();
  validateRecord(validationRecord);
  return _validationErrorList;
}
//--------------------------------------------------------------------------------------------------
protected abstract void validateRecord(final GLValidationRecord validationRecord);
//--------------------------------------------------------------------------------------------------
}