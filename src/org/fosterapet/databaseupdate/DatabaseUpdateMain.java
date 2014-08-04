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