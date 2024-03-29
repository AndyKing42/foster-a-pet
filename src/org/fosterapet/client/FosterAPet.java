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
import org.greatlogic.glgwt.client.core.GLClientUtil;
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
      FAPUtil.initialize();
      RootLayoutPanel.get().add(FAPUtil.getClientFactory().getMainLayoutWidget());
    }
  };
}
//--------------------------------------------------------------------------------------------------
}