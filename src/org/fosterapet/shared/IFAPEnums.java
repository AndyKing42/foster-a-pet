package org.fosterapet.shared;

import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.greatlogic.glgwt.shared.IGLLookupType;
import org.greatlogic.glgwt.shared.IGLTable;

public interface IFAPEnums {
//--------------------------------------------------------------------------------------------------
public enum ELookupType implements IGLLookupType {
PetType(EFAPTable.PetType),
Sex(null);
private final IGLTable _table;
private ELookupType(final IGLTable table) {
  _table = table;
}
@Override
public IGLTable getTable() {
  return _table;
}
}
//--------------------------------------------------------------------------------------------------
public enum ETestDataOption {
Reload,
Unknown;
public static ETestDataOption lookup(final String testDataOptionString) {
  try {
    return ETestDataOption.valueOf(testDataOptionString);
  }
  catch (final Exception e) {
    return ETestDataOption.Unknown;
  }
}
}
//--------------------------------------------------------------------------------------------------
}