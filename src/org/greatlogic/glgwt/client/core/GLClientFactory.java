package org.greatlogic.glgwt.client.core;

import org.fosterapet.shared.IRemoteServiceAsync;
import org.fosterapet.shared.Validators;
import org.greatlogic.glgwt.client.db.GLDBUpdater;
import org.greatlogic.glgwt.client.db.GLLookupCache;
import org.greatlogic.glgwt.client.event.GLEventBus;

public abstract class GLClientFactory {
//--------------------------------------------------------------------------------------------------
protected GLDBUpdater         _dbUpdater;
protected GLEventBus          _eventBus;
protected GLLookupCache       _lookupCache;
protected IRemoteServiceAsync _remoteService;
protected Validators          _validators;
//--------------------------------------------------------------------------------------------------
public GLDBUpdater getDBUpdater() {
  return _dbUpdater;
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
public IRemoteServiceAsync getRemoteService() {
  return _remoteService;
}
//--------------------------------------------------------------------------------------------------
public Validators getValidators() {
  return _validators;
}
//--------------------------------------------------------------------------------------------------
}