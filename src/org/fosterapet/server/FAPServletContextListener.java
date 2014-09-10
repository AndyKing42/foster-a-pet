package org.fosterapet.server;
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
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.lang3.StringUtils;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.NextId;
import org.greatlogic.glgwt.server.GLServerUtil;
import com.greatlogic.glbase.gllib.GLUtil;
import com.greatlogic.glbase.gllib.IGLProgram;

public class FAPServletContextListener implements ServletContextListener {
//==================================================================================================
private static class FAPProgram implements IGLProgram {
@Override
public boolean displayCommandLineHelp() {
  return false;
} // displayCommandLineHelp()
} // class FAPProgram
//==================================================================================================
@Override
public void contextDestroyed(final ServletContextEvent event) {
  //
}
//--------------------------------------------------------------------------------------------------
@Override
public void contextInitialized(final ServletContextEvent event) {
  final ServletContext servletContext = event.getServletContext();
  String configFilename = servletContext.getInitParameter("ConfigFilename");
  configFilename = StringUtils.isBlank(configFilename) ? "cfg/FosterAPet.cfg" : configFilename;
  configFilename = servletContext.getRealPath(configFilename).replace('\\', '/');
  GLUtil.initializeProgram(new FAPProgram(), null, null, true, //
                           "<args ConfigFilename='" + configFilename + "'/>");
  GLServerUtil.initialize(EFAPTable.NextId.name(), NextId.NextIdName.name(),
                          NextId.NextIdValue.name());
}
//--------------------------------------------------------------------------------------------------
}