package org.fosterapet.client;

import org.fosterapet.client.widget.MainLayoutWidget;
import org.fosterapet.shared.IRemoteService;
import org.fosterapet.shared.IRemoteServiceAsync;
import org.fosterapet.shared.LookupCacheLoader;
import org.fosterapet.shared.Validators;
import org.greatlogic.glgwt.client.core.GLLookupCache;
import org.greatlogic.glgwt.client.event.GLEventBus;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.widget.core.client.ContentPanel;

public abstract class ClientFactory {
//--------------------------------------------------------------------------------------------------
public static ClientFactory   Instance;

private final GLEventBus      _eventBus;
protected GLLookupCache       _lookupCache;
protected MainLayoutWidget    _mainLayoutWidget;
protected IRemoteServiceAsync _remoteService;
protected Validators          _validators;
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
public ContentPanel getCenterPanel() {
  return _mainLayoutWidget == null ? null : _mainLayoutWidget.getCenterPanel();
}
//--------------------------------------------------------------------------------------------------
public GLEventBus getEventBus() {
  return _eventBus;
}
//--------------------------------------------------------------------------------------------------
public GLLookupCache getLookupCache() {
  return _lookupCache;
}
//--------------------------------------------------------------------------------------------------
public MainLayoutWidget getMainLayoutWidget() {
  return _mainLayoutWidget;
}
//--------------------------------------------------------------------------------------------------
public IRemoteServiceAsync getRemoteService() {
  return _remoteService;
}
//--------------------------------------------------------------------------------------------------
public Validators getValidators() {
  return _validators;
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