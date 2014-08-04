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
import org.fosterapet.client.widget.GridWidgetManager;
import org.fosterapet.client.widget.PetGridWidget;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.Pet;
import org.fosterapet.shared.IFAPEnums.ETestDataOption;
import org.fosterapet.shared.IRemoteServiceAsync;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.db.GLDBException;
import org.greatlogic.glgwt.client.db.GLListStore;
import org.greatlogic.glgwt.client.db.GLSQL;
import org.greatlogic.glgwt.client.db.IGLSQLSelectCallback;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DBAccess {
//--------------------------------------------------------------------------------------------------
public static void load(final GLListStore listStore, final IGLTable table,
                        final String orderByClause, final boolean includeArchivedRows) {
  try {
    final GLSQL sql = GLSQL.select();
    sql.from(table);
    sql.orderBy(orderByClause);
    sql.setIncludeArchivedRows(includeArchivedRows);
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
    //    GLClientUtil.getRemoteService().log(logLevel, location, message, callback);
  }
}
////--------------------------------------------------------------------------------------------------
//public static void loadPets(final GLListStore petListStore) {
//  try {
//    final GLSQL petSQL = GLSQL.select();
//    petSQL.from(EFAPTable.Pet);
//    petSQL.orderBy(EFAPTable.Pet, Pet.PetName, true);
//    petSQL.executeSelect(petListStore, new IGLSQLSelectCallback() {
//      @Override
//      public void onFailure(final Throwable t) {
//        GLLog.popup(30, "Pet loading failed: " + t.getMessage());
//      }
//      @Override
//      public void onSuccess() {
//        GLLog.popup(5, "Pets loaded successfully");
//      }
//    });
//  }
//  catch (final GLDBException dbe) {
//    //    GLClientUtil.getRemoteService().log(logLevel, location, message, callback);
//  }
//}
//--------------------------------------------------------------------------------------------------
public static void reloadTestData() {
  final IRemoteServiceAsync remoteService = ClientFactory.Instance.getRemoteService();
  remoteService.loadTestData(ETestDataOption.Reload.name(), new AsyncCallback<Void>() {
    @Override
    public void onFailure(final Throwable t) {
      GLLog.popup(10, "Test data reload failed:" + t.getMessage());
    }
    @Override
    public void onSuccess(final Void result) {
      GLLog.popup(10, "Test data reload is complete");
      final PetGridWidget petGrid = GridWidgetManager.getPetGrid("Pet1");
      ClientFactory.Instance.getLookupCache().reloadAll();
      load(petGrid.getListStore(), EFAPTable.Pet, Pet.PetName.name(), false);
    }
  });
}
//--------------------------------------------------------------------------------------------------
}