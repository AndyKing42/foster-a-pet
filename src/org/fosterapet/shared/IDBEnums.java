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
import org.greatlogic.glgwt.client.db.IGLColumnInitializer;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLLookupType;
import org.greatlogic.glgwt.shared.IGLSharedEnums.EGLColumnDataType;
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
ValueListId(29, "ValueListId", EFAPTable.ValueList),
SessionTokenId(30, "SessionTokenId", EFAPTable.SessionToken);
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
SessionToken(SessionToken.class),
State(State.class),
Treatment(Treatment.class),
TreatmentType(TreatmentType.class),
ValueList(ValueList.class);
private TreeMap<String, IGLColumn>     _columnByColumnNameMap; // tableName.columnName -> IGLColumn
private final Class<? extends Enum<?>> _columnEnumClass;
private TreeMap<Integer, IGLColumn>    _comboboxColumnMap;
private IGLColumn                      _primaryKeyColumn;
private EFAPTable(final Class<? extends Enum<?>> columnEnumClass) {
  _columnEnumClass = columnEnumClass;
}
@Override
public IGLColumn findColumnUsingColumnName(final String columnName) {
  initializeColumnSettings();
  return _columnByColumnNameMap.get(name() + "." + columnName);
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
      _columnByColumnNameMap.put(name() + "." + column, column);
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
public void initializeNewRecord(final GLRecord record, final IGLColumnInitializer columnInitializer) {
  initializeColumnSettings();
  for (final IGLColumn column : _columnByColumnNameMap.values()) {
    final Object defaultValue = column.getDefaultValue();
    if (defaultValue != null) {
      record.set(column, defaultValue);
    }
    if (columnInitializer != null) {
      final Object value = columnInitializer.initializeColumn(column);
      if (value != null) {
        record.set(column, value);
      }
    }
  }
}
}
//--------------------------------------------------------------------------------------------------
public enum Address implements IGLColumn {
AddressId("AddressId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
AddressLine1("Address Line 1", null, EGLColumnDataType.String, 100, true, null, false, 0, null, -1),
AddressLine2("Address Line 2", null, EGLColumnDataType.String, 100, true, null, false, 0, null, -1),
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
CityId("City", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.City, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1),
ZIPCode("ZIP Code", null, EGLColumnDataType.String, 10, true, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private Address(final String title, final String toolTip, final EGLColumnDataType dataType,
                final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
                final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
                final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum Attribute implements IGLColumn {
AdditionalComments("Additional Comments", null, EGLColumnDataType.String, 2000, true, null, false, 0, null, -1),
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
AttributeDefId("Attribute Definition", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.AttributeDef, -1),
AttributeId("AttributeId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
AttributeSeq("Attribute Seq", null, EGLColumnDataType.Int, 0, false, null, false, 0, null, -1),
AttributeValue("Attribute Value", null, EGLColumnDataType.String, 2000, true, null, false, 0, null, -1),
CreatedByPersonId("Created By Person", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
EntityId("Entity", null, EGLColumnDataType.Int, 0, false, null, false, 0, null, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private Attribute(final String title, final String toolTip, final EGLColumnDataType dataType,
                  final int decimalPlacesOrLength, final boolean nullable,
                  final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                  final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum AttributeDef implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
AttributeDefId("AttributeDefId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
AttributeDefName("Attribute Def Name", null, EGLColumnDataType.String, 30, false, null, false, 1, null, -1),
AttributeDefType("Attribute Def Type", null, EGLColumnDataType.String, 3, false, null, false, 0, null, -1),
AutomaticallyCreateFlag("Automatically Create Flag", null, EGLColumnDataType.Boolean, 1, false, null, false, 0, null, -1),
DataType("Data Type", null, EGLColumnDataType.String, 10, false, null, false, 0, null, -1),
EntityTypeId("Entity Type", null, EGLColumnDataType.Int, 0, true, null, false, 0, ELookupType.EntityType, -1),
MaxOccurrences("Max Occurrences", null, EGLColumnDataType.Int, 0, false, null, false, 0, null, -1),
OrgId("Organization", null, EGLColumnDataType.Int, 0, false, null, false, 0, null, -1),
RequiredFlag("Required Flag", null, EGLColumnDataType.Boolean, 1, false, null, false, 0, null, -1),
ValueListId("Value List", null, EGLColumnDataType.Int, 0, true, null, false, 0, ELookupType.ValueList, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private AttributeDef(final String title, final String toolTip, final EGLColumnDataType dataType,
                     final int decimalPlacesOrLength, final boolean nullable,
                     final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                     final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum City implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
CityId("CityId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
CityName("City Name", null, EGLColumnDataType.String, 100, false, null, false, 1, null, -1),
StateId("State", null, EGLColumnDataType.Int, 0, false, null, false, 2, ELookupType.State, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private City(final String title, final String toolTip, final EGLColumnDataType dataType,
             final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
             final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
             final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum DBAudit implements IGLColumn {
AuditAction("Audit Action", null, EGLColumnDataType.String, 1, false, null, false, 0, null, -1),
AuditDate("Audit Date", null, EGLColumnDataType.DateTime, 0, false, null, false, 0, null, -1),
DBAuditId("Database Audit Id", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
NewData("New Data", null, EGLColumnDataType.String, 1000000, true, null, false, 0, null, -1),
OldData("Old Data", null, EGLColumnDataType.String, 1000000, true, null, false, 0, null, -1),
OrgId("Organization", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
PersonId("Person", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private DBAudit(final String title, final String toolTip, final EGLColumnDataType dataType,
                final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
                final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
                final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum DBUpdateNote implements IGLColumn {
AppliedDateTime("Applied Date/Time", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
DBRevNumber("DB Rev Number", null, EGLColumnDataType.String, 30, false, null, false, 1, null, -1),
DBUpdateDesc("DB Update Description", null, EGLColumnDataType.String, 2000, false, null, false, 0, null, -1),
DBUpdateNoteId("DBUpdateNoteId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
DevDateTime("Development Date/Time", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private DBUpdateNote(final String title, final String toolTip, final EGLColumnDataType dataType,
                     final int decimalPlacesOrLength, final boolean nullable,
                     final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                     final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum FosterHistory implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
FosterDateFinish("Foster Date Finish", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
FosterDateStart("Foster Date Start", null, EGLColumnDataType.DateTime, 0, false, null, false, 0, null, -1),
FosterHistoryId("FosterHistoryId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
LocId("Location", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Loc, -1),
OrgId("Organization", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
PetId("Pet", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Pet, -1),
PrimaryPersonId("Primary Person", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private FosterHistory(final String title, final String toolTip, final EGLColumnDataType dataType,
                      final int decimalPlacesOrLength, final boolean nullable,
                      final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                      final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum Loc implements IGLColumn {
AddressId("Address", null, EGLColumnDataType.Int, 0, true, null, false, 0, ELookupType.Address, -1),
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
LocDesc("Description", null, EGLColumnDataType.String, 100, true, null, false, 0, null, -1),
LocId("LocId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
LocShortDesc("Short Description", null, EGLColumnDataType.String, 30, false, null, false, 1, null, -1),
LocTypeId("Loc Type", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.LocType, -1),
OrgId("Organization", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private Loc(final String title, final String toolTip, final EGLColumnDataType dataType,
            final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
            final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
            final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum LocPerson implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
LocId("Location", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Loc, -1),
LocPersonId("Loc Person", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
OrgId("Organization", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
PersonId("Person", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
PrimaryFlag("Primary Flag", null, EGLColumnDataType.Boolean, 1, false, null, false, 0, null, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private LocPerson(final String title, final String toolTip, final EGLColumnDataType dataType,
                  final int decimalPlacesOrLength, final boolean nullable,
                  final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                  final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum LocType implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
LocTypeDesc("Description", null, EGLColumnDataType.String, 50, false, null, false, 0, null, -1),
LocTypeId("LocTypeId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
LocTypeShortDesc("Short Description", null, EGLColumnDataType.String, 10, false, null, false, 1, null, -1),
OrgId("Organization", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private LocType(final String title, final String toolTip, final EGLColumnDataType dataType,
                final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
                final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
                final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum NextId implements IGLColumn {
NextId("NextId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
NextIdName("Next Id Name", null, EGLColumnDataType.String, 30, false, null, false, 1, null, -1),
NextIdValue("Next Id Value", null, EGLColumnDataType.Int, 0, false, null, false, 0, null, -1),
TableName("Table Name", null, EGLColumnDataType.String, 30, true, null, false, 0, null, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private NextId(final String title, final String toolTip, final EGLColumnDataType dataType,
               final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
               final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
               final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum Org implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
OrgDesc("Description", null, EGLColumnDataType.String, 200, false, null, false, 0, null, -1),
OrgId("OrgId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
OrgName("Name", null, EGLColumnDataType.String, 50, false, null, false, 1, null, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private Org(final String title, final String toolTip, final EGLColumnDataType dataType,
            final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
            final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
            final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum OrgPerson implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
OrgId("Organization", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
OrgPersonId("OrgPersonId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
PersonId("Person", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
PersonRoleId("Role", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.PersonRole, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private OrgPerson(final String title, final String toolTip, final EGLColumnDataType dataType,
                  final int decimalPlacesOrLength, final boolean nullable,
                  final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                  final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum Person implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
CurrentOrgId("Current Org", null, EGLColumnDataType.Int, 0, true, null, false, 0, ELookupType.Org, -1),
DateOfBirth("Date Of Birth", null, EGLColumnDataType.Date, 0, true, null, false, 0, null, -1),
DisplayName("Display Name", null, EGLColumnDataType.String, 50, false, null, false, 1, null, -1),
EmailAddress("Email Address", null, EGLColumnDataType.String, 100, true, null, false, 0, null, -1),
FirstName("First Name", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1),
LastName("Last Name", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1),
LoginName("Login Name", null, EGLColumnDataType.String, 100, true, null, false, 0, null, -1),
PasswordHash("Password Hash", null, EGLColumnDataType.String, 200, true, null, false, 0, null, -1),
PersonId("PersonId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
PhoneNumberHome("Home Phone", null, EGLColumnDataType.String, 50, true, null, false, 0, null, -1),
PhoneNumberMobile("Mobile Phone", null, EGLColumnDataType.String, 50, true, null, false, 0, null, -1),
PhoneNumberOffice("Office Phone", null, EGLColumnDataType.String, 50, true, null, false, 0, null, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private Person(final String title, final String toolTip, final EGLColumnDataType dataType,
               final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
               final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
               final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum PersonRelationship implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
PersonId1("Person 1", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
PersonId2("Person 2", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
PersonRelationshipId("PersonRelationshipId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
RelationshipOf1To2("Relationship Of 1 To 2", null, EGLColumnDataType.String, 20, false, null, false, 0, ELookupType.PersonRelationship, -1),
RelationshipOf2To1("Relationship Of 2 To 1", null, EGLColumnDataType.String, 20, false, null, false, 0, ELookupType.PersonRelationship, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private PersonRelationship(final String title, final String toolTip,
                           final EGLColumnDataType dataType, final int decimalPlacesOrLength,
                           final boolean nullable, final String defaultValue,
                           final boolean primaryKey, final int comboboxSeq,
                           final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum PersonRole implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
PersonRoleId("PersonRoleId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
RoleDesc("Description", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1),
RoleShortDesc("Short Description", null, EGLColumnDataType.String, 10, false, null, false, 1, null, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private PersonRole(final String title, final String toolTip, final EGLColumnDataType dataType,
                   final int decimalPlacesOrLength, final boolean nullable,
                   final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                   final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum Pet implements IGLColumn {
AdoptionFee("Adoption Fee", null, EGLColumnDataType.Currency, 0, false, "0", false, 0, null, -1),
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
DateOfBirth("Date Of Birth", null, EGLColumnDataType.Date, 0, false, null, false, 0, null, -1),
IntakeDate("Intake Date", null, EGLColumnDataType.Date, 0, false, null, false, 0, null, -1),
OrgId("Organization", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
PetId("PetId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
PetName("Pet Name", null, EGLColumnDataType.String, 50, false, null, false, 1, null, -1),
PetTypeId("Pet Type", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.PetType, -1),
Sex("Sex", null, EGLColumnDataType.String, 1, false, "U", false, 0, ELookupType.Sex, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private Pet(final String title, final String toolTip, final EGLColumnDataType dataType,
            final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
            final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
            final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum PetType implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
OrgId("Organization", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
PetTypeDesc("Description", null, EGLColumnDataType.String, 50, false, null, false, 0, null, -1),
PetTypeId("PetTypeId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
PetTypeShortDesc("Short Description", null, EGLColumnDataType.String, 10, false, null, false, 1, null, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private PetType(final String title, final String toolTip, final EGLColumnDataType dataType,
                final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
                final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
                final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum PlanEntry implements IGLColumn {
ActionDate("Action Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
LocId("Location", null, EGLColumnDataType.Int, 0, true, null, false, 0, ELookupType.Loc, -1),
PetId("Pet", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Pet, -1),
PlanEntryId("PlanEntryId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
PlanEntryNotes("Plan Entry Notes", null, EGLColumnDataType.String, 2000, true, null, false, 0, null, -1),
ScheduledDate("Scheduled Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
TreatmentTypeId("Treatment Type", null, EGLColumnDataType.Int, 0, true, null, false, 0, ELookupType.TreatmentType, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private PlanEntry(final String title, final String toolTip, final EGLColumnDataType dataType,
                  final int decimalPlacesOrLength, final boolean nullable,
                  final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                  final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum PlanPerson implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
Notes("Notes", null, EGLColumnDataType.String, 2000, true, null, false, 0, null, -1),
PersonId("Person", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
PlanEntryId("Plan Entry", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.PlanEntry, -1),
PlanPersonId("PlanPersonId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
ReminderType("Reminder Type", null, EGLColumnDataType.String, 10, false, null, false, 0, null, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private PlanPerson(final String title, final String toolTip, final EGLColumnDataType dataType,
                   final int decimalPlacesOrLength, final boolean nullable,
                   final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                   final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum PlanTemplate implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
OrgId("Organization", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
PlanTemplateDesc("Description", null, EGLColumnDataType.String, 2000, true, null, false, 0, null, -1),
PlanTemplateId("PlanTemplateId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
PlanTemplateName("Name", null, EGLColumnDataType.String, 50, false, null, false, 1, null, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private PlanTemplate(final String title, final String toolTip, final EGLColumnDataType dataType,
                     final int decimalPlacesOrLength, final boolean nullable,
                     final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                     final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum PlanTemplateEntry implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
OffsetNumberOfDays("Offset Number Of Days", null, EGLColumnDataType.Int, 0, true, null, false, 0, null, -1),
PlanTemplateEntryDesc("Description", null, EGLColumnDataType.String, 50, false, null, false, 1, null, -1),
PlanTemplateEntryId("PlanTemplateEntryId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
PlanTemplateId("Plan Template", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.PlanTemplate, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private PlanTemplateEntry(final String title, final String toolTip,
                          final EGLColumnDataType dataType, final int decimalPlacesOrLength,
                          final boolean nullable, final String defaultValue,
                          final boolean primaryKey, final int comboboxSeq,
                          final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum PlanTemplateTreatment implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
LocId("Location", null, EGLColumnDataType.Int, 0, true, null, false, 0, ELookupType.Loc, -1),
PlanTemplateEntryId("Plan Template Entry", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.PlanTemplateEntry, -1),
PlanTemplateTreatmentId("PlanTemplateTreatmentId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
TreatmentTypeId("Treatment Type", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.TreatmentType, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private PlanTemplateTreatment(final String title, final String toolTip,
                              final EGLColumnDataType dataType, final int decimalPlacesOrLength,
                              final boolean nullable, final String defaultValue,
                              final boolean primaryKey, final int comboboxSeq,
                              final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum SearchDef implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
OrgId("Organization", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
OwnerPersonId("Owner Person", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1),
PublicFlag("Public Flag", null, EGLColumnDataType.Boolean, 1, false, null, false, 0, null, -1),
SearchDefDesc("Description", null, EGLColumnDataType.String, 100, true, null, false, 0, null, -1),
SearchDefId("SearchDefId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
SearchDefName("Name", null, EGLColumnDataType.String, 30, true, null, false, 1, null, -1),
SearchDefType("Search Def Type", null, EGLColumnDataType.String, 20, false, null, false, 0, null, -1),
SortColumns("Sort Columns", null, EGLColumnDataType.String, 200, true, null, false, 0, null, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private SearchDef(final String title, final String toolTip, final EGLColumnDataType dataType,
                  final int decimalPlacesOrLength, final boolean nullable,
                  final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                  final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum SearchDefDetail implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
SearchDefDetailId("SearchDefDetailId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
SearchDefId("Search Def", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.SearchDef, -1),
Value1("Value 1", null, EGLColumnDataType.String, 50, true, null, false, 0, null, -1),
Value2("Value 2", null, EGLColumnDataType.String, 50, true, null, false, 0, null, -1),
Value3("Value 3", null, EGLColumnDataType.String, 50, true, null, false, 0, null, -1),
ValueList("Value List", null, EGLColumnDataType.String, 1000000, true, null, false, 0, null, -1),
ValueName("Name", null, EGLColumnDataType.String, 50, false, null, false, 1, null, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private SearchDefDetail(final String title, final String toolTip, final EGLColumnDataType dataType,
                        final int decimalPlacesOrLength, final boolean nullable,
                        final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                        final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum SessionToken implements IGLColumn {
ExpirationTime("Expiration Time", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
SessionToken("Session Token", null, EGLColumnDataType.String, 100, false, null, false, 1, null, -1),
SessionTokenId("SesionTokenId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
UserId("User Id", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Person, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private SessionToken(final String title, final String toolTip, final EGLColumnDataType dataType,
                     final int decimalPlacesOrLength, final boolean nullable,
                     final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                     final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum State implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
StateAbbrev("Abbreviation", null, EGLColumnDataType.String, 2, false, null, false, 1, null, -1),
StateId("StateId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
StateName("Name", null, EGLColumnDataType.String, 50, false, null, false, 0, null, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private State(final String title, final String toolTip, final EGLColumnDataType dataType,
              final int decimalPlacesOrLength, final boolean nullable, final String defaultValue,
              final boolean primaryKey, final int comboboxSeq, final IGLLookupType lookupType,
              final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum Treatment implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
Cost("Cost", null, EGLColumnDataType.Currency, 0, false, null, false, 0, null, -1),
LocId("Location", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Loc, -1),
OrgId("Organization", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
PetId("Pet", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Pet, -1),
PlanEntryId("Plan Entry", null, EGLColumnDataType.Int, 0, true, null, false, 0, ELookupType.PlanEntry, -1),
ScheduledDate("Scheduled Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
TreatmentDate("Treatment Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
TreatmentDesc("Description", null, EGLColumnDataType.String, 200, true, null, false, 1, null, -1),
TreatmentId("TreatmentId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
TreatmentTypeId("Treatment Type", null, EGLColumnDataType.Int, 0, false, null, false, 0, null, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private Treatment(final String title, final String toolTip, final EGLColumnDataType dataType,
                  final int decimalPlacesOrLength, final boolean nullable,
                  final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                  final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum TreatmentType implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
OrgId("Organization", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
TreatmentTypeDesc("Description", null, EGLColumnDataType.String, 50, false, null, false, 0, null, -1),
TreatmentTypeId("TreatmentTypeId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
TreatmentTypeShortDesc("Short Description", null, EGLColumnDataType.String, 10, false, null, false, 1, null, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private TreatmentType(final String title, final String toolTip, final EGLColumnDataType dataType,
                      final int decimalPlacesOrLength, final boolean nullable,
                      final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                      final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
public enum ValueList implements IGLColumn {
ArchiveDate("Archive Date", null, EGLColumnDataType.DateTime, 0, true, null, false, 0, null, -1),
OrgId("Organization", null, EGLColumnDataType.Int, 0, false, null, false, 0, ELookupType.Org, -1),
ValueListDesc("Description", null, EGLColumnDataType.String, 50, false, null, false, 1, null, -1),
ValueList("Value List", null, EGLColumnDataType.String, 1000000, true, null, false, 0, null, -1),
ValueListId("ValueListId", null, EGLColumnDataType.Int, 0, false, null, true, 0, null, -1),
Version("Version", null, EGLColumnDataType.String, 30, false, null, false, 0, null, -1);
private final int               _comboboxSeq;
private final EGLColumnDataType _dataType;
private final int               _decimalPlacesOrLength;
private final int               _defaultGridColumnWidth;
private final Object            _defaultValue;
private final IGLLookupType     _lookupType;
private final boolean           _nullable;
private final boolean           _primaryKey;
private final String            _title;
private final String            _toolTip;
private ValueList(final String title, final String toolTip, final EGLColumnDataType dataType,
                  final int decimalPlacesOrLength, final boolean nullable,
                  final String defaultValue, final boolean primaryKey, final int comboboxSeq,
                  final IGLLookupType lookupType, final int defaultGridColumnWidth) {
  _title = title;
  _toolTip = toolTip;
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
@Override
public String getToolTip() {
  return _toolTip;
}
}
//--------------------------------------------------------------------------------------------------
}