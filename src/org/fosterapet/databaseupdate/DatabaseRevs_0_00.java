package org.fosterapet.databaseupdate;

import org.fosterapet.databaseupdate.IDatabaseUpdateEnums.EDBUConfigAD;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLDBUtil;
import com.greatlogic.glbase.gldb.GLDataSource;
import com.greatlogic.glbase.gldb.GLSQL;
import com.greatlogic.glbase.gllib.GLConfig;

class DatabaseRevs_0_00 {
//--------------------------------------------------------------------------------------------------
static String rev_0_00_000(final String dbRevNumber, final boolean apply) throws GLDBException {
  final String result = "Create the DBUpdateNote table.";
  if (apply) {
    GLDBUtil.createTableFromSQL(EFAPTable.DBUpdateNote, true,
                                "DBRevNumber VARCHAR(30) NOT NULL," //
                                        + "AppliedDateTime DATETIME," //
                                        + "DBUpdateDesc VARCHAR(2000) NOT NULL," //
                                        + "DBUpdateNoteId INTEGER NOT NULL," //
                                        + "DevDateTime DATETIME NOT NULL," //
                                        + "Version INTEGER NOT NULL");
    GLDBUtil.createPrimaryKey(EFAPTable.DBUpdateNote, EDBUpdateNote.DBUpdateNoteId);
    GLDataSource.getTableResultSetMetaData(EFAPTable.DBUpdateNote.name());
    DBUUtil_old.insertDBUpdateNote(dbRevNumber, "20130415", "113000", result);
  }
  return result;
} // rev_0_00_000()
//--------------------------------------------------------------------------------------------------
static String rev_0_00_001(final String dbRevNumber, final boolean apply) throws GLDBException {
  final String result = "Initial FAP table creation.";
  if (apply) {
    DBUUtil_old.applySQLFile("FosterAPet.sql");
    if (GLConfig.getTopConfigElement().attributeAsBoolean(EDBUConfigAD.RecreateAllTables)) {
      final GLSQL personSQL = GLSQL.insert(EFAPTable.Person, true);
      personSQL.setValue(EPerson.DisplayName, "System Administrator");
      personSQL.setValue(EPerson.FirstName, "Andy");
      personSQL.setValue(EPerson.LastName, "King");
      personSQL.setValue(EPerson.LoginName, "AndyKing@FosterAPet.org");
      personSQL.setValue(EPerson.PasswordHash,
                         "$2a$10$iYdbWnh/5/XRzsCFFMz.t.Wz0qz1YB/y9PVj.BnAOQu.7UMi4d306");
      personSQL.setValue(EPerson.PersonId, 1);
      personSQL.setValue(EPerson.Version, "1");
      personSQL.execute();
    }
    DBUUtil_old.addNextIds();
    DBUUtil_old.insertDBUpdateNote(dbRevNumber, "20130415", "113000", result);
  }
  return result;
} // rev_0_00_001()
//--------------------------------------------------------------------------------------------------
}