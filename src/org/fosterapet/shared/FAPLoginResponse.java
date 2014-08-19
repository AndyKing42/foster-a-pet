package org.fosterapet.shared;

import java.util.ArrayList;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.greatlogic.glgwt.client.core.GLCSV;
import org.greatlogic.glgwt.client.core.GLCSVException;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.db.GLRecordDef;
import org.greatlogic.glgwt.shared.GLLoginResponse;
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
public class FAPLoginResponse extends GLLoginResponse {
//--------------------------------------------------------------------------------------------------
private static final long  serialVersionUID = 1122L;

private String             _personColumnCSV;
private String             _personDataCSV;
private transient GLRecord _personRecord;
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
public GLRecord getPersonRecord() {
  if (_personColumnCSV == null || _personDataCSV == null) {
    GLLog.popup(30, "FAPLoginResponse CSV is null");
    return null;
  }
  if (_personRecord == null) {
    final GLRecordDef recordDef = new GLRecordDef(EFAPTable.Person, _personColumnCSV.split(","));
    try {
      _personRecord = new GLRecord(recordDef, (ArrayList)GLCSV.extract(_personDataCSV));
    }
    catch (final GLCSVException e) {
      GLLog.popup(30, "CSV extract failed:" + e.getMessage());
    }
  }
  return _personRecord;
}
//--------------------------------------------------------------------------------------------------
public void setPersonCSVs(final String personColumnCSV, final String personDataCSV) {
  _personColumnCSV = personColumnCSV;
  _personDataCSV = personDataCSV;
}
//--------------------------------------------------------------------------------------------------
}