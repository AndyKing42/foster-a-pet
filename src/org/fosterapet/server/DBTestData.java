package org.fosterapet.server;
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
import java.util.List;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.Org;
import org.fosterapet.shared.IDBEnums.Pet;
import org.fosterapet.shared.IDBEnums.PetType;
import org.fosterapet.shared.IFAPEnums.ETestDataOption;
import org.greatlogic.glgwt.server.GLServerUtil;
import com.google.common.collect.Lists;
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLDBUtil;
import com.greatlogic.glbase.gldb.GLSQL;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.GLUtil;

class DBTestData {
//--------------------------------------------------------------------------------------------------
public static void processRequest(final String testDataOptionString) throws GLDBException {
  final ETestDataOption testDataOption = ETestDataOption.lookup(testDataOptionString);
  switch (testDataOption) {
    case Reload:
      reload();
      break;
    case Unknown:
      GLLog.major("Unknown test data option:" + testDataOptionString);
      break;
  }
}
//--------------------------------------------------------------------------------------------------
private static void reload() throws GLDBException {
  reloadOrgs();
  reloadPetTypes();
  reloadPets();
}
//--------------------------------------------------------------------------------------------------
private static final String[] Orgs = new String[] {"1,Organization 1,The first organization"};
private static void reloadOrgs() throws GLDBException {
  truncate(EFAPTable.Org);
  for (final String org : Orgs) {
    final String[] orgFields = org.split(",");
    final GLSQL orgSQL = GLSQL.insert(EFAPTable.Org.name(), false);
    orgSQL.setValue(Org.OrgDesc.name(), orgFields[2]);
    orgSQL.setValue(Org.OrgId.name(), GLUtil.stringToInt(orgFields[0]));
    orgSQL.setValue(Org.OrgName.name(), orgFields[1]);
    orgSQL.setValue(Org.Version.name(), GLServerUtil.generateVersion());
    orgSQL.execute();
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
private static void reloadPets() throws GLDBException {
  truncate(EFAPTable.Pet);
  final List<Integer> petTypeIdList = reloadPetsGetPetTypeIdList();
  for (final String petNameAndSex : PetNamesAndSex) {
    final GLSQL petSQL = GLSQL.insert(EFAPTable.Pet.name(), false);
    final String[] nameAndSex = petNameAndSex.split(",");
    final String intakeDate = GLUtil.dateAddDays("20130501", GLUtil.getRandomInt(365));
    final int hour = 6 + GLUtil.getRandomInt(12);
    final int minute = GLUtil.getRandomInt(4) * 15;
    final String intakeTime = (hour < 10 ? "0" : "") + hour + (minute < 10 ? "0" : "") + //
                              minute + "00";
    final String dateOfBirth = GLUtil.dateAddDays(intakeDate, -GLUtil.getRandomInt(10, 4000));
    petSQL.setValue(Pet.AdoptionFee.name(), GLUtil.getRandomInt(3000, 10000) / 100.0);
    petSQL.setValue(Pet.DateOfBirth.name(), dateOfBirth);
    petSQL.setValue(Pet.IntakeDate.name(), intakeDate + intakeTime);
    petSQL.setValue(Pet.OrgId.name(), 1);
    petSQL.setValue(Pet.PetId.name(), GLServerUtil.getNextIdValue(EFAPTable.Pet + "Id", 1));
    petSQL.setValue(Pet.PetName.name(), nameAndSex[0]);
    petSQL.setValue(Pet.PetTypeId.name(),
                    petTypeIdList.get(GLUtil.getRandomInt(petTypeIdList.size())));
    petSQL.setValue(Pet.Sex.name(), nameAndSex[1]);
    petSQL.setValue(Pet.Version.name(), GLServerUtil.generateVersion());
    petSQL.execute(false);
  }
}
//--------------------------------------------------------------------------------------------------
private static List<Integer> reloadPetsGetPetTypeIdList() throws GLDBException {
  final List<Integer> result = Lists.newArrayList();
  final GLSQL petTypeSQL = GLSQL.select();
  petTypeSQL.from(EFAPTable.PetType.name());
  petTypeSQL.open();
  try {
    while (petTypeSQL.next(false)) {
      result.add(petTypeSQL.asInt(PetType.PetTypeId.name()));
    }
  }
  finally {
    petTypeSQL.close();
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
private static final String[] PetTypes = new String[] {"Cat,Cat", "Dog,Dog", "Hedgedog,Hedgehog",
    "Invisible Pink Unicorn,IPU"       };
private static void reloadPetTypes() throws GLDBException {
  truncate(EFAPTable.PetType);
  for (final String petType : PetTypes) {
    final String[] petTypeFields = petType.split(",");
    final GLSQL petTypeSQL = GLSQL.insert(EFAPTable.PetType.name(), false);
    petTypeSQL.setValue(PetType.OrgId.name(), 1);
    petTypeSQL.setValue(PetType.PetTypeDesc.name(), petTypeFields[0]);
    petTypeSQL.setValue(Pet.PetTypeId.name(),
                        GLServerUtil.getNextIdValue(EFAPTable.PetType + "Id", 1));
    petTypeSQL.setValue(PetType.PetTypeShortDesc.name(), petTypeFields[1]);
    petTypeSQL.setValue(PetType.Version.name(), GLServerUtil.generateVersion());
    petTypeSQL.execute();
  }
  GLServerUtil.getNextIdValue(EFAPTable.PetType + "Id", PetTypes.length + 1);
}
//--------------------------------------------------------------------------------------------------
private static void truncate(final EFAPTable table) throws GLDBException {
  GLDBUtil.execSQL("truncate " + table);
}
//--------------------------------------------------------------------------------------------------
}