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
private void createColumnByColumnNameMap() {
  if (_columnByColumnNameMap == null) {
    _columnByColumnNameMap = new TreeMap<>();
    _comboboxColumnMap = new TreeMap<>();
    IGLColumn possiblePrimaryKeyColumn = null;
    for (final Enum<?> columnEnumConstant : _columnEnumClass.getEnumConstants()) {
      final IGLColumn column = (IGLColumn)columnEnumConstant;
      _columnByColumnNameMap.put(column.toString(), column);
      if (column.getPrimaryKey() > 0) {
        _primaryKeyColumnMap.put(column.getPrimaryKey(), column);
      }
      if (column.getComboboxSeq() > 0) {
        _comboboxColumnMap.put(column.getComboboxSeq(), column);
      }
      if (possiblePrimaryKeyColumn == null || column.toString().equalsIgnoreCase(name() + "Id")) {
        possiblePrimaryKeyColumn = column;
      }
    }
    if (_primaryKeyColumnMap.size() == 0) {
      _primaryKeyColumnMap.put(1, possiblePrimaryKeyColumn);
    }
    if (_comboboxColumnMap.size() == 0) {
      _comboboxColumnMap.put(1, primaryKeyColumn);
    }
  }
}
@Override
public IGLColumn findColumnUsingColumnName(final String columnName) {
  createColumnByColumnNameMap();
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
  createColumnByColumnNameMap();
  return _columnByColumnNameMap.values();
}
@Override
public TreeMap<Integer, IGLColumn> getComboboxColumnMap() {
  createColumnByColumnNameMap();
  return _comboboxColumnMap;
}
@Override
public String getDataSourceName() {
  return null;
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
public enum Address implements IGLColumn {
AddressId("Address Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
AddressLine1("Address Line 1", EGLColumnDataType.String, 100, true, null, 0, 0, null, 0),
AddressLine2("Address Line 2", EGLColumnDataType.String, 100, true, null, 0, 0, null, 0),
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
CityId("City Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.City, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0),
ZIPCode("ZIP Code", EGLColumnDataType.String, 10, true, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum Attribute implements IGLColumn {
AdditionalComments("Additional Comments", EGLColumnDataType.String, 2000, true, null, 0, 0, null, 0),
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
AttributeDefId("Attribute Def Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.AttributeDef, 0),
AttributeId("Attribute Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
AttributeSeq("Attribute Seq", EGLColumnDataType.Int, 0, false, null, 0, 0, null, 0),
AttributeValue("Attribute Value", EGLColumnDataType.String, 2000, true, null, 0, 0, null, 0),
CreatedByPersonId("Created By Person Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Person, 0),
EntityId("Entity Id", EGLColumnDataType.Int, 0, false, null, 0, 0, null, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum AttributeDef implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
AttributeDefId("Attribute Def Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
AttributeDefName("Attribute Def Name", EGLColumnDataType.String, 30, false, null, 0, 1, null, 0),
AttributeDefType("Attribute Def Type", EGLColumnDataType.String, 3, false, null, 0, 0, null, 0),
AutomaticallyCreateFlag("Automatically Create Flag", EGLColumnDataType.Boolean, 1, false, null, 0, 0, null, 0),
DataType("Data Type", EGLColumnDataType.String, 10, false, null, 0, 0, null, 0),
EntityTypeId("Entity Type Id", EGLColumnDataType.Int, 0, true, null, 0, 0, ELookupType.EntityType, 0),
MaxOccurrences("Max Occurrences", EGLColumnDataType.Int, 0, false, null, 0, 0, null, 0),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, 0, 0, null, 0),
RequiredFlag("Required Flag", EGLColumnDataType.Boolean, 1, false, null, 0, 0, null, 0),
ValueListId("Value List Id", EGLColumnDataType.Int, 0, true, null, 0, 0, ELookupType.ValueList, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum City implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
CityId("City Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
CityName("City Name", EGLColumnDataType.String, 100, false, null, 0, 1, null, 0),
StateId("State Id", EGLColumnDataType.Int, 0, false, null, 0, 2, ELookupType.State, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum DBAudit implements IGLColumn {
AuditAction("Audit Action", EGLColumnDataType.String, 1, false, null, 0, 0, null, 0),
AuditDate("Audit Date", EGLColumnDataType.DateTime, 0, false, null, 0, 0, null, 0),
DBAuditId("D B Audit Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
NewData("New Data", EGLColumnDataType.String, 1000000, true, null, 0, 0, null, 0),
OldData("Old Data", EGLColumnDataType.String, 1000000, true, null, 0, 0, null, 0),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Org, 0),
PersonId("Person Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Person, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum DBUpdateNote implements IGLColumn {
AppliedDateTime("Applied Date/Time", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
DBRevNumber("DB Rev Number", EGLColumnDataType.String, 30, false, null, 0, 1, null, 0),
DBUpdateDesc("DB Update Description", EGLColumnDataType.String, 2000, false, null, 0, 0, null, 0),
DBUpdateNoteId("DB Update Note Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
DevDateTime("Development Date/Time", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum FosterHistory implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
FosterDateFinish("Foster Date Finish", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
FosterDateStart("Foster Date Start", EGLColumnDataType.DateTime, 0, false, null, 0, 0, null, 0),
FosterHistoryId("Foster History Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
LocId("Loc Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Loc, 0),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Org, 0),
PetId("Pet Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Pet, 0),
PrimaryPersonId("Primary Person Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Person, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum Loc implements IGLColumn {
AddressId("Address Id", EGLColumnDataType.Int, 0, true, null, 0, 0, ELookupType.Address, 0),
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
LocDesc("Loc Description", EGLColumnDataType.String, 100, true, null, 0, 0, null, 0),
LocId("Loc Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
LocShortDesc("Loc Short Description", EGLColumnDataType.String, 30, false, null, 0, 1, null, 0),
LocTypeId("Loc Type Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.LocType, 0),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Org, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum LocPerson implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
LocId("Loc Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Loc, 0),
LocPersonId("Loc Person Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Org, 0),
PersonId("Person Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Person, 0),
PrimaryFlag("Primary Flag", EGLColumnDataType.Boolean, 1, false, null, 0, 0, null, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum LocType implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
LocTypeDesc("Loc Type Description", EGLColumnDataType.String, 50, false, null, 0, 0, null, 0),
LocTypeId("Loc Type Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
LocTypeShortDesc("Loc Type Short Description", EGLColumnDataType.String, 10, false, null, 0, 1, null, 0),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Org, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum NextId implements IGLColumn {
NextId("Next Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
NextIdName("Next Id Name", EGLColumnDataType.String, 30, false, null, 0, 1, null, 0),
NextIdValue("Next Id Value", EGLColumnDataType.Int, 0, false, null, 0, 0, null, 0),
TableName("Table Name", EGLColumnDataType.String, 30, true, null, 0, 0, null, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum Org implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
OrgDesc("Org Description", EGLColumnDataType.String, 200, false, null, 0, 0, null, 0),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, 1, 0, ELookupType.Org, 0),
OrgName("Org Name", EGLColumnDataType.String, 50, false, null, 0, 1, null, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum OrgPerson implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Org, 0),
OrgPersonId("Org Person Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
PersonId("Person Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Person, 0),
PersonRoleId("Person Role Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.PersonRole, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum Person implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
CurrentOrgId("Current Org Id", EGLColumnDataType.Int, 0, true, null, 0, 0, ELookupType.Org, 0),
DateOfBirth("Date Of Birth", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
DisplayName("Display Name", EGLColumnDataType.String, 50, false, null, 0, 1, null, 0),
EmailAddress("Email Address", EGLColumnDataType.String, 100, true, null, 0, 0, null, 0),
FirstName("First Name", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0),
LastName("Last Name", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0),
LoginName("Login Name", EGLColumnDataType.String, 100, true, null, 0, 0, null, 0),
PasswordHash("Password Hash", EGLColumnDataType.String, 200, true, null, 0, 0, null, 0),
PersonId("Person Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
PhoneNumberHome("Phone Number Home", EGLColumnDataType.String, 50, true, null, 0, 0, null, 0),
PhoneNumberMobile("Phone Number Mobile", EGLColumnDataType.String, 50, true, null, 0, 0, null, 0),
PhoneNumberOffice("Phone Number Office", EGLColumnDataType.String, 50, true, null, 0, 0, null, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum PersonRelationship implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
PersonId1("Person Id 1", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Person, 0),
PersonId2("Person Id 2", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Person, 0),
PersonRelationshipId("Person Relationship Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
RelationshipOf1To2("Relationship Of 1 To 2", EGLColumnDataType.String, 20, false, null, 0, 0, ELookupType.PersonRelationship, 0),
RelationshipOf2To1("Relationship Of 2 To 1", EGLColumnDataType.String, 20, false, null, 0, 0, ELookupType.PersonRelationship, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum PersonRole implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
PersonRoleId("Person Role Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
RoleDesc("Role Description", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0),
RoleShortDesc("Role Short Description", EGLColumnDataType.String, 10, false, null, 0, 1, null, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum Pet implements IGLColumn {
AdoptionFee("Adoption Fee", EGLColumnDataType.Currency, 0, false, "0", 0, 0, null, 100),
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 125),
DateOfBirth("Date Of Birth", EGLColumnDataType.Date, 0, false, null, 0, 0, null, 100),
IntakeDate("Intake Date", EGLColumnDataType.DateTime, 0, false, null, 0, 0, null, 125),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Org, 50),
PetId("Pet Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 50),
PetName("Pet Name", EGLColumnDataType.String, 50, false, null, 0, 1, null, 80),
PetTypeId("Pet Type Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.PetType, 80),
Sex("Sex", EGLColumnDataType.String, 1, false, "U", 0, 0, ELookupType.Sex, 50),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 50);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum PetType implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Org, 0),
PetTypeDesc("Pet Type Description", EGLColumnDataType.String, 50, false, null, 0, 0, null, 0),
PetTypeId("Pet Type Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
PetTypeShortDesc("Pet Type Short Description", EGLColumnDataType.String, 10, false, null, 0, 1, null, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum PlanEntry implements IGLColumn {
ActionDate("Action Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
LocId("Loc Id", EGLColumnDataType.Int, 0, true, null, 0, 0, ELookupType.Loc, 0),
PetId("Pet Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Pet, 0),
PlanEntryId("Plan Entry Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
PlanEntryNotes("Plan Entry Notes", EGLColumnDataType.String, 2000, true, null, 0, 0, null, 0),
ScheduledDate("Scheduled Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
TreatmentTypeId("Treatment Type Id", EGLColumnDataType.Int, 0, true, null, 0, 0, ELookupType.TreatmentType, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum PlanPerson implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
Notes("Notes", EGLColumnDataType.String, 2000, true, null, 0, 0, null, 0),
PersonId("Person Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Person, 0),
PlanEntryId("Plan Entry Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.PlanEntry, 0),
PlanPersonId("Plan Person Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
ReminderType("Reminder Type", EGLColumnDataType.String, 10, false, null, 0, 0, null, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum PlanTemplate implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Org, 0),
PlanTemplateDesc("Plan Template Description", EGLColumnDataType.String, 2000, true, null, 0, 0, null, 0),
PlanTemplateId("Plan Template Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
PlanTemplateName("Plan Template Name", EGLColumnDataType.String, 50, false, null, 0, 1, null, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum PlanTemplateEntry implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
OffsetNumberOfDays("Offset Number Of Days", EGLColumnDataType.Int, 0, true, null, 0, 0, null, 0),
PlanTemplateEntryDesc("Plan Template Entry Description", EGLColumnDataType.String, 50, false, null, 0, 1, null, 0),
PlanTemplateEntryId("Plan Template Entry Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
PlanTemplateId("Plan Template Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.PlanTemplate, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum PlanTemplateTreatment implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
LocId("Loc Id", EGLColumnDataType.Int, 0, true, null, 0, 0, ELookupType.Loc, 0),
PlanTemplateEntryId("Plan Template Entry Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.PlanTemplateEntry, 0),
PlanTemplateTreatmentId("Plan Template Treatment Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
TreatmentTypeId("Treatment Type Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.TreatmentType, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum SearchDef implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Org, 0),
OwnerPersonId("Owner Person Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Person, 0),
PublicFlag("Public Flag", EGLColumnDataType.Boolean, 1, false, null, 0, 0, null, 0),
SearchDefDesc("Search Def Description", EGLColumnDataType.String, 100, true, null, 0, 0, null, 0),
SearchDefId("Search Def Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
SearchDefName("Search Def Name", EGLColumnDataType.String, 30, true, null, 0, 1, null, 0),
SearchDefType("Search Def Type", EGLColumnDataType.String, 20, false, null, 0, 0, null, 0),
SortColumns("Sort Columns", EGLColumnDataType.String, 200, true, null, 0, 0, null, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum SearchDefDetail implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
SearchDefDetailId("Search Def Detail Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
SearchDefId("Search Def Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.SearchDef, 0),
Value1("Value 1", EGLColumnDataType.String, 50, true, null, 0, 0, null, 0),
Value2("Value 2", EGLColumnDataType.String, 50, true, null, 0, 0, null, 0),
Value3("Value 3", EGLColumnDataType.String, 50, true, null, 0, 0, null, 0),
ValueList("Value List", EGLColumnDataType.String, 1000000, true, null, 0, 0, null, 0),
ValueName("Value Name", EGLColumnDataType.String, 50, false, null, 0, 1, null, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum State implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
StateAbbrev("State Abbreviation", EGLColumnDataType.String, 2, false, null, 0, 1, null, 0),
StateId("State Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
StateName("State Name", EGLColumnDataType.String, 50, false, null, 0, 0, null, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum Treatment implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
Cost("Cost", EGLColumnDataType.Currency, 0, false, null, 0, 0, null, 0),
LocId("Loc Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Loc, 0),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Org, 0),
PetId("Pet Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Pet, 0),
PlanEntryId("Plan Entry Id", EGLColumnDataType.Int, 0, true, null, 0, 0, ELookupType.PlanEntry, 0),
ScheduledDate("Scheduled Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
TreatmentDate("Treatment Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
TreatmentDesc("Treatment Description", EGLColumnDataType.String, 200, true, null, 0, 1, null, 0),
TreatmentId("Treatment Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
TreatmentTypeId("Treatment Type Id", EGLColumnDataType.Int, 0, false, null, 0, 0, null, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum TreatmentType implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, 0, 0, null, 0),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Org, 0),
TreatmentTypeDesc("Treatment Type Description", EGLColumnDataType.String, 50, false, null, 0, 0, null, 0),
TreatmentTypeId("Treatment Type Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
TreatmentTypeShortDesc("Treatment Type Short Description", EGLColumnDataType.String, 10, false, null, 0, 1, null, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
public enum ValueList implements IGLColumn {
ArchiveDate("Archive Date", EGLColumnDataType.DateTime, 0, true, null, false, 0, null, 0),
OrgId("Org Id", EGLColumnDataType.Int, 0, false, null, 0, 0, ELookupType.Org, 0),
ValueListDesc("Value List Description", EGLColumnDataType.String, 50, false, null, 0, 1, null, 0),
ValueList("Value List", EGLColumnDataType.String, 1000000, true, null, 0, 0, null, 0),
ValueListId("Value List Id", EGLColumnDataType.Int, 0, false, null, 1, 0, null, 0),
Version("Version", EGLColumnDataType.String, 30, false, null, 0, 0, null, 0);
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
  _primaryKeySeq = primaryKeySeq;
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
  return _primaryKeySeq;
}
@Override
public String getTitle() {
  return _title;
}
}
//--------------------------------------------------------------------------------------------------
}