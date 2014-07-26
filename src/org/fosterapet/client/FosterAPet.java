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
import org.fosterapet.client.widget.MainLayoutWidget;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLListStore;
import org.greatlogic.glgwt.client.widget.grid.GLGridWidget;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class FosterAPet implements EntryPoint {
//--------------------------------------------------------------------------------------------------
@Override
public void onModuleLoad() {
  GLClientUtil.setUncaughtExceptionHandler(createModuleLoadCommand());
}
//--------------------------------------------------------------------------------------------------
private ScheduledCommand createModuleLoadCommand() {
  return new ScheduledCommand() {
    @Override
    public void execute() {
      final ClientFactory clientFactory = new ClientFactoryUI();
      GLClientUtil.initialize("Foster A Pet", clientFactory);
      clientFactory.setMainLayoutWidget(new MainLayoutWidget());
      final boolean loadTestData = false;
      clientFactory.getMainLayoutWidget().getAppTabPanelWidget().createPetGrid(false, true, true);
      final GLGridWidget gridWidget = GridWidgetManager.getPetGrid("Pets1");
      if (loadTestData) {
        final GLListStore petTypeListStore = new GLListStore();
        TestData.loadPetTypeTestData(petTypeListStore);
        TestData.loadPetTestData(gridWidget.getListStore());
      }
      RootLayoutPanel.get().add(clientFactory.getMainLayoutWidget());
      // todo: GLClientUtil.login();
    }
  };
}
//--------------------------------------------------------------------------------------------------
}