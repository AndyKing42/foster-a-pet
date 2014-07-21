package org.fosterapet.shared;

import org.greatlogic.glgwt.shared.IGLRemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("FAPRemoteServiceServlet")
public interface IRemoteService extends IGLRemoteService {
//--------------------------------------------------------------------------------------------------
void loadTestData(final String testDataOptionString);
//--------------------------------------------------------------------------------------------------
}