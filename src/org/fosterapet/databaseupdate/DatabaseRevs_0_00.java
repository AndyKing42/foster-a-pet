package org.fosterapet.databaseupdate;

import org.fosterapet.databaseupdate.IDatabaseUpdateEnums.EDBUConfigAD;
import org.fosterapet.shared.IDBEnums.DBUpdateNote;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.Person;
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
}