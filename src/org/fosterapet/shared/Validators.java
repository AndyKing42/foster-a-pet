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
import java.math.BigDecimal;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.Pet;
import org.greatlogic.glgwt.client.widget.GLValidationRecord;
import org.greatlogic.glgwt.shared.GLRecordValidator;
import org.greatlogic.glgwt.shared.GLValidators;

public class Validators extends GLValidators {
//--------------------------------------------------------------------------------------------------
public Validators() {
  super();
  addPetValidators();
}
//--------------------------------------------------------------------------------------------------
private void addPetValidators() {
  //  addColumnValidator(Pet.NumberOfFosters, new Validator<Integer>() {
  //    @Override
  //    public List<EditorError> validate(final Editor<Integer> editor, final Integer value) {
  //      return null;
  //    }
  //  });
  addRecordValidator(EFAPTable.Pet, new GLRecordValidator() {
    @Override
    public void validateRecord(final GLValidationRecord validationRecord) {
      if (validationRecord.asDec(Pet.AdoptionFee).compareTo(BigDecimal.ZERO) < 0) {
        addError(Pet.AdoptionFee, "Adoption fee cannot be less than zero");
      }
    }
  });
}
//--------------------------------------------------------------------------------------------------
}