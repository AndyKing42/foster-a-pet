package org.greatlogic.glgwt.client.db;
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
import java.util.HashSet;
import org.fosterapet.client.FAPClientFactory;
import org.fosterapet.shared.IFAPEnums.ETestDataOption;
import org.fosterapet.shared.IFAPRemoteServiceAsync;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.event.GLLookupTableLoadedEvent;
import org.greatlogic.glgwt.client.event.GLLookupTableLoadedEvent.IGLLookupTableLoadedEventHandler;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.Event.Type;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class DBAccess {
private HandlerRegistration _lookupTableLoadedHandlerRegistration;
//--------------------------------------------------------------------------------------------------
/**
 * If there are lookup tables that are needed by any of the columns in the grid then the creation of
 * the grid must be deferred until all of those lookup tables have been loaded into the cache. The
 * lookup tables are added to the lookupTableSet; when the LookupLoadedEvent is fired the table
 * that's been loaded is removed from the set; when all tables have been loaded the grid is created.
 * @param lookupTableSet The set that contains the list of tables that need to be loaded prior to
 * creating the grid.
 */
private void addLookupTableLoadedEventHandler(final HashSet<IGLTable> lookupTableSet) {
  final IGLLookupTableLoadedEventHandler handler = new IGLLookupTableLoadedEventHandler() {
    @Override
    public void onLookupTableLoadedEvent(final GLLookupTableLoadedEvent lookupTableLoadedEvent) {
      lookupTableSet.remove(lookupTableLoadedEvent.getTable());
      if (lookupTableSet.size() == 0) {
        _lookupTableLoadedHandlerRegistration.removeHandler();
        _lookupTableLoadedHandlerRegistration = null;
        //        createGrid();
      }
    }
  };
  final Type<IGLLookupTableLoadedEventHandler> eventType;
  eventType = GLLookupTableLoadedEvent.LookupTableLoadedEventType;
  _lookupTableLoadedHandlerRegistration = GLClientUtil.getEventBus().addHandler(eventType, //
                                                                                handler);
}
//--------------------------------------------------------------------------------------------------
public static void reloadTestData() {
  final IFAPRemoteServiceAsync remoteService;
  remoteService = (IFAPRemoteServiceAsync)FAPClientFactory.Instance.getRemoteService();
  remoteService.loadTestData(null, ETestDataOption.Reload.name(), new AsyncCallback<String>() {
    @Override
    public void onFailure(final Throwable t) {
      GLLog.popup(10, "Test data reload failed:" + t.getMessage());
    }
    @Override
    public void onSuccess(final String result) {
      GLLog.popup(10, "Test data reload is complete");
      FAPClientFactory.Instance.getLookupCache().reloadAll();
    }
  });
}
//--------------------------------------------------------------------------------------------------
}