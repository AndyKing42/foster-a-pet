package org.fosterapet.databaseupdate;
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
import org.fosterapet.databaseupdate.IDatabaseUpdateEnums.EDBUConfigAD;
import org.fosterapet.shared.IDBEnums.DBUpdateNote;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.Person;
import org.fosterapet.shared.IDBEnums.SessionToken;
import com.greatlogic.glbase.gldb.EGLColumnDataType;
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLDBUtil;
import com.greatlogic.glbase.gldb.GLDataSource;
import com.greatlogic.glbase.gldb.GLSQL;
import com.greatlogic.glbase.gldb.IGLDBEnums.EGLDBException;
import com.greatlogic.glbase.gllib.GLConfig;
import com.greatlogic.glbase.gllib.GLLog;

class DatabaseRevs_0_00 {
//--------------------------------------------------------------------------------------------------
static String rev_0_00_000(final String dbRevNumber, final boolean apply) throws GLDBException {
  final String result = "Create the DBUpdateNote table.";
  if (apply) {
    GLDBUtil.createTableFromSQL(EFAPTable.DBUpdateNote.name(), true,
                                "" //
                                        + "DBUpdateNoteId INTEGER NOT NULL," //
                                        + "AppliedDateTime DATETIME," //
                                        + "DBRevNumber VARCHAR(30) NOT NULL," //
                                        + "DBUpdateDesc VARCHAR(2000) NOT NULL," //
                                        + "DevDateTime DATETIME NOT NULL," //
                                        + "Version VARCHAR(30) NOT NULL");
    GLDBUtil.createPrimaryKey(EFAPTable.DBUpdateNote.name(), DBUpdateNote.DBUpdateNoteId.name());
    GLDataSource.getTableResultSetMetadata(EFAPTable.DBUpdateNote.name());
    DBUUtil.insertDBUpdateNote(dbRevNumber, "20130415", "113000", result);
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
static String rev_0_00_001(final String dbRevNumber, final boolean apply) throws GLDBException {
  final String result = "Initial FAP table creation.";
  if (apply) {
    DBUUtil.applySQLFile("FosterAPet.sql");
    if (GLConfig.getTopConfigElement().attributeAsBoolean(EDBUConfigAD.RecreateAllTables)) {
      final GLSQL personSQL = GLSQL.insert(EFAPTable.Person.name(), true);
      personSQL.setValue(Person.DisplayName.name(), "System Administrator");
      personSQL.setValue(Person.FirstName.name(), "Andy");
      personSQL.setValue(Person.LastName.name(), "King");
      personSQL.setValue(Person.LoginName.name(), "AndyKing@FosterAPet.org");
      personSQL.setValue(Person.PasswordHash.name(),
                         "$2a$10$iYdbWnh/5/XRzsCFFMz.t.Wz0qz1YB/y9PVj.BnAOQu.7UMi4d306");
      personSQL.setValue(Person.PersonId.name(), 1);
      personSQL.setValue(Person.Version.name(), "1");
      personSQL.execute();
    }
    try {
      DBUUtil.addNextIds();
    }
    catch (final Exception e) {
      GLLog.major("addNextIds failed", e);
      throw new GLDBException(EGLDBException.ExecSQLError);
    }
    DBUUtil.insertDBUpdateNote(dbRevNumber, "20130415", "113000", result);
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
static String rev_0_00_002(final String dbRevNumber, final boolean apply) throws GLDBException {
  final String result = "Create the SessionToken table.";
  if (apply) {
    final String sql = "CREATE TABLE SessionToken\n" //
                       + "  (\n" //
                       + "    SessionTokenId INTEGER NOT NULL ,\n" //
                       + "    PersonId       INTEGER NOT NULL ,\n" //
                       + "    SessionToken   VARCHAR (100) ,\n" //
                       + "    CONSTRAINT SessionTokenPK PRIMARY KEY CLUSTERED (SessionTokenId)\n" //
                       + "WITH\n" //
                       + "  (\n" //
                       + "    ALLOW_PAGE_LOCKS = ON ,\n" //
                       + "    ALLOW_ROW_LOCKS  = ON\n" //
                       + "  )\n" //
                       + "  ON \"default\"\n" //
                       + "  )\n" //
                       + "  ON \"default\"\n" //
                       + "GO\n" //
                       + "CREATE UNIQUE NONCLUSTERED INDEX\n" //
                       + "SessionToken_SessionToken_IDX ON SessionToken\n" //
                       + "(\n" //
                       + "  SessionToken\n" //
                       + ")\n" //
                       + "ON \"default\"\n" //
                       + "GO\n" //
                       + "CREATE UNIQUE NONCLUSTERED INDEX\n" //
                       + "SessionToken_PersonId_SessionToken_IDX ON SessionToken\n" //
                       + "(\n" //
                       + "  PersonId ,\n" //
                       + "  SessionToken\n" //
                       + ")\n" //
                       + "ON \"default\"\n" //
                       + "GO\n";
    DBUUtil.createTablesFromSQL(sql);
    DBUUtil.insertDBUpdateNote(dbRevNumber, "20140804", "113000", result);
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
static String rev_0_00_003(final String dbRevNumber, final boolean apply) throws GLDBException {
  final String result = "Add the SessionToken.ExpirationTime column.";
  if (apply) {
    GLDBUtil.addColumn(EFAPTable.SessionToken.name(), SessionToken.ExpirationTime.name(),
                       EGLColumnDataType.DateTime, true);
    DBUUtil.insertDBUpdateNote(dbRevNumber, "20140806", "163000", result);
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
static String rev_0_00_004(final String dbRevNumber, final boolean apply) throws GLDBException {
  final String result = "Recreate the SessionToken table.";
  if (apply) {
    final String sql;
    sql = "CREATE TABLE SessionToken\n" //
          + "(\n" //
          + "SessionTokenId INTEGER NOT NULL ,\n" //
          + "ExpirationTime DATETIME NOT NULL ,\n" //
          + "PersonId       INTEGER NOT NULL ,\n" //
          + "SessionToken   VARCHAR (100) NOT NULL ,\n" //
          + "Version        VARCHAR (30) NOT NULL ,\n" //
          + "CONSTRAINT SessionTokenPK PRIMARY KEY CLUSTERED (SessionTokenId)\n" //
          + "WITH\n" //
          + "(\n" //
          + "ALLOW_PAGE_LOCKS = ON ,\n" //
          + "ALLOW_ROW_LOCKS  = ON\n" //
          + ")\n" //
          + "ON \"default\"\n" //
          + ")\n" //
          + "ON \"default\"\n" //
          + "GO\n" //
          + "CREATE UNIQUE NONCLUSTERED INDEX\n" //
          + "SessionToken_SessionToken_IDX ON SessionToken\n" //
          + "(\n" //
          + "SessionToken\n" //
          + ")\n" //
          + "ON \"default\"\n" //
          + "GO\n" //
          + "CREATE UNIQUE NONCLUSTERED INDEX\n" //
          + "SessionToken_PersonId_SessionToken_IDX ON SessionToken\n" //
          + "(\n" //
          + "PersonId ,\n" //
          + "SessionToken\n" //
          + ")\n" //
          + "ON \"default\"\n" //
          + "GO\n";
    DBUUtil.createTablesFromSQL(sql);
    DBUUtil.insertDBUpdateNote(dbRevNumber, "20140808", "110000", result);
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
static String rev_0_00_005(final String dbRevNumber, final boolean apply) throws GLDBException {
  final String result = "Recreate the SessionToken table.";
  if (apply) {
    final String sql;
    sql = "CREATE TABLE SessionToken\n" //
          + "(\n" //
          + "SessionTokenId INTEGER NOT NULL ,\n" //
          + "ExpirationTime DATETIME NOT NULL ,\n" //
          + "SessionToken   VARCHAR (100) NOT NULL ,\n" //
          + "UserId         INTEGER NOT NULL ,\n" //
          + "Version        VARCHAR (30) NOT NULL ,\n" //
          + "CONSTRAINT SessionTokenPK PRIMARY KEY CLUSTERED (SessionTokenId)\n" //
          + "WITH\n" //
          + "(\n" //
          + "ALLOW_PAGE_LOCKS = ON ,\n" //
          + "ALLOW_ROW_LOCKS  = ON\n" //
          + ")\n" //
          + "ON \"default\"\n" //
          + ")\n" //
          + "ON \"default\"\n" //
          + "GO\n" //
          + "CREATE UNIQUE NONCLUSTERED INDEX\n" //
          + "SessionToken_SessionToken_IDX ON SessionToken\n" //
          + "(\n" //
          + "SessionToken\n" //
          + ")\n" //
          + "ON \"default\"\n" //
          + "GO\n" //
          + "CREATE UNIQUE NONCLUSTERED INDEX\n" //
          + "SessionToken_PersonId_SessionToken_IDX ON SessionToken\n" //
          + "(\n" //
          + "UserId ,\n" //
          + "SessionToken\n" //
          + ")\n" //
          + "ON \"default\"\n" //
          + "GO\n";
    DBUUtil.createTablesFromSQL(sql);
    DBUUtil.insertDBUpdateNote(dbRevNumber, "20140815", "143000", result);
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
}