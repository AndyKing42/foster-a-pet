package org.fosterapet.login;

import org.fosterapet.server.IServerEnums.ESessionAttribute;
import org.fosterapet.server.model.daolocator.PersonDAOLocator;
import org.fosterapet.server.model.dto.Person;
import org.fosterapet.shared.IRemoteService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.IGLLibEnums.EGLLogLevel;
@SuppressWarnings("serial")
public class FAPRemoteServiceServlet extends RemoteServiceServlet implements IRemoteService {
//--------------------------------------------------------------------------------------------------
@Override
public void log(final int logLevelId, final String location, final String message) {
  GLLog.log(EGLLogLevel.lookupUsingID(logLevelId), location + "=>" + message);
} // log()
//--------------------------------------------------------------------------------------------------
/**
 * Attempts to log in using the supplied login name and password.
 * @param loginName The login name that will be used for the login attempt.
 * @param password The password that will be used for the login attempt (this is the plain text
 * password, not the encrypted hash value).
 * @return The id of the Person row, or zero if the login request fails.
 */
@Override
public Integer login(final String loginName, final String password) {
  final Person loginPerson = PersonDAOLocator.getPersonDAO().findByLoginNameAndPassword(loginName,
                                                                                        password);
  if (loginPerson == null) {
    GLLog.infoSummary("Login failed for login name:" + loginName);
    return 0;
  }
  GLLog.infoSummary("Login succeeded for login name:" + loginPerson.getLoginName());
  getThreadLocalRequest().getSession().setAttribute(ESessionAttribute.LoginPerson.name(),
                                                    loginPerson);
  return loginPerson.getPersonId();
} // login()
//--------------------------------------------------------------------------------------------------
}