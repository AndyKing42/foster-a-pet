package org.fosterapet.shared;

import java.util.Collection;
import java.util.TreeMap;
import org.fosterapet.shared.IFAPEnums.ELookupType;
import org.greatlogic.glgwt.client.core.GLRecord;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLEnums.EGLColumnDataType;
import org.greatlogic.glgwt.shared.IGLLookupType;
import org.greatlogic.glgwt.shared.IGLTable;

public interface IDBEnums {
//--------------------------------------------------------------------------------------------------
public enum EFAPId {
PetId(1, "PetId", EFAPTable.Pet),
PetTypeId(2, "PetTypeId", EFAPTable.PetType);
private final String           _name;
private final int              _nextId;
private final EFAPTable _table;
private EFAPId(final int nextId, final String name, final EFAPTable table) {
  _nextId = nextId;
  _name = name;
  _table = table;
}
public String getName() {
  return _name;
}
public int getNextId() {
  return _nextId;
}
public EFAPTable getTable() {
  return _table;
}
}
//--------------------------------------------------------------------------------------------------
public enum EFAPTable implements IGLTable {
NextId(IDBEnums.NextId.class),
Pet(IDBEnums.Pet.class),
PetType(IDBEnums.PetType.class);
private TreeMap<String, IGLColumn>     _columnByColumnNameMap;
private final Class<? extends Enum<?>> _columnClass;
private TreeMap<Integer, IGLColumn>    _comboboxColumnMap;
private TreeMap<Integer, IGLColumn>    _primaryKeyColumnMap;
private EFAPTable(final Class<? extends Enum<?>> columnClass) {
  _columnClass = columnClass;
}
private void createColumnByColumnNameMap() {
  if (_columnByColumnNameMap == null) {
    _columnByColumnNameMap = new TreeMap<>();
    _comboboxColumnMap = new TreeMap<>();
    _primaryKeyColumnMap = new TreeMap<>();
    for (final Enum<?> columnEnumConstant : _columnClass.getEnumConstants()) {
      final IGLColumn column = (IGLColumn)columnEnumConstant;
      _columnByColumnNameMap.put(column.toString(), column);
      if (column.getPrimaryKeySeq() > 0) {
        _primaryKeyColumnMap.put(column.getPrimaryKeySeq(), column);
      }
      if (column.getComboboxSeq() > 0) {
        _comboboxColumnMap.put(column.getComboboxSeq(), column);
      }
    }
  }
}
@Override
public IGLColumn findColumnUsingColumnName(final String columnName) {
  createColumnByColumnNameMap();
  return _columnByColumnNameMap.get(columnName);
}
@Override
public Collection<IGLColumn> getColumns() {
  createColumnByColumnNameMap();
  return _columnByColumnNameMap.values();
}
@Override
public TreeMap<Integer, IGLColumn> getComboboxColumnMap() {
  return _comboboxColumnMap;
}
@Override
public TreeMap<Integer, IGLColumn> getPrimaryKeyColumnMap() {
  createColumnByColumnNameMap();
  return _primaryKeyColumnMap;
}
@Override
public void initializeNewRecord(final GLRecord record) {
  createColumnByColumnNameMap();
  for (final IGLColumn column : _columnByColumnNameMap.values()) {
    final Object defaultValue = column.getDefaultValue();
    if (defaultValue != null) {
      record.set(column, defaultValue);
    }
  }
}
}
//--------------------------------------------------------------------------------------------------
public enum NextId implements IGLColumn {
NextId(EGLColumnDataType.Int, null, 0, false, 1, 0, null, "Id", 50),
NextIdName(EGLColumnDataType.String, null, 50, false, 1, 0, null, "Name", 100),
NextIdTableName(EGLColumnDataType.String, null, 50, true, 0, 0, null, "Table", 100),
NextIdValue(EGLColumnDataType.Int, 100, 0, false, 0, 0, null, "Next Value", 10);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final int               _numberOfDecimalPlaces;
private final int               _primaryKeySeq;
private final String            _title;
private NextId(final EGLColumnDataType dataType, final Object defaultValue,
               final int numberOfDecimalPlaces, final boolean nullable, final int primaryKeySeq,
               final int comboboxSeq, final IGLLookupType lookupType, final String title,
               final int defaultGridColumnWidth) {
  _dataType = dataType;
  _defaultValue = defaultValue;
  _numberOfDecimalPlaces = numberOfDecimalPlaces;
  _nullable = nullable;
  _primaryKeySeq = primaryKeySeq;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
  _title = title;
  _defaultGridColumnWidth = defaultGridColumnWidth;
}
@Override
public int getComboboxSeq() {
  return _comboboxSeq;
}
@Override
public EGLColumnDataType getDataType() {
  return _dataType;
}
@Override
public int getDefaultGridColumnWidth() {
  return _defaultGridColumnWidth;
}
@Override
public Object getDefaultValue() {
  return _defaultValue;
}
@Override
public IGLLookupType getLookupType() {
  return _lookupType;
}
@Override
public boolean getNullable() {
  return _nullable;
}
@Override
public int getNumberOfDecimalPlaces() {
  return _numberOfDecimalPlaces;
}
@Override
public int getPrimaryKeySeq() {
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum Pet implements IGLColumn {
AdoptionFee(EGLColumnDataType.Currency, 0, 2, false, 0, 0, null, "Adoption Fee", 100),
FosterDate(EGLColumnDataType.Date, null, 0, true, 0, 0, null, "Foster Date", 100),
IntakeDate(EGLColumnDataType.DateTime, null, 0, true, 0, 0, null, "Intake Date/Time", 125),
NumberOfFosters(EGLColumnDataType.Int, 0, 0, false, 0, 0, null, "Number Of Fosters", 60),
PetId(EGLColumnDataType.Int, null, 0, false, 1, 0, null, "Id", 50),
PetName(EGLColumnDataType.String, null, 0, false, 0, 1, null, "Pet Name", 80),
PetTypeId(EGLColumnDataType.Int, null, 0, false, 0, 0, ELookupType.PetType, "Pet Type", 80),
Sex(EGLColumnDataType.String, "U", 0, false, 0, 0, ELookupType.Sex, "Sex", 50),
TrainedFlag(EGLColumnDataType.Boolean, "N", 0, false, 0, 0, null, "Trained?", 80);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final int               _numberOfDecimalPlaces;
private final int               _primaryKeySeq;
private final String            _title;
private Pet(final EGLColumnDataType dataType, final Object defaultValue,
            final int numberOfDecimalPlaces, final boolean nullable, final int primaryKeySeq,
            final int comboboxSeq, final IGLLookupType lookupType, final String title,
            final int defaultGridColumnWidth) {
  _dataType = dataType;
  _defaultValue = defaultValue;
  _numberOfDecimalPlaces = numberOfDecimalPlaces;
  _nullable = nullable;
  _primaryKeySeq = primaryKeySeq;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
  _title = title;
  _defaultGridColumnWidth = defaultGridColumnWidth;
}
@Override
public int getComboboxSeq() {
  return _comboboxSeq;
}
@Override
public EGLColumnDataType getDataType() {
  return _dataType;
}
@Override
public int getDefaultGridColumnWidth() {
  return _defaultGridColumnWidth;
}
@Override
public Object getDefaultValue() {
  return _defaultValue;
}
@Override
public IGLLookupType getLookupType() {
  return _lookupType;
}
@Override
public boolean getNullable() {
  return _nullable;
}
@Override
public int getNumberOfDecimalPlaces() {
  return _numberOfDecimalPlaces;
}
@Override
public int getPrimaryKeySeq() {
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum PetType implements IGLColumn {
PetTypeDesc(EGLColumnDataType.String, null, 0, false, 0, 0, null, "Pet Type Desc", 100),
PetTypeId(EGLColumnDataType.Int, null, 0, false, 1, 0, null, "Id", 50),
PetTypeShortDesc(EGLColumnDataType.String, null, 0, false, 0, 1, null, "Pet Type Short Desc", 10);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final int               _numberOfDecimalPlaces;
private final int               _primaryKeySeq;
private final String            _title;
private PetType(final EGLColumnDataType dataType, final Object defaultValue,
                final int numberOfDecimalPlaces, final boolean nullable, final int primaryKeySeq,
                final int comboboxSeq, final IGLLookupType lookupType, final String title,
                final int defaultGridColumnWidth) {
  _dataType = dataType;
  _defaultValue = defaultValue;
  _numberOfDecimalPlaces = numberOfDecimalPlaces;
  _nullable = nullable;
  _primaryKeySeq = primaryKeySeq;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
  _title = title;
  _defaultGridColumnWidth = defaultGridColumnWidth;
}
@Override
public int getComboboxSeq() {
  return _comboboxSeq;
}
@Override
public EGLColumnDataType getDataType() {
  return _dataType;
}
@Override
public int getDefaultGridColumnWidth() {
  return _defaultGridColumnWidth;
}
@Override
public Object getDefaultValue() {
  return _defaultValue;
}
@Override
public IGLLookupType getLookupType() {
  return _lookupType;
}
@Override
public boolean getNullable() {
  return _nullable;
}
@Override
public int getNumberOfDecimalPlaces() {
  return _numberOfDecimalPlaces;
}
@Override
public int getPrimaryKeySeq() {
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
}