package org.greatlogic.glgwt.client.event;
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
import org.greatlogic.glgwt.client.event.GLLoginSuccessfulEvent.IGLLoginSuccessfulEventHandler;
import org.greatlogic.glgwt.shared.GLLoginResponse;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

public class GLLoginSuccessfulEvent extends Event<IGLLoginSuccessfulEventHandler> {
//--------------------------------------------------------------------------------------------------
public static final Type<IGLLoginSuccessfulEventHandler> LoginSuccessfulEventType;

private final GLLoginResponse                            _response;
//==================================================================================================
public interface IGLLoginSuccessfulEventHandler extends EventHandler {
public void onLoginSuccessfulEvent(final GLLoginSuccessfulEvent loginSuccessfulEvent);
}
//==================================================================================================
static {
  LoginSuccessfulEventType = new Type<IGLLoginSuccessfulEventHandler>();
}
//--------------------------------------------------------------------------------------------------
public GLLoginSuccessfulEvent(final GLLoginResponse response) {
  _response = response;
}
//--------------------------------------------------------------------------------------------------
@Override
protected void dispatch(final IGLLoginSuccessfulEventHandler handler) {
  handler.onLoginSuccessfulEvent(this);
}
//--------------------------------------------------------------------------------------------------
@Override
public Type<IGLLoginSuccessfulEventHandler> getAssociatedType() {
  return LoginSuccessfulEventType;
}
//--------------------------------------------------------------------------------------------------
public GLLoginResponse getResponse() {
  return _response;
}
//--------------------------------------------------------------------------------------------------
@Override
public String toString() {
  return "LoginSuccessfulEvent";
}
//--------------------------------------------------------------------------------------------------
}