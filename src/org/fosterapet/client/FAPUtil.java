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
import org.fosterapet.client.widget.MainLayoutWidget;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.event.GLLoginSuccessfulEvent;
import org.greatlogic.glgwt.client.event.GLLoginSuccessfulEvent.IGLLoginSuccessfulEventHandler;

public class FAPUtil {
//--------------------------------------------------------------------------------------------------
public static FAPClientFactory _clientFactory;
//--------------------------------------------------------------------------------------------------
private static IGLLoginSuccessfulEventHandler createLoginSuccessfulEventHandler() {
  return new IGLLoginSuccessfulEventHandler() {
    @Override
    public void onLoginSuccessfulEvent(final GLLoginSuccessfulEvent loginSuccessfulEvent) {
      //      save_the_person_id_and_org_id();
    }
  };
}
//--------------------------------------------------------------------------------------------------
public static FAPClientFactory getClientFactory() {
  return _clientFactory;
}
//--------------------------------------------------------------------------------------------------
static void initialize() {
  _clientFactory = new FAPClientFactoryUI();
  GLClientUtil.initialize("Foster A Pet", _clientFactory, EFAPTable.class, "Foster A Pet Login",
                          createLoginSuccessfulEventHandler());
  _clientFactory.setMainLayoutWidget(new MainLayoutWidget());
  // todo: restore the user's prior state? _clientFactory.getMainLayoutWidget().getAppTabPanelWidget().createPetGrid(false, true, true);
}
//--------------------------------------------------------------------------------------------------
}