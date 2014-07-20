package org.fosterapet.server;

import org.fosterapet.shared.IRemoteService;
import org.greatlogic.glgwt.server.GLRemoteServiceServlet;
import com.google.appengine.api.modules.ModulesService;
import com.google.appengine.api.modules.ModulesServiceFactory;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.IGLLibEnums.EGLLogLevel;

@SuppressWarnings("serial")
public class FAPRemoteServiceServlet extends GLRemoteServiceServlet implements IRemoteService {
//--------------------------------------------------------------------------------------------------
@Override
public String gaeTest() {
  final ModulesService modulesService = ModulesServiceFactory.getModulesService();
  if (modulesService == null) {
    return "modulesService is null";
  }
  final String module = modulesService.getCurrentModule();
  final String version = modulesService.getCurrentVersion();
  final String informationMessage = "CurrentModule:" + module + " CurrentInstanceId:" + //
                                    modulesService.getCurrentInstanceId() + //
                                    " CurrentVersion:" + version;
  try {
    modulesService.stopVersion(module, version);
    return informationMessage + " (stopVersion succeeded)";
  }
  catch (final Throwable t) {
    return informationMessage + " (gaeTest failed:" + t.getMessage() + ")";
  }
}
//--------------------------------------------------------------------------------------------------
@Override
public void loadTestData(final String testDataOptionString) {
  try {
    DBTestData.processRequest(testDataOptionString);
  }
  catch (final Exception e) {
    GLLog.major("Error loading test data", e);
  }
}
//--------------------------------------------------------------------------------------------------
@Override
public void log(final int logLevelId, final String location, final String message) {
  GLLog.log(EGLLogLevel.lookupUsingPriority(logLevelId * 10), location + "=>" + message);
}
//--------------------------------------------------------------------------------------------------
}