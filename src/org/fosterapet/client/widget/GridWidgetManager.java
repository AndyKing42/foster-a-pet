package org.fosterapet.client.widget;
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
import java.util.TreeMap;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.OrgPerson;
import org.fosterapet.shared.IDBEnums.Person;
import org.fosterapet.shared.IDBEnums.Pet;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.db.GLDBException;
import org.greatlogic.glgwt.client.widget.grid.GLGridWidget;
import org.greatlogic.glgwt.shared.GLRecordValidator;

public class GridWidgetManager {
//--------------------------------------------------------------------------------------------------
private static TreeMap<String, GridWidgetInfo> _gridWidgetInfoMap; /* grid name -> GLGridWidgetInfo */
//--------------------------------------------------------------------------------------------------
private static class GridWidgetInfo {
private final GLGridWidget _gridWidget;
private final boolean      _inlineEditing;
private final boolean      _rowLevelCommits;
private final boolean      _useCheckBoxSelectionModel;
private GridWidgetInfo(final GLGridWidget gridWidget, final boolean inlineEditing,
                       final boolean useCheckBoxSelectionModel, final boolean rowLevelCommits) {
  _gridWidget = gridWidget;
  _inlineEditing = inlineEditing;
  _useCheckBoxSelectionModel = useCheckBoxSelectionModel;
  _rowLevelCommits = rowLevelCommits;
}
}
//--------------------------------------------------------------------------------------------------
static {
  _gridWidgetInfoMap = new TreeMap<>();
}
//--------------------------------------------------------------------------------------------------
public static OrgPersonGridWidget getOrgPersonGrid(final String gridName) throws GLDBException {
  final OrgPersonGridWidget result;
  GridWidgetInfo gridWidgetInfo = _gridWidgetInfoMap.get(gridName);
  if (gridWidgetInfo == null) {
    final GLRecordValidator validator;
    validator = GLClientUtil.getValidators().getRecordValidator(EFAPTable.OrgPerson);
    result = new OrgPersonGridWidget(validator, false, true, false, OrgPerson.OrgId, //
                                     OrgPerson.PersonRoleId);
    gridWidgetInfo = new GridWidgetInfo(result, false, true, false);
    _gridWidgetInfoMap.put(gridName, gridWidgetInfo);
  }
  return (OrgPersonGridWidget)gridWidgetInfo._gridWidget;
}
//--------------------------------------------------------------------------------------------------
public static PersonGridWidget getPersonGrid(final String gridName) throws GLDBException {
  final GridWidgetInfo gridWidgetInfo = _gridWidgetInfoMap.get(gridName);
  boolean inlineEditing;
  boolean rowLevelCommits;
  boolean useCheckBoxSelectionModel;
  if (gridWidgetInfo == null) {
    inlineEditing = false;
    rowLevelCommits = true;
    useCheckBoxSelectionModel = true;
  }
  else {
    inlineEditing = gridWidgetInfo._inlineEditing;
    rowLevelCommits = gridWidgetInfo._rowLevelCommits;
    useCheckBoxSelectionModel = gridWidgetInfo._useCheckBoxSelectionModel;
  }
  return getPersonGrid(gridName, inlineEditing, useCheckBoxSelectionModel, rowLevelCommits);
}
//--------------------------------------------------------------------------------------------------
public static PersonGridWidget getPersonGrid(final String gridName, final boolean inlineEditing,
                                             final boolean useCheckBoxSelectionModel,
                                             final boolean rowLevelCommits) throws GLDBException {
  final PersonGridWidget result;
  GridWidgetInfo gridWidgetInfo = _gridWidgetInfoMap.get(gridName);
  if (gridWidgetInfo == null || gridWidgetInfo._inlineEditing != inlineEditing ||
      gridWidgetInfo._useCheckBoxSelectionModel != useCheckBoxSelectionModel ||
      gridWidgetInfo._rowLevelCommits != rowLevelCommits) {
    final GLRecordValidator validator;
    validator = GLClientUtil.getValidators().getRecordValidator(EFAPTable.Person);
    result = new PersonGridWidget(validator, inlineEditing, useCheckBoxSelectionModel, //
                                  rowLevelCommits, Person.DisplayName, Person.FirstName, //
                                  Person.LastName, Person.LoginName, Person.EmailAddress, //
                                  Person.DateOfBirth, Person.PhoneNumberMobile, //
                                  Person.PhoneNumberHome, Person.PhoneNumberOffice);
    gridWidgetInfo = new GridWidgetInfo(result, inlineEditing, useCheckBoxSelectionModel, //
                                        rowLevelCommits);
    _gridWidgetInfoMap.put(gridName, gridWidgetInfo);
  }
  return (PersonGridWidget)gridWidgetInfo._gridWidget;
}
//--------------------------------------------------------------------------------------------------
public static PetGridWidget getPetGrid(final String gridName) throws GLDBException {
  final GridWidgetInfo gridWidgetInfo = _gridWidgetInfoMap.get(gridName);
  boolean inlineEditing;
  boolean rowLevelCommits;
  boolean useCheckBoxSelectionModel;
  if (gridWidgetInfo == null) {
    inlineEditing = false;
    rowLevelCommits = true;
    useCheckBoxSelectionModel = true;
  }
  else {
    inlineEditing = gridWidgetInfo._inlineEditing;
    rowLevelCommits = gridWidgetInfo._rowLevelCommits;
    useCheckBoxSelectionModel = gridWidgetInfo._useCheckBoxSelectionModel;
  }
  return getPetGrid(gridName, inlineEditing, useCheckBoxSelectionModel, rowLevelCommits);
}
//--------------------------------------------------------------------------------------------------
public static PetGridWidget getPetGrid(final String gridName, final boolean inlineEditing,
                                       final boolean useCheckBoxSelectionModel,
                                       final boolean rowLevelCommits) throws GLDBException {
  final PetGridWidget result;
  GridWidgetInfo gridWidgetInfo = _gridWidgetInfoMap.get(gridName);
  if (gridWidgetInfo == null || gridWidgetInfo._inlineEditing != inlineEditing ||
      gridWidgetInfo._useCheckBoxSelectionModel != useCheckBoxSelectionModel ||
      gridWidgetInfo._rowLevelCommits != rowLevelCommits) {
    final GLRecordValidator validator;
    validator = GLClientUtil.getValidators().getRecordValidator(EFAPTable.Pet);
    result = new PetGridWidget(validator, inlineEditing, useCheckBoxSelectionModel, //
                               rowLevelCommits, Pet.PetName, Pet.PetTypeId, Pet.DateOfBirth, //
                               Pet.Sex, Pet.IntakeDate, Pet.AdoptionFee);
    gridWidgetInfo = new GridWidgetInfo(result, inlineEditing, useCheckBoxSelectionModel, //
                                        rowLevelCommits);
    _gridWidgetInfoMap.put(gridName, gridWidgetInfo);
  }
  return (PetGridWidget)gridWidgetInfo._gridWidget;
}
//--------------------------------------------------------------------------------------------------
}