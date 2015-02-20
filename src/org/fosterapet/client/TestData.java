package org.fosterapet.client;
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
import java.util.List;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.Pet;
import org.fosterapet.shared.IDBEnums.PetType;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.db.GLListStore;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.db.GLRecordDef;
import org.greatlogic.glgwt.shared.IGLColumn;

public class TestData {
//--------------------------------------------------------------------------------------------------
private static void addRecordToList(final GLListStore listStore, final List<GLRecord> recordList,
                                    final GLRecord record) {
  if (listStore == null) {
    recordList.add(record);
  }
  else {
    listStore.add(record);
  }
}
//--------------------------------------------------------------------------------------------------
private static void clearList(final GLListStore listStore, final List<GLRecord> recordList) {
  if (listStore == null) {
    recordList.clear();
  }
  else {
    listStore.clear();
  }
}
//--------------------------------------------------------------------------------------------------
private static final String[] PetNamesAndSex = new String[] {"Angel,F", "Ashley,F", "Bandit,M",
    "Beau,M", "Bella,F", "Bo,M", "Boomer,M", "Bubba,M", "Buddy,M", "Buster,M", "Callie,F",
    "Calvin,M", "Cassie,F", "Champ,M", "Chelsea,F", "Chester,M", "Cleopatra,F", "Cody,M",
    "Cookie,F", "Cooper,M", "Dexter,M", "Diesel,M", "Dixie,F", "Duke,M", "Duncan,M", "Dusty,M",
    "Emily,F", "Emma,F", "Felix,M", "Female,F", "Fred,M", "Grace,F", "Guinness,M", "Haley,F",
    "Harry,M", "Hunter,M", "Isabella,F", "Jack,M", "Jasmine,F", "Joey,M", "Junior,M", "Kitty,F",
    "Kobe,M", "Leo,M", "Loki,U", "Lucy,F", "Maddie,F", "Mandy,F", "Marley,M", "Max,M", "Maximus,M",
    "Maxwell,M", "Maya,F", "Merlin,M", "Mia,F", "Mickey,M", "Mikey,M", "Millie,F", "Milo,U",
    "Misty,F", "Mocha,U", "Moose,M", "Morgan,U", "Murphy,M", "Nala,U", "Nikki,F", "Olivia,F",
    "Oreo,U", "Pebbles,F", "Penny,F", "Rex,M", "Roxie,F", "Rufus,M", "Sabrina,F", "Sadie,F",
    "Sam,U", "Samantha,F", "Sarah,F", "Sassy,U", "Scooter,M", "Sebastian,M", "Sheba,F", "Simba,M",
    "Snowball,U", "Socks,U", "Sophia,F", "Sophie,F", "Spencer,M", "Sunny,F", "Sylvester,M",
    "Taz,M", "Thomas,M", "Tinkerbell,F", "Toby,M", "Tommy,M", "Tucker,M", "Winston,M", "Ziggy,M",
    "Zoe,F", "Zoey,F"                        };
public static void loadPetTestData(final GLListStore listStore) {
  loadPetTestData(listStore, null);
}
public static void loadPetTestData(final List<GLRecord> recordList) {
  loadPetTestData(null, recordList);
}
private static void loadPetTestData(final GLListStore listStore, final List<GLRecord> recordList) {
  final IGLColumn[] columns = new IGLColumn[] {Pet.AdoptionFee, Pet.IntakeDate, //
      Pet.PetId, Pet.PetName, Pet.PetTypeId, Pet.Sex};
  final GLRecordDef recordDef = new GLRecordDef(EFAPTable.Pet, columns);
  clearList(listStore, recordList);
  int nextPetId = 1;
  for (final String petNameAndSex : PetNamesAndSex) {
    final String[] nameAndSex = petNameAndSex.split(",");
    final String intakeDate = GLClientUtil.dateAddDays("20130501", GLClientUtil.getRandomInt(365));
    final int hour = 6 + GLClientUtil.getRandomInt(12);
    final int minute = GLClientUtil.getRandomInt(4) * 15;
    final String intakeTime = (hour < 10 ? "0" : "") + hour + (minute < 10 ? "0" : "") + minute;
    final String fosterDate = GLClientUtil.dateAddDays(intakeDate, 60);
    final ArrayList<Object> valueList = new ArrayList<Object>(columns.length);
    valueList.add(GLClientUtil.getRandomInt(3000, 10000) / 100.0);
    valueList.add(fosterDate);
    valueList.add(intakeDate + intakeTime);
    valueList.add(nextPetId);
    valueList.add(nameAndSex[0]);
    valueList.add(GLClientUtil.getRandomInt(PetTypes.length) + 1);
    valueList.add(nameAndSex[1]);
    valueList.add(GLClientUtil.getRandomInt(2) == 0 ? "Y" : "N");
    final GLRecord record = new GLRecord(recordDef, valueList, null);
    addRecordToList(listStore, recordList, record);
    ++nextPetId;
  }
}
//--------------------------------------------------------------------------------------------------
private static final String[] PetTypes = new String[] {"Cat,Cat", "Dog,Dog"};
public static void loadPetTypeTestData(final GLListStore listStore) {
  loadPetTypeTestData(listStore, null);
}
public static void loadPetTypeTestData(final List<GLRecord> recordList) {
  loadPetTypeTestData(null, recordList);
}
private static void loadPetTypeTestData(final GLListStore listStore, final List<GLRecord> recordList) {
  final IGLColumn[] columns = new IGLColumn[] {PetType.PetTypeShortDesc, PetType.PetTypeDesc, //
      PetType.PetTypeId};
  final GLRecordDef recordDef = new GLRecordDef(EFAPTable.PetType, columns);
  clearList(listStore, recordList);
  int nextPetTypeId = 1;
  for (final String petType : PetTypes) {
    final String[] petTypeFields = petType.split(",");
    final ArrayList<Object> valueList = new ArrayList<Object>(columns.length);
    valueList.add(petTypeFields[0]);
    valueList.add(petTypeFields[1]);
    valueList.add(nextPetTypeId);
    final GLRecord record = new GLRecord(recordDef, valueList, null);
    addRecordToList(listStore, recordList, record);
    ++nextPetTypeId;
  }
}
//--------------------------------------------------------------------------------------------------
}