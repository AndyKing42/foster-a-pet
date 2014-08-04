package org.fosterapet.login;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.fosterapet.server.IServerEnums.ESessionAttribute;
import org.fosterapet.server.model.dto.Person;
import com.google.web.bindery.requestfactory.server.ExceptionHandler;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.google.web.bindery.requestfactory.server.ServiceLayer;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.greatlogic.glbase.gllib.GLLog;
@SuppressWarnings("serial")
public class FAPRequestFactoryServlet extends RequestFactoryServlet {
//==================================================================================================
static class RequestFactoryExceptionHandler implements ExceptionHandler {
@Override
public ServerFailure createServerFailure(final Throwable t) {
  GLLog.major("Request factory exception", t);
  return new ServerFailure(t.getMessage(), t.getClass().getName(), null, true);
} // createServerFailure()
} // class RequestFactoryExceptionHandler
//==================================================================================================
public FAPRequestFactoryServlet() {
  super(new RequestFactoryExceptionHandler());
  ServiceLayer.create(new FAPServiceLayerDecorator());
} // FAPRequestFactoryServlet()
//--------------------------------------------------------------------------------------------------
@Override
protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
  throws IOException, ServletException {
  if (!personIsLoggedIn(request)) {
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  }
  else {
    super.doPost(request, response);
  }
} // doPost()
//--------------------------------------------------------------------------------------------------
private boolean personIsLoggedIn(final HttpServletRequest request) {
  final HttpSession session = request.getSession();
  if (session == null) {
    return false;
  }
  final Person loginPerson = (Person)session.getAttribute(ESessionAttribute.LoginPerson.name());
  return loginPerson != null;
} // personIsLoggedIn()
//--------------------------------------------------------------------------------------------------
}