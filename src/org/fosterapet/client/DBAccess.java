package org.fosterapet.client;

import org.fosterapet.client.widget.GridWidgetManager;
import org.fosterapet.client.widget.PetGridWidget;
import org.fosterapet.shared.IRemoteServiceAsync;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.Pet;
import org.fosterapet.shared.IFAPEnums.ETestDataOption;
import org.greatlogic.glgwt.client.core.GLDBException;
import org.greatlogic.glgwt.client.core.GLListStore;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.core.GLSQL;
import org.greatlogic.glgwt.client.core.IGLSQLSelectCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DBAccess {
//--------------------------------------------------------------------------------------------------
public static void loadPets(final GLListStore petListStore) {
  try {
    final GLSQL petSQL = GLSQL.select();
    petSQL.from(EFAPTable.Pet);
    petSQL.orderBy(EFAPTable.Pet, Pet.PetName, true);
    petSQL.executeSelect(petListStore, new IGLSQLSelectCallback() {
      @Override
      public void onFailure(final Throwable t) {
        GLLog.popup(30, "Pet loading failed: " + t.getMessage());
      }
      @Override
      public void onSuccess() {
        GLLog.popup(5, "Pets loaded successfully");
      }
    });
  }
  catch (final GLDBException dbe) {
    //    GLUtil.getRemoteService().log(logLevel, location, message, callback);
  }
}
//--------------------------------------------------------------------------------------------------
public static void recreateTables() {
  ClientFactory.Instance.getRemoteService().recreateTables(new AsyncCallback<Void>() {
    @Override
    public void onFailure(final Throwable t) {
      GLLog.popup(10, "Database table creation failed:" + t.getMessage());
    }
    @Override
    public void onSuccess(final Void result) {
      GLLog.popup(10, "Database table creation is complete");
      final PetGridWidget petGrid = GridWidgetManager.getPetGrid("Main");
      ClientFactory.Instance.getLookupCache().reloadAll();
      loadPets(petGrid.getListStore());
    }
  });
}
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
      final PetGridWidget petGrid = GridWidgetManager.getPetGrid("Main");
      ClientFactory.Instance.getLookupCache().reloadAll();
      loadPets(petGrid.getListStore());
    }
  });
}
//--------------------------------------------------------------------------------------------------
}