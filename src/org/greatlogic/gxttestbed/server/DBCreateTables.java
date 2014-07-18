package org.greatlogic.gxttestbed.server;

import java.util.Map;
import org.greatlogic.gxttestbed.shared.IDBEnums.EGXTTestbedId;
import org.greatlogic.gxttestbed.shared.IDBEnums.EGXTTestbedTable;
import org.greatlogic.gxttestbed.shared.IDBEnums.NextId;
import com.google.common.collect.Maps;
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLDBUtil;
import com.greatlogic.glbase.gldb.GLSQL;
import com.greatlogic.glbase.gldb.GLSchemaLoader;
import com.greatlogic.glbase.gldb.GLSchemaTable;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.GLUtil;

class DBCreateTables {
//--------------------------------------------------------------------------------------------------
public static void recreateTables() {
  try {
    GLDBUtil.dropTable(null, EGXTTestbedTable.NextId.name());
    GLDBUtil.dropTable(null, EGXTTestbedTable.Pet.name());
    GLDBUtil.dropTable(null, EGXTTestbedTable.PetType.name());
    final String sql;
    sql = "" //
          + "create table NextId\n" //
          + "(\n" //
          + "NextId          integer not null,\n" //
          + "NextIdName      varchar(50) not null,\n" //
          + "NextIdTableName varchar(50) null,\n" //
          + "NextIdValue     integer not null,\n" //
          + "constraint NextIdPK primary key (NextId)\n" //
          + ")\n" //
          + "go\n" //
          + "create table Pet\n" //
          + "(\n" //
          + "PetId           INTEGER NOT NULL,\n" //
          + "AdoptionFee     DECIMAL (19,4) NOT NULL,\n" //
          + "FosterDate      DATETIME,\n" //
          + "IntakeDate      DATETIME NOT NULL,\n" //
          + "NumberOfFosters INTEGER NOT NULL,\n" //
          + "PetName         VARCHAR (50) NOT NULL,\n" //
          + "PetTypeId       INTEGER NOT NULL,\n" //
          + "Sex             VARCHAR (1) NOT NULL,\n" //
          + "TrainedFlag     VARCHAR (1) NOT NULL,\n" //
          + "constraint PetPK primary key (PetId)\n" //
          + ")\n" //
          + "go\n" //
          + "create TABLE PetType\n" //
          + "(\n" //
          + "PetTypeId        INTEGER NOT NULL,\n" //
          + "PetTypeDesc      VARCHAR (50) NOT NULL,\n" //
          + "PetTypeShortDesc VARCHAR (10) NOT NULL,\n" //
          + "constraint FosterLocTypePK primary key (PetTypeId)\n" //
          + ")\n" //
          + "go\n" //
          + "create unique index PetType_index1 on PetType\n" //
          + "(\n" //
          + "PetTypeShortDesc\n" //
          + ")\n" //
          + "go\n" //
          + "create unique index PetType_index2 on PetType\n" //
          + "(\n" //
          + "PetTypeDesc\n" //
          + ")\n" //
          + "go\n";
    final GLSchemaLoader schemaLoader = GLSchemaLoader.createUsingString(sql, "go", "--");
    for (final GLSchemaTable table : schemaLoader.getTables()) {
      table.executeSQL(null, true, true);
    }
    addIds();
  }
  catch (final Exception e) {
    GLLog.major("Table creation failed", e);
  }
}
//--------------------------------------------------------------------------------------------------
public static void addIds() throws GLDBException {
  for (final EGXTTestbedId id : EGXTTestbedId.values()) {
    int nextIdValue = 0;
    if (id.getTable() != null) {
      final String primaryKeyColumnName = id.getTable().getPrimaryKeyColumnMap().get(1).toString();
      final GLSQL sql = GLSQL.select();
      sql.from(id.getTable().name());
      final Map<String, String> maxMap = Maps.newHashMap();
      sql.getAggregates(false, null, maxMap, null, primaryKeyColumnName);
      nextIdValue = GLUtil.stringToInt(maxMap.get(primaryKeyColumnName));
    }
    final GLSQL nextIdSQL = GLSQL.insert(EGXTTestbedTable.NextId.name(), true);
    nextIdSQL.setValue(NextId.NextId.name(), id.getNextId());
    nextIdSQL.setValue(NextId.NextIdName.name(), id.getName());
    nextIdSQL.setValue(NextId.NextIdTableName.name(), id.getTable().name());
    nextIdSQL.setValue(NextId.NextIdValue.name(), nextIdValue + 100);
    nextIdSQL.execute(false);
  }
}
//--------------------------------------------------------------------------------------------------
}