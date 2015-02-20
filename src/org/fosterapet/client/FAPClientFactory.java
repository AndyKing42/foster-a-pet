package org.fosterapet.client;
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
import org.fosterapet.client.widget.AppTabPanelWidget;
import org.fosterapet.client.widget.MainLayoutWidget;
import org.fosterapet.shared.FAPValidators;
import org.fosterapet.shared.IFAPRemoteService;
import org.fosterapet.shared.LookupCacheLoader;
import org.greatlogic.glgwt.client.core.GLClientFactory;
import org.greatlogic.glgwt.client.db.GLDBException;
import org.greatlogic.glgwt.client.db.GLDBUpdater;
import org.greatlogic.glgwt.client.db.GLLookupCache;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.db.GLSQL;
import org.greatlogic.glgwt.client.db.IGLSQLModifier;
import org.greatlogic.glgwt.client.event.GLEventBus;
import com.google.gwt.core.client.GWT;

public abstract class FAPClientFactory extends GLClientFactory {
//--------------------------------------------------------------------------------------------------
public static FAPClientFactory Instance;

protected AppTabPanelWidget    _appTabPanelWidget;
private GLRecord               _loginPersonRecord;
protected MainLayoutWidget     _mainLayoutWidget;
//--------------------------------------------------------------------------------------------------
protected FAPClientFactory() {
  Instance = this;
  _dbUpdater = new GLDBUpdater();
  _eventBus = new GLEventBus();
  _remoteService = GWT.create(IFAPRemoteService.class);
  _remoteServiceHelper = new RemoteServiceHelper();
  _lookupCache = new GLLookupCache(createLookupCacheSQLModifier());
  LookupCacheLoader.load(_lookupCache);
  _validators = new FAPValidators();
}
//--------------------------------------------------------------------------------------------------
protected IGLSQLModifier createLookupCacheSQLModifier() {
  return new IGLSQLModifier() {
    @Override
    public void modifySQL(final GLSQL sql) throws GLDBException {
      FAPUtil.addStandardSQLWhere(sql);
    }
  };
}
//--------------------------------------------------------------------------------------------------
public AppTabPanelWidget getAppTabPanelWidget() {
  return _appTabPanelWidget;
}
//--------------------------------------------------------------------------------------------------
public GLRecord getLoginPersonRecord() {
  return _loginPersonRecord;
}
//--------------------------------------------------------------------------------------------------
public MainLayoutWidget getMainLayoutWidget() {
  return _mainLayoutWidget;
}
//--------------------------------------------------------------------------------------------------
public abstract void hidePleaseWait();
//--------------------------------------------------------------------------------------------------
public abstract void setAppTabPanelWidget(final AppTabPanelWidget appTabPanelWidget);
//--------------------------------------------------------------------------------------------------
public void setLoginPersonRecord(final GLRecord personRecord) {
  _loginPersonRecord = personRecord;
}
//--------------------------------------------------------------------------------------------------
public abstract void setMainLayoutWidget(final MainLayoutWidget mainLayoutWidget);
//--------------------------------------------------------------------------------------------------
public abstract void showPleaseWait();
//--------------------------------------------------------------------------------------------------
}