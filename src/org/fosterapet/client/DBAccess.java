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
import org.fosterapet.shared.IFAPEnums.ETestDataOption;
import org.fosterapet.shared.IFAPRemoteServiceAsync;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.db.GLDBException;
import org.greatlogic.glgwt.client.db.GLListStore;
import org.greatlogic.glgwt.client.db.GLSQL;
import org.greatlogic.glgwt.client.db.IGLSQLModifier;
import org.greatlogic.glgwt.client.db.IGLSQLSelectCallback;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DBAccess {
//--------------------------------------------------------------------------------------------------
public static void load(final GLListStore listStore, final IGLTable table,
                        final String orderByClause, final boolean includeArchivedRows) {
  load(listStore, table, orderByClause, includeArchivedRows, 0, null);
}
//--------------------------------------------------------------------------------------------------
public static void load(final GLListStore listStore, final IGLTable table,
                        final String orderByClause, final boolean includeArchivedRows,
                        final int orgId) {
  load(listStore, table, orderByClause, includeArchivedRows, orgId, null);
}
//--------------------------------------------------------------------------------------------------
public static void load(final GLListStore listStore, final IGLTable table,
                        final String orderByClause, final boolean includeArchivedRows,
                        final int orgId, final IGLSQLModifier sqlModifier) {
  try {
    final GLSQL sql = GLSQL.select();
    sql.from(table);
    sql.orderBy(orderByClause);
    if (sqlModifier != null) {

    }
    if (orgId > 0) {
      sql.whereAddParens();
      sql.whereAnd(0, "OrgId=" + orgId, 0);
    }
    if (!includeArchivedRows) {
      sql.whereAddParens();
      sql.whereAnd(0, "ArchiveDate is null", 0);
    }
    sql.executeSelect(listStore, new IGLSQLSelectCallback() {
      @Override
      public void onFailure(final Throwable t) {
        GLLog.popup(30, table + " load failed: " + t.getMessage());
      }
      @Override
      public void onSuccess() {
        GLLog.popup(5, table + " load succeeded (" + listStore.size() + " rows)");
      }
    });
  }
  catch (final GLDBException dbe) {
    // todo: something useful ... GLClientUtil.getRemoteService().log(logLevel, location, message, callback);
  }
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