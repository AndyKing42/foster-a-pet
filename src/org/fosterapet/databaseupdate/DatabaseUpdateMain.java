package org.fosterapet.databaseupdate;

import org.fosterapet.databaseupdate.IDatabaseUpdateEnums.EDBUConfigAD;
import com.greatlogic.glbase.gllib.GLConfig;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.GLUtil;
import com.greatlogic.glbase.gllib.IGLProgram;

public class DatabaseUpdateMain {
//==================================================================================================
private static class DatabaseUpdateProgram implements IGLProgram {
@Override
public boolean displayCommandLineHelp() {
  return false;
} // displayCommandLineHelp()
} // class DatabaseUpdateProgram
//==================================================================================================
public static void main(final String... args) {
  GLUtil.initializeProgram(new DatabaseUpdateProgram(), null, null, true, args);
  new DatabaseUpdateMain();
} // main()
//--------------------------------------------------------------------------------------------------
private DatabaseUpdateMain() {
  try {
    final String loadDataFilename = GLConfig.getTopConfigElement() //
                                            .attributeAsString(EDBUConfigAD.LoadDataFilename);
    if (loadDataFilename.isEmpty()) {
      new DatabaseUpdater();
    }
    else {
      //      new LoadTestData(loadDataFilename);
    }
  }
  catch (final Exception e) {
    GLLog.toErrorOut("Database update failed ... see the log file for details",
                     GLUtil.LineSeparator);
    GLLog.critical("DatabaseUpdate failed", e);
    GLUtil.sleepAndExit(5000, false);
  }
  // test();
  GLUtil.sleepAndExit(1000, false);
} // DatabaseUpdateMain()
//--------------------------------------------------------------------------------------------------
void test() {
  try {
    // do something interesting
  }
  catch (final Exception e) {
    GLLog.major("Error testing DatabaseUpdate", e);
  }
  //  GLClientUtil.sleepAndExit(10000, false);
} // test()
//--------------------------------------------------------------------------------------------------
}