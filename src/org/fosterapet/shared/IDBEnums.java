package org.fosterapet.shared;
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
import java.util.Collection;
import java.util.TreeMap;
import org.fosterapet.shared.IFAPEnums.ELookupType;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLEnums.EGLColumnDataType;
import org.greatlogic.glgwt.shared.IGLLookupType;
import org.greatlogic.glgwt.shared.IGLTable;

public interface IDBEnums {
//--------------------------------------------------------------------------------------------------
public enum EFAPId {
AddressId(1, "AddressId", EFAPTable.Address),
AttributeId(2, "AttributeId", EFAPTable.Attribute),
AttributeDefId(3, "AttributeDefId", EFAPTable.AttributeDef),
CityId(4, "CityId", EFAPTable.City),
DBAuditId(5, "DBAuditId", EFAPTable.DBAudit),
DBUpdateNoteId(6, "DBUpdateNoteId", EFAPTable.DBUpdateNote),
FosterHistoryId(7, "FosterHistoryId", EFAPTable.FosterHistory),
LocId(8, "LocId", EFAPTable.Loc),
LocPersonId(9, "LocPersonId", EFAPTable.LocPerson),
LocTypeId(10, "LocTypeId", EFAPTable.LocType),
NextIdId(11, "NextIdId", EFAPTable.NextId),
OrgId(12, "OrgId", EFAPTable.Org),
OrgPersonId(13, "OrgPersonId", EFAPTable.OrgPerson),
PersonId(14, "PersonId", EFAPTable.Person),
PersonRelationshipId(15, "PersonRelationshipId", EFAPTable.PersonRelationship),
PersonRoleId(16, "PersonRoleId", EFAPTable.PersonRole),
PetId(17, "PetId", EFAPTable.Pet),
PetTypeId(18, "PetTypeId", EFAPTable.PetType),
PlanEntryId(19, "PlanEntryId", EFAPTable.PlanEntry),
PlanPersonId(20, "PlanPersonId", EFAPTable.PlanPerson),
PlanTemplateId(21, "PlanTemplateId", EFAPTable.PlanTemplate),
PlanTemplateEntryId(22, "PlanTemplateEntryId", EFAPTable.PlanTemplateEntry),
PlanTemplateTreatmentId(23, "PlanTemplateTreatmentId", EFAPTable.PlanTemplateTreatment),
SearchDefId(24, "SearchDefId", EFAPTable.SearchDef),
SearchDefDetailId(25, "SearchDefDetailId", EFAPTable.SearchDefDetail),
StateId(26, "StateId", EFAPTable.State),
TreatmentId(27, "TreatmentId", EFAPTable.Treatment),
TreatmentTypeId(28, "TreatmentTypeId", EFAPTable.TreatmentType),
ValueListId(29, "ValueListId", EFAPTable.ValueList);
private final String    _name;
private final int       _nextId;
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
Address(Address.class),
Attribute(Attribute.class),
AttributeDef(AttributeDef.class),
City(City.class),
DBAudit(DBAudit.class),
DBUpdateNote(DBUpdateNote.class),
FosterHistory(FosterHistory.class),
Loc(Loc.class),
LocPerson(LocPerson.class),
LocType(LocType.class),
NextId(NextId.class),
Org(Org.class),
OrgPerson(OrgPerson.class),
Person(Person.class),
PersonRelationship(PersonRelationship.class),
PersonRole(PersonRole.class),
Pet(Pet.class),
PetType(PetType.class),
PlanEntry(PlanEntry.class),
PlanPerson(PlanPerson.class),
PlanTemplate(PlanTemplate.class),
PlanTemplateEntry(PlanTemplateEntry.class),
PlanTemplateTreatment(PlanTemplateTreatment.class),
SearchDef(SearchDef.class),
SearchDefDetail(SearchDefDetail.class),
State(State.class),
Treatment(Treatment.class),
TreatmentType(TreatmentType.class),
ValueList(ValueList.class);
private TreeMap<String, IGLColumn>     _columnByColumnNameMap;
private final Class<? extends Enum<?>> _columnEnumClass;
private TreeMap<Integer, IGLColumn>    _comboboxColumnMap;
private IGLColumn                      _primaryKeyColumn;
private EFAPTable(final Class<? extends Enum<?>> columnEnumClass) {
  _columnEnumClass = columnEnumClass;
}
@Override
public IGLColumn findColumnUsingColumnName(final String columnName) {
  initializeColumnSettings();
  return _columnByColumnNameMap.get(columnName);
}
@Override
public String getAbbrev() {
  return _columnEnumClass.getSimpleName();
}
@Override
public Class<? extends Enum<?>> getColumnEnumClass() {
  return _columnEnumClass;
}
@Override
public Collection<IGLColumn> getColumns() {
  initializeColumnSettings();
  return _columnByColumnNameMap.values();
}
@Override
public TreeMap<Integer, IGLColumn> getComboboxColumnMap() {
  initializeColumnSettings();
  return _comboboxColumnMap;
}
@Override
public String getDataSourceName() {
  return null;
}
@Override
public IGLColumn getPrimaryKeyColumn() {
  initializeColumnSettings();
  return _primaryKeyColumn;
}
@Override
public void initializeColumnSettings() {
  if (_columnByColumnNameMap == null) {
    _columnByColumnNameMap = new TreeMap<>();
    _comboboxColumnMap = new TreeMap<>();
    IGLColumn possiblePrimaryKeyColumn = null;
    for (final Enum<?> columnEnumConstant : _columnEnumClass.getEnumConstants()) {
      final IGLColumn column = (IGLColumn)columnEnumConstant;
      _columnByColumnNameMap.put(column.toString(), column);
      if (column.getPrimaryKey()) {
        _primaryKeyColumn = column;
      }
      if (column.getComboboxSeq() > 0) {
        _comboboxColumnMap.put(column.getComboboxSeq(), column);
      }
      if (possiblePrimaryKeyColumn == null || column.toString().equalsIgnoreCase(name() + "Id")) {
        possiblePrimaryKeyColumn = column;
      }
    }
    if (_primaryKeyColumn == null) {
      _primaryKeyColumn = possiblePrimaryKeyColumn;
    }
    if (_comboboxColumnMap.size() == 0) {
      _comboboxColumnMap.put(1, _primaryKeyColumn);
    }
  }
}
@Override
public void initializeNewRecord(final GLRecord record) {
  initializeColumnSettings();
  for (final IGLColumn column : _columnByColumnNameMap.values()) {
    final Object defaultValue = column.getDefaultValue();
    if (defaultValue != null) {
      record.set(column, defaultValue);
    }
  }
}
}
//--------------------------------------------------------------------------------------------------
public enum Address implements IGLColumn {
AddressId("Address Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
AddressLine1("Address Line 1", EGLColumnDataType.String, 100, true, null, false, 0, null, -1),
AddressLine2("Address Line 2", EGLColumnDataType.String, 100, true, null, false, 0, null, -1),
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
CityId("City Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.City, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1),
ZIPCode("ZIP Code", EGLColumnDataType.String, 10, true, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private Address(final String title, final EGLColumnDataType dataType,
                final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
                final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
                final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum Attribute implements IGLColumn {
AdditionalComments("Additional Comments", EGLColumnDataType.String, 2000, true, null, false, 0, null, -1),
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
AttributeDefId("Attribute Def Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.AttributeDef, -1),
AttributeId("Attribute Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
AttributeSeq("Attribute Seq", EGLColumnDataType.Int, 0, false, null, false, 0, null, -1),
AttributeValue("Attribute Value", EGLColumnDataType.String, 2000, true, null, false, 0, null, -1),
CreatedByPersonId("Created By Person Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
EntityId("Entity Id", EGLColumnDataType.Int, 0, false, null, false, 0, null, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private Attribute(final String title, final EGLColumnDataType dataType,
                  final int decimalPlacesOrLength, final boolean nullable,
                  final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                  final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum AttributeDef implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
AttributeDefId("Attribute Def Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
AttributeDefName("Attribute Def Name", EGLColumnDataType.String, 30, false, null, false, 1, null, -1),
AttributeDefType("Attribute Def Type", EGLColumnDataType.String, 3, false, null, false, 0, null, -1),
AutomaticallyCreateFlag("Automatically Create Flag", EGLColumnDataType.Boolean, 1, false, null, false, 0, null, -1),
DataType("Data Type", EGLColumnDataType.String, 10, false, null, false, 0, null, -1),
EntityTypeId("Entity Type Id", EGLColumnDataType.Int, 0, true, null, false, 0, ELookupType.EntityType, -1),
MaxOccurrences("Max Occurrences", EGLColumnDataType.Int, 0, false, null, false, 0, null, -1),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, false, 0, null, -1),
RequiredFlag("Required Flag", EGLColumnDataType.Boolean, 1, false, null, false, 0, null, -1),
ValueListId("Value List Id", EGLColumnDataType.Int, 0, true, null, false, 0, ELookupType.ValueList, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private AttributeDef(final String title, final EGLColumnDataType dataType,
                     final int decimalPlacesOrLength, final boolean nullable,
                     final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                     final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum City implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
CityId("City Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
CityName("City Name", EGLColumnDataType.String, 100, false, null, false, 1, null, -1),
StateId("State Id", EGLColumnDataType.Int, 0, false, null, false, 2, ELookupType.State, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private City(final String title, final EGLColumnDataType dataType, final int decimalPlacesOrLength,
             final boolean nullable, final String defaultValue, final boolean primaryKey,
             final int comboboxSeq, final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum DBAudit implements IGLColumn {
AuditAction("Audit Action", EGLColumnDataType.String, 1, false, null, false, 0, null, -1),
AuditDate("Audit Date", EGLColumnDataType.DateTime, 0, false, null, false, 0, null, -1),
DBAuditId("D B Audit Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
NewData("New Data", EGLColumnDataType.String, 1000000, true, null, false, 0, null, -1),
OldData("Old Data", EGLColumnDataType.String, 1000000, true, null, false, 0, null, -1),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
PersonId("Person Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private DBAudit(final String title, final EGLColumnDataType dataType,
                final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
                final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
                final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum DBUpdateNote implements IGLColumn {
AppliedDateTime("Applied Date/Time", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
DBRevNumber("DB Rev Number", EGLColumnDataType.String, 30, false, null, false, 1, null, -1),
DBUpdateDesc("DB Update Description", EGLColumnDataType.String, 2000, false, null, false, 0, null, -1),
DBUpdateNoteId("DB Update Note Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
DevDateTime("Development Date/Time", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private DBUpdateNote(final String title, final EGLColumnDataType dataType,
                     final int decimalPlacesOrLength, final boolean nullable,
                     final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                     final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum FosterHistory implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
FosterDateFinish("Foster Date Finish", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
FosterDateStart("Foster Date Start", EGLColumnDataType.DateTime, 0, false, null, false, 0, null, -1),
FosterHistoryId("Foster History Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
LocId("Loc Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Loc, -1),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
PetId("Pet Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Pet, -1),
PrimaryPersonId("Primary Person Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private FosterHistory(final String title, final EGLColumnDataType dataType,
                      final int decimalPlacesOrLength, final boolean nullable,
                      final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                      final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum Loc implements IGLColumn {
AddressId("Address Id", EGLColumnDataType.Int, 0, true, null, false, 0, ELookupType.Address, -1),
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
LocDesc("Loc Description", EGLColumnDataType.String, 100, true, null, false, 0, null, -1),
LocId("Loc Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
LocShortDesc("Loc Short Description", EGLColumnDataType.String, 30, false, null, false, 1, null, -1),
LocTypeId("Loc Type Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.LocType, -1),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private Loc(final String title, final EGLColumnDataType dataType, final int decimalPlacesOrLength,
            final boolean nullable, final String defaultValue, final boolean primaryKey,
            final int comboboxSeq, final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum LocPerson implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
LocId("Loc Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Loc, -1),
LocPersonId("Loc Person Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
PersonId("Person Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
PrimaryFlag("Primary Flag", EGLColumnDataType.Boolean, 1, false, null, false, 0, null, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private LocPerson(final String title, final EGLColumnDataType dataType,
                  final int decimalPlacesOrLength, final boolean nullable,
                  final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                  final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum LocType implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
LocTypeDesc("Loc Type Description", EGLColumnDataType.String, 50, false, null, false, 0, null, -1),
LocTypeId("Loc Type Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
LocTypeShortDesc("Loc Type Short Description", EGLColumnDataType.String, 10, false, null, false, 1, null, -1),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private LocType(final String title, final EGLColumnDataType dataType,
                final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
                final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
                final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum NextId implements IGLColumn {
NextId("Next Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
NextIdName("Next Id Name", EGLColumnDataType.String, 30, false, null, false, 1, null, -1),
NextIdValue("Next Id Value", EGLColumnDataType.Int, 0, false, null, false, 0, null, -1),
TableName("Table Name", EGLColumnDataType.String, 30, true, null, false, 0, null, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private NextId(final String title, final EGLColumnDataType dataType,
               final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
               final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
               final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum Org implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
OrgDesc("Org Description", EGLColumnDataType.String, 200, false, null, false, 0, null, -1),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
OrgName("Org Name", EGLColumnDataType.String, 50, false, null, false, 1, null, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private Org(final String title, final EGLColumnDataType dataType, final int decimalPlacesOrLength,
            final boolean nullable, final String defaultValue, final boolean primaryKey,
            final int comboboxSeq, final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum OrgPerson implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
OrgPersonId("Org Person Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
PersonId("Person Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
PersonRoleId("Person Role Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.PersonRole, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private OrgPerson(final String title, final EGLColumnDataType dataType,
                  final int decimalPlacesOrLength, final boolean nullable,
                  final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                  final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum Person implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
CurrentOrgId("Current Org Id", EGLColumnDataType.Int, 0, true, null, false, 0, ELookupType.Org, -1),
DateOfBirth("Date Of Birth", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
DisplayName("Display Name", EGLColumnDataType.String, 50, false, null, false, 1, null, -1),
EmailAddress("Email Address", EGLColumnDataType.String, 100, true, null, false, 0, null, -1),
FirstName("First Name", EGLColumnDataType.String, 30, false, null, false, 0, null, -1),
LastName("Last Name", EGLColumnDataType.String, 30, false, null, false, 0, null, -1),
LoginName("Login Name", EGLColumnDataType.String, 100, true, null, false, 0, null, -1),
PasswordHash("Password Hash", EGLColumnDataType.String, 200, true, null, false, 0, null, -1),
PersonId("Person Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
PhoneNumberHome("Phone Number Home", EGLColumnDataType.String, 50, true, null, false, 0, null, -1),
PhoneNumberMobile("Phone Number Mobile", EGLColumnDataType.String, 50, true, null, false, 0, null, -1),
PhoneNumberOffice("Phone Number Office", EGLColumnDataType.String, 50, true, null, false, 0, null, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private Person(final String title, final EGLColumnDataType dataType,
               final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
               final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
               final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum PersonRelationship implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
PersonId1("Person Id 1", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
PersonId2("Person Id 2", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
PersonRelationshipId("Person Relationship Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
RelationshipOf1To2("Relationship Of 1 To 2", EGLColumnDataType.String, 20, false, null, false, 0, ELookupType.PersonRelationship, -1),
RelationshipOf2To1("Relationship Of 2 To 1", EGLColumnDataType.String, 20, false, null, false, 0, ELookupType.PersonRelationship, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private PersonRelationship(final String title, final EGLColumnDataType dataType,
                           final int decimalPlacesOrLength, final boolean nullable,
                           final String defaultValue, final boolean primaryKey,
                           final int comboboxSeq, final IGLLookupType lookupType,
                           final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum PersonRole implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
PersonRoleId("Person Role Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
RoleDesc("Role Description", EGLColumnDataType.String, 30, false, null, false, 0, null, -1),
RoleShortDesc("Role Short Description", EGLColumnDataType.String, 10, false, null, false, 1, null, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private PersonRole(final String title, final EGLColumnDataType dataType,
                   final int decimalPlacesOrLength, final boolean nullable,
                   final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                   final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum Pet implements IGLColumn {
AdoptionFee("Adoption Fee", EGLColumnDataType.Currency, 0, false, "0", false, 0, null, -1),
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
DateOfBirth("Date Of Birth", EGLColumnDataType.Date, 0, false, null, false, 0, null, -1),
IntakeDate("Intake Date", EGLColumnDataType.DateTime, 0, false, null, false, 0, null, -1),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
PetId("Pet Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
PetName("Pet Name", EGLColumnDataType.String, 50, false, null, false, 1, null, -1),
PetTypeId("Pet Type Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.PetType, -1),
Sex("Sex", EGLColumnDataType.String, 1, false, "U", false, 0, ELookupType.Sex, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private Pet(final String title, final EGLColumnDataType dataType, final int decimalPlacesOrLength,
            final boolean nullable, final String defaultValue, final boolean primaryKey,
            final int comboboxSeq, final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum PetType implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
PetTypeDesc("Pet Type Description", EGLColumnDataType.String, 50, false, null, false, 0, null, -1),
PetTypeId("Pet Type Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
PetTypeShortDesc("Pet Type Short Description", EGLColumnDataType.String, 10, false, null, false, 1, null, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private PetType(final String title, final EGLColumnDataType dataType,
                final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
                final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
                final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum PlanEntry implements IGLColumn {
ActionDate("Action Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
LocId("Loc Id", EGLColumnDataType.Int, 0, true, null, false, 0, ELookupType.Loc, -1),
PetId("Pet Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Pet, -1),
PlanEntryId("Plan Entry Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
PlanEntryNotes("Plan Entry Notes", EGLColumnDataType.String, 2000, true, null, false, 0, null, -1),
ScheduledDate("Scheduled Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
TreatmentTypeId("Treatment Type Id", EGLColumnDataType.Int, 0, true, null, false, 0, ELookupType.TreatmentType, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private PlanEntry(final String title, final EGLColumnDataType dataType,
                  final int decimalPlacesOrLength, final boolean nullable,
                  final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                  final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum PlanPerson implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
Notes("Notes", EGLColumnDataType.String, 2000, true, null, false, 0, null, -1),
PersonId("Person Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
PlanEntryId("Plan Entry Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.PlanEntry, -1),
PlanPersonId("Plan Person Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
ReminderType("Reminder Type", EGLColumnDataType.String, 10, false, null, false, 0, null, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private PlanPerson(final String title, final EGLColumnDataType dataType,
                   final int decimalPlacesOrLength, final boolean nullable,
                   final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                   final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum PlanTemplate implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
PlanTemplateDesc("Plan Template Description", EGLColumnDataType.String, 2000, true, null, false, 0, null, -1),
PlanTemplateId("Plan Template Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
PlanTemplateName("Plan Template Name", EGLColumnDataType.String, 50, false, null, false, 1, null, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private PlanTemplate(final String title, final EGLColumnDataType dataType,
                     final int decimalPlacesOrLength, final boolean nullable,
                     final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                     final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum PlanTemplateEntry implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
OffsetNumberOfDays("Offset Number Of Days", EGLColumnDataType.Int, 0, true, null, false, 0, null, -1),
PlanTemplateEntryDesc("Plan Template Entry Description", EGLColumnDataType.String, 50, false, null, false, 1, null, -1),
PlanTemplateEntryId("Plan Template Entry Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
PlanTemplateId("Plan Template Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.PlanTemplate, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private PlanTemplateEntry(final String title, final EGLColumnDataType dataType,
                          final int decimalPlacesOrLength, final boolean nullable,
                          final String defaultValue, final boolean primaryKey,
                          final int comboboxSeq, final IGLLookupType lookupType,
                          final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum PlanTemplateTreatment implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
LocId("Loc Id", EGLColumnDataType.Int, 0, true, null, false, 0, ELookupType.Loc, -1),
PlanTemplateEntryId("Plan Template Entry Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.PlanTemplateEntry, -1),
PlanTemplateTreatmentId("Plan Template Treatment Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
TreatmentTypeId("Treatment Type Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.TreatmentType, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private PlanTemplateTreatment(final String title, final EGLColumnDataType dataType,
                              final int decimalPlacesOrLength, final boolean nullable,
                              final String defaultValue, final boolean primaryKey,
                              final int comboboxSeq, final IGLLookupType lookupType,
                              final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum SearchDef implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
OwnerPersonId("Owner Person Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
PublicFlag("Public Flag", EGLColumnDataType.Boolean, 1, false, null, false, 0, null, -1),
SearchDefDesc("Search Def Description", EGLColumnDataType.String, 100, true, null, false, 0, null, -1),
SearchDefId("Search Def Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
SearchDefName("Search Def Name", EGLColumnDataType.String, 30, true, null, false, 1, null, -1),
SearchDefType("Search Def Type", EGLColumnDataType.String, 20, false, null, false, 0, null, -1),
SortColumns("Sort Columns", EGLColumnDataType.String, 200, true, null, false, 0, null, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private SearchDef(final String title, final EGLColumnDataType dataType,
                  final int decimalPlacesOrLength, final boolean nullable,
                  final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                  final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum SearchDefDetail implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
SearchDefDetailId("Search Def Detail Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
SearchDefId("Search Def Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.SearchDef, -1),
Value1("Value 1", EGLColumnDataType.String, 50, true, null, false, 0, null, -1),
Value2("Value 2", EGLColumnDataType.String, 50, true, null, false, 0, null, -1),
Value3("Value 3", EGLColumnDataType.String, 50, true, null, false, 0, null, -1),
ValueList("Value List", EGLColumnDataType.String, 1000000, true, null, false, 0, null, -1),
ValueName("Value Name", EGLColumnDataType.String, 50, false, null, false, 1, null, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private SearchDefDetail(final String title, final EGLColumnDataType dataType,
                        final int decimalPlacesOrLength, final boolean nullable,
                        final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                        final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum State implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
StateAbbrev("State Abbreviation", EGLColumnDataType.String, 2, false, null, false, 1, null, -1),
StateId("State Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
StateName("State Name", EGLColumnDataType.String, 50, false, null, false, 0, null, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private State(final String title, final EGLColumnDataType dataType,
              final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
              final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
              final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum Treatment implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
Cost("Cost", EGLColumnDataType.Currency, 0, false, null, false, 0, null, -1),
LocId("Loc Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Loc, -1),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
PetId("Pet Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Pet, -1),
PlanEntryId("Plan Entry Id", EGLColumnDataType.Int, 0, true, null, false, 0, ELookupType.PlanEntry, -1),
ScheduledDate("Scheduled Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
TreatmentDate("Treatment Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
TreatmentDesc("Treatment Description", EGLColumnDataType.String, 200, true, null, false, 1, null, -1),
TreatmentId("Treatment Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
TreatmentTypeId("Treatment Type Id", EGLColumnDataType.Int, 0, false, null, false, 0, null, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private Treatment(final String title, final EGLColumnDataType dataType,
                  final int decimalPlacesOrLength, final boolean nullable,
                  final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                  final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum TreatmentType implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
TreatmentTypeDesc("Treatment Type Description", EGLColumnDataType.String, 50, false, null, false, 0, null, -1),
TreatmentTypeId("Treatment Type Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
TreatmentTypeShortDesc("Treatment Type Short Description", EGLColumnDataType.String, 10, false, null, false, 1, null, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private TreatmentType(final String title, final EGLColumnDataType dataType,
                      final int decimalPlacesOrLength, final boolean nullable,
                      final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                      final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum ValueList implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
ValueListDesc("Value List Description", EGLColumnDataType.String, 50, false, null, false, 1, null, -1),
ValueList("Value List", EGLColumnDataType.String, 1000000, true, null, false, 0, null, -1),
ValueListId("Value List Id", EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
Version("Version", EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private ValueList(final String title, final EGLColumnDataType dataType,
                  final int decimalPlacesOrLength, final boolean nullable,
                  final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                  final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _dataType = dataType;
  _decimalPlacesOrLength = decimalPlacesOrLength;
  _nullable = nullable;
  _defaultValue = defaultValue;
  _primaryKey = primaryKey;
  _comboboxSeq = comboboxSeq;
  _lookupType = lookupType;
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
public int getDecimalPlacesOrLength() {
  return _decimalPlacesOrLength;
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
public boolean getPrimaryKey() {
  return _primaryKey;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
}