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
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.greatlogic.glgwt.shared.IGLLookupType;
import org.greatlogic.glgwt.shared.IGLTable;

public interface IFAPEnums {
//--------------------------------------------------------------------------------------------------
public enum ELookupType implements IGLLookupType {
Address(EFAPTable.Address),
Attribute(EFAPTable.Attribute),
AttributeDef(EFAPTable.AttributeDef),
City(EFAPTable.City),
DBAudit(EFAPTable.DBAudit),
DBUpdateNote(EFAPTable.DBUpdateNote),
EntityType(null),
FosterHistory(EFAPTable.FosterHistory),
Loc(EFAPTable.Loc),
LocPerson(EFAPTable.LocPerson),
LocType(EFAPTable.LocType),
NextId(EFAPTable.NextId),
Org(EFAPTable.Org),
OrgPerson(EFAPTable.OrgPerson),
Person(EFAPTable.Person),
PersonRelationship(EFAPTable.PersonRelationship),
PersonRole(EFAPTable.PersonRole),
Pet(EFAPTable.Pet),
PetType(EFAPTable.PetType),
PlanEntry(EFAPTable.PlanEntry),
PlanPerson(EFAPTable.PlanPerson),
PlanTemplate(EFAPTable.PlanTemplate),
PlanTemplateEntry(EFAPTable.PlanTemplateEntry),
PlanTemplateTreatment(EFAPTable.PlanTemplateTreatment),
SearchDef(EFAPTable.SearchDef),
SearchDefDetail(EFAPTable.SearchDefDetail),
Sex(null),
State(EFAPTable.State),
Treatment(EFAPTable.Treatment),
TreatmentType(EFAPTable.TreatmentType),
ValueList(EFAPTable.ValueList);
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