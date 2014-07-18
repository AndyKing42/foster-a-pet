package org.fosterapet.shared;

import org.greatlogic.glgwt.shared.IGLRemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("FAPRemoteServiceServlet")
public interface IRemoteService extends IGLRemoteService {
//--------------------------------------------------------------------------------------------------
String gaeTest();
void loadTestData(final String testDataOptionString);
void recreateTables();
//--------------------------------------------------------------------------------------------------
}