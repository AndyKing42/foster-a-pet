package org.fosterapet.client;

import org.fosterapet.client.widget.MainLayoutWidget;
import org.fosterapet.shared.IRemoteService;
import org.fosterapet.shared.LookupCacheLoader;
import org.fosterapet.shared.Validators;
import org.greatlogic.glgwt.client.core.GLClientFactory;
import org.greatlogic.glgwt.client.core.GLLookupCache;
import org.greatlogic.glgwt.client.event.GLEventBus;
import com.google.gwt.core.client.GWT;

public abstract class ClientFactory extends GLClientFactory {
//--------------------------------------------------------------------------------------------------
public static ClientFactory Instance;

protected MainLayoutWidget  _mainLayoutWidget;
//--------------------------------------------------------------------------------------------------
protected ClientFactory() {
  Instance = this;
  _eventBus = new GLEventBus();
  _remoteService = GWT.create(IRemoteService.class);
  _lookupCache = new GLLookupCache();
  LookupCacheLoader.load(_lookupCache);
  _validators = new Validators();
}
//--------------------------------------------------------------------------------------------------
public MainLayoutWidget getMainLayoutWidget() {
  return _mainLayoutWidget;
}
//--------------------------------------------------------------------------------------------------
public abstract void hidePleaseWait();
//--------------------------------------------------------------------------------------------------
public abstract void login();
//--------------------------------------------------------------------------------------------------
public abstract void showPleaseWait();
//--------------------------------------------------------------------------------------------------
public abstract void setMainLayoutWidget(MainLayoutWidget mainLayoutWidget);
//--------------------------------------------------------------------------------------------------
}