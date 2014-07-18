package org.greatlogic.gxttestbed.client.widget;
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
import org.greatlogic.glgwt.client.widget.GLGridWidget;
import org.greatlogic.glgwt.shared.GLRecordValidator;
import org.greatlogic.gxttestbed.shared.IDBEnums.Pet;
import com.sencha.gxt.core.client.util.DateWrapper;

public class PetGridWidget extends GLGridWidget {
//--------------------------------------------------------------------------------------------------
public PetGridWidget(final GLRecordValidator recordValidator, final boolean inlineEditing,
                     final boolean useCheckBoxSelectionModel, final boolean rowLevelCommits,
                     final Pet... petColumns) {
  super(null, "There are no pets", recordValidator, inlineEditing, useCheckBoxSelectionModel,
        rowLevelCommits, petColumns);
}
//--------------------------------------------------------------------------------------------------
@Override
protected void addFilters() {
  addFilter(Pet.AdoptionFee);
  addFilter(Pet.FosterDate, new DateWrapper().addYears(-20).asDate(), //
            new DateWrapper().addDays(1).asDate());
  addFilter(Pet.IntakeDate, new DateWrapper().addYears(-20).asDate(), //
            new DateWrapper().addDays(1).asDate());
  addFilter(Pet.NumberOfFosters);
  addFilter(Pet.PetName);
  addFilter(Pet.PetTypeId);
  addFilter(Pet.Sex);
  addFilter(Pet.TrainedFlag);
}
//--------------------------------------------------------------------------------------------------
}