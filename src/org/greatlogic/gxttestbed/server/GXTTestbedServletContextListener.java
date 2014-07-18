package org.greatlogic.gxttestbed.server;

import javax.servlet.ServletContextEvent;
import org.greatlogic.glgwt.server.GLServletContextListener;
import com.google.appengine.api.utils.SystemProperty;
import com.greatlogic.glbase.gldb.GLDBType;
import com.greatlogic.glbase.gldb.GLDataSource;
import com.greatlogic.glbase.gldb.IGLDBEnums.EGLDBConfigAttribute;
import com.greatlogic.glbase.gldb.IGLDBEnums.EGLDBType;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.glxml.GLXMLElement;

public class GXTTestbedServletContextListener extends GLServletContextListener {
//--------------------------------------------------------------------------------------------------
@Override
public void contextInitialized(final ServletContextEvent event) {
  initialize("GXTTestbed");
  final GLXMLElement dsElement = new GLXMLElement("DataSources");
  final GLXMLElement fapElement = new GLXMLElement("FosterAPet", dsElement);
  fapElement.addAttribute(EGLDBConfigAttribute.Active, true);
  fapElement.addAttribute(EGLDBConfigAttribute.InitialConnections, 5);
  fapElement.addAttribute(EGLDBConfigAttribute.Default, true);
  fapElement.addAttribute(EGLDBConfigAttribute.MaxConnections, 20);
  fapElement.addAttribute(EGLDBConfigAttribute.Name, "FosterAPet");
  fapElement.addAttribute(EGLDBConfigAttribute.Type, EGLDBType.MySQL.name());
  if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
    GLLog.debug("Production");
    fapElement.addAttribute(EGLDBConfigAttribute.Password, "");
    fapElement.addAttribute(EGLDBConfigAttribute.User, "root");
    GLDBType.getDBType(EGLDBType.MySQL).setDriverManagerClassName("com.mysql.jdbc.GoogleDriver");
    fapElement.addAttribute(EGLDBConfigAttribute.ConnectionURL,
                            "jdbc:google:mysql://gxt-testbed:foster-a-pet/fosterapet");
  }
  else {
    GLLog.debug("Non-production");
    fapElement.addAttribute(EGLDBConfigAttribute.Password, "andy");
    fapElement.addAttribute(EGLDBConfigAttribute.ServerAddress, "localhost");
    fapElement.addAttribute(EGLDBConfigAttribute.User, "andy");
  }
  GLLog.debug(dsElement.toString());
  GLDataSource.initialize(dsElement);
  GLLog.infoSummary("Context initialized");
}
//--------------------------------------------------------------------------------------------------
}