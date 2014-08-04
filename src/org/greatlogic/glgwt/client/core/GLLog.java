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
import org.greatlogic.glgwt.shared.IGLEnums.EGLLogLevel;
import org.greatlogic.glgwt.shared.IGLRemoteServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.info.DefaultInfoConfig;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.info.InfoConfig;

public class GLLog {
//--------------------------------------------------------------------------------------------------
private static IGLRemoteServiceAsync _remoteService;
//--------------------------------------------------------------------------------------------------
static {
  _remoteService = GLClientUtil.getRemoteService();
}
//--------------------------------------------------------------------------------------------------
public static void critical(final String location, final String message) {
  log(EGLLogLevel.Critical, location, message);
}
//--------------------------------------------------------------------------------------------------
public static void debug(final String location, final String message) {
  log(EGLLogLevel.Debug, location, message);
}
//--------------------------------------------------------------------------------------------------
public static void infoDetail(final String location, final String message) {
  log(EGLLogLevel.InfoDetail, location, message);
}
//--------------------------------------------------------------------------------------------------
public static void infoSummary(final String location, final String message) {
  log(EGLLogLevel.InfoSummary, location, message);
}
//--------------------------------------------------------------------------------------------------
public static void log(final EGLLogLevel logLevel, final String location, final String message) {
  if (_remoteService != null) {
    _remoteService.log(logLevel.getPriority(), location, message, new AsyncCallback<Void>() {
      @Override
      public void onSuccess(final Void result) {/**/}
      @Override
      public void onFailure(final Throwable caught) {/**/}
    });
  }
}
//--------------------------------------------------------------------------------------------------
public static void major(final String location, final String message) {
  log(EGLLogLevel.Major, location, message);
}
//--------------------------------------------------------------------------------------------------
public static void minor(final String location, final String message) {
  log(EGLLogLevel.Minor, location, message);
}
//--------------------------------------------------------------------------------------------------
public static void popup(final int seconds, final String message) {
  final InfoConfig infoConfig = new DefaultInfoConfig("", message == null ? "null" : message);
  infoConfig.setDisplay(seconds * 1000);
  final Info info = new Info();
  info.show(infoConfig);
}
//--------------------------------------------------------------------------------------------------
}