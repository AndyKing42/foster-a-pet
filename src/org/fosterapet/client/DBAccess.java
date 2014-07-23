package org.fosterapet.client;

import org.fosterapet.client.widget.GridWidgetManager;
import org.fosterapet.client.widget.PetGridWidget;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.Pet;
import org.fosterapet.shared.IFAPEnums.ETestDataOption;
import org.fosterapet.shared.IRemoteServiceAsync;
import org.greatlogic.glgwt.client.core.GLDBException;
import org.greatlogic.glgwt.client.core.GLListStore;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.core.GLSQL;
import org.greatlogic.glgwt.client.core.IGLSQLSelectCallback;
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