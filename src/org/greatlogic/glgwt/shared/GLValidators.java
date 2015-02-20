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
import java.util.HashMap;
import com.sencha.gxt.widget.core.client.form.Validator;

public abstract class GLValidators {
//--------------------------------------------------------------------------------------------------
private final HashMap<IGLColumn, Validator<?>>     _columnValidatorMap;
private final HashMap<IGLTable, GLRecordValidator> _recordValidatorMap;
//--------------------------------------------------------------------------------------------------
protected GLValidators() {
  _columnValidatorMap = new HashMap<>();
  _recordValidatorMap = new HashMap<>();
}
//--------------------------------------------------------------------------------------------------
public void addColumnValidator(final IGLColumn column, final Validator<?> validator) {
  _columnValidatorMap.put(column, validator);
}
//--------------------------------------------------------------------------------------------------
public void addRecordValidator(final IGLTable table, final GLRecordValidator recordValidator) {
  _recordValidatorMap.put(table, recordValidator);
}
//--------------------------------------------------------------------------------------------------
public Validator<?> getColumnValidator(final IGLColumn column) {
  return _columnValidatorMap.get(column);
}
//--------------------------------------------------------------------------------------------------
public GLRecordValidator getRecordValidator(final IGLTable table) {
  return _recordValidatorMap.get(table);
}
//--------------------------------------------------------------------------------------------------
}