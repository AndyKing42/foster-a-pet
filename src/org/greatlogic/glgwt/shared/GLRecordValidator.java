package org.greatlogic.glgwt.shared;
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