package org.fosterapet.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.lang3.StringUtils;
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
  String configFilename = System.getenv("FosterAPetConfigFilename");
  if (StringUtils.isEmpty(configFilename)) {
    configFilename = event.getServletContext().getInitParameter("ConfigFilename");
  }
  GLUtil.initializeProgram(new FAPProgram(), null, null, true, //
                           "<args ConfigFilename='" + configFilename + "'/>");
}
//--------------------------------------------------------------------------------------------------
}