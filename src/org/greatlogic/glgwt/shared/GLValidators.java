package org.greatlogic.glgwt.shared;

import java.util.HashMap;
import com.sencha.gxt.widget.core.client.form.Validator;

public abstract class GLValidators {
//--------------------------------------------------------------------------------------------------
private final HashMap<IGLColumn, Validator<?>>      _columnValidatorMap;
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