package org.greatlogic.glgwt.client.core;
/*
 * Copyright 2006-2014 Andy King (GreatLogic.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
import org.fosterapet.shared.IRemoteServiceAsync;
import org.fosterapet.shared.Validators;
import org.greatlogic.glgwt.client.db.GLDBUpdater;
import org.greatlogic.glgwt.client.db.GLLookupCache;
import org.greatlogic.glgwt.client.event.GLEventBus;
import org.greatlogic.glgwt.client.widget.GLLoginWidget;

public abstract class GLClientFactory {
//--------------------------------------------------------------------------------------------------
protected GLDBUpdater           _dbUpdater;
protected GLEventBus            _eventBus;
private GLLoginWidget           _loginWidget;
protected GLLookupCache         _lookupCache;
protected IRemoteServiceAsync   _remoteService;
protected GLRemoteServiceHelper _remoteServiceHelper;
protected Validators            _validators;
//--------------------------------------------------------------------------------------------------
public GLDBUpdater getDBUpdater() {
  return _dbUpdater;
}
//--------------------------------------------------------------------------------------------------
public GLEventBus getEventBus() {
  return _eventBus;
}
//--------------------------------------------------------------------------------------------------
public GLLoginWidget getLoginWidget() {
  return _loginWidget;
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
public GLRemoteServiceHelper getRemoteServiceHelper() {
  return _remoteServiceHelper;
}
//--------------------------------------------------------------------------------------------------
public Validators getValidators() {
  return _validators;
}
//--------------------------------------------------------------------------------------------------
}