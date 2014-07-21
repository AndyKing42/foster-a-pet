package org.fosterapet.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
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
  final String configFilename = servletContext.getRealPath("cfg/FosterAPet.cfg").replace('\\', '/');
  GLUtil.initializeProgram(new FAPProgram(), null, null, true, //
                           "<args ConfigFilename='" + configFilename + "'/>");
}
//--------------------------------------------------------------------------------------------------
}