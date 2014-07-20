package org.fosterapet.shared;

import org.greatlogic.glgwt.shared.IGLRemoteServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IRemoteServiceAsync extends IGLRemoteServiceAsync {
//--------------------------------------------------------------------------------------------------
void gaeTest(AsyncCallback<String> callback);
void loadTestData(final String testDataOptionString, final AsyncCallback<Void> callback);
//--------------------------------------------------------------------------------------------------
}