package org.greatlogic.glgwt.client.core;

import org.fosterapet.shared.IRemoteServiceAsync;
import org.fosterapet.shared.Validators;
import org.greatlogic.glgwt.client.event.GLEventBus;

public abstract class GLClientFactory {
//--------------------------------------------------------------------------------------------------
protected GLEventBus          _eventBus;
protected GLLookupCache       _lookupCache;
protected IRemoteServiceAsync _remoteService;
protected Validators          _validators;
//--------------------------------------------------------------------------------------------------
public GLEventBus getEventBus() {
  return _eventBus;
}
//--------------------------------------------------------------------------------------------------
public GLLookupCache getLookupCache() {
  return _lookupCache;
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
}