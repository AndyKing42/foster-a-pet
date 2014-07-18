package org.greatlogic.glgwt.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.greatlogic.glbase.gllib.GLGAE;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.glxml.GLXMLElement;

public abstract class GLServletContextListener implements ServletContextListener {
//--------------------------------------------------------------------------------------------------
@Override
public void contextDestroyed(final ServletContextEvent event) {
  //
}
//--------------------------------------------------------------------------------------------------
protected void initialize(final String loggerName) {
  GLGAE.setUsingGAE(true);
  GLLog.initialize(true);
  final GLXMLElement logElement = new GLXMLElement("Log");
  logElement.addAttribute("Level", "Debug");
  logElement.addAttribute("LoggerName", loggerName);
  GLLog.setLoggingOptions(logElement, null, null);
}
//--------------------------------------------------------------------------------------------------
}