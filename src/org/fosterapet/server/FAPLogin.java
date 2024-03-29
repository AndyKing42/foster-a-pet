package org.fosterapet.server;

import org.fosterapet.shared.FAPLoginResponse;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.Person;
import org.greatlogic.glgwt.server.GLLogin;
import org.greatlogic.glgwt.shared.GLLoginResponse;
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLSQL;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.GLUtil;
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
public class FAPLogin extends GLLogin {
//--------------------------------------------------------------------------------------------------
private void loadPerson(final FAPLoginResponse loginResponse, final String whereClause) {
  try {
    final GLSQL personSQL = GLSQL.select();
    personSQL.from(EFAPTable.Person.name());
    personSQL.whereAnd(0, whereClause, 0);
    personSQL.open();
    try {
      if (personSQL.next()) {
        _loginName = personSQL.asString(Person.LoginName.name());
        _userId = personSQL.asInt(Person.PersonId.name());
        _passwordHash = personSQL.asString(Person.PasswordHash.name());
        if (loginResponse != null) {
          final String personColumnCSV;
          personColumnCSV = GLUtil.iterableAsCommaDelim(personSQL.getColumnNameIterable(), null) //
                                  .toString();
          final String personDataCSV = personSQL.getRowAsCSV(null).toString();
          loginResponse.setPersonCSVs(personColumnCSV, personDataCSV);
        }
      }
    }
    finally {
      personSQL.close();
    }
  }
  catch (final GLDBException e) {
    GLLog.major("Select failed for the Person table using:" + whereClause, e);
  }
}
//--------------------------------------------------------------------------------------------------
@Override
protected void setLoginNameAndPasswordHash(final GLLoginResponse loginResponse, final int userId) {
  loadPerson((FAPLoginResponse)loginResponse, Person.PersonId + "=" + userId);
}
//--------------------------------------------------------------------------------------------------
@Override
protected boolean setNewPassword(final String newPasswordHash) {
  try {
    final GLSQL personSQL = GLSQL.update(EFAPTable.Person.name());
    personSQL.setValue(Person.PasswordHash.name(), newPasswordHash);
    personSQL.whereAnd(0, Person.PersonId + "=" + _userId, 0);
    personSQL.execute();
    return true;
  }
  catch (final GLDBException dbe) {
    GLLog.major("setNewPassword() failed", dbe);
    return false;
  }
}
//--------------------------------------------------------------------------------------------------
@Override
protected void setUserIdAndPasswordHash(final GLLoginResponse loginResponse, final String loginName) {
  loadPerson((FAPLoginResponse)loginResponse, Person.LoginName + "='" + loginName + "'");
}
//--------------------------------------------------------------------------------------------------
}