package org.greatlogic.glgwt.client.widget;
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
import org.greatlogic.glgwt.client.core.GLLog;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;

public class GLLoginWidget extends Window {
//--------------------------------------------------------------------------------------------------
protected static final String SessionTokenCookie = "SessionToken";

@UiField
FieldLabel                    errorMessageLabel;
@UiField
TextField                     loginNameField;
@UiField
TextButton                    okButton;
@UiField
PasswordField                 passwordField;
@UiField
Window                        window;

private boolean               _firstTime;
private final String          _windowHeadingText;
//==================================================================================================
interface IGLLoginWidgetBinder extends UiBinder<Widget, GLLoginWidget> { //
}
//==================================================================================================
public GLLoginWidget(final String windowHeadingText) {
  _windowHeadingText = windowHeadingText;
  final IGLLoginWidgetBinder binder = GWT.create(IGLLoginWidgetBinder.class);
  binder.createAndBindUi(this);
}
//--------------------------------------------------------------------------------------------------
public void logIn() {
  window.setHeadingText(_windowHeadingText);
  window.show();
}
//--------------------------------------------------------------------------------------------------
public void logIn(final String loginName, final String password) {
  final AsyncCallback<String> callback = new AsyncCallback<String>() {
    @Override
    public void onFailure(final Throwable caught) {
      GLLog.popup(10, "Login failed");
      if (!_firstTime) {
        errorMessageLabel.setText("Login failed ... please try again");
      }
      _firstTime = false;
      logIn();
    }
    @Override
    public void onSuccess(final String result) {
      if (result.isEmpty()) {
        GLLog.popup(10, "Login failed");
        errorMessageLabel.setText("Login failed ... please try again");
        return;
      }
      final int tildeIndex = result.lastIndexOf('~');
      if (tildeIndex < 1 || tildeIndex == result.length() - 1) {
        return;
      }
      final String sessionToken = result.substring(0, tildeIndex);
      final int personId = GLClientUtil.stringToInt(result.substring(tildeIndex + 1), -1);
      if (personId <= 0) {
        return;
      }
      GLLog.popup(10, "Login succeeded:" + result);
      errorMessageLabel.setText("");
      Cookies.setCookie(SessionTokenCookie, sessionToken);
      window.hide();
    }
  };
  final String currentSessionToken = Cookies.getCookie(SessionTokenCookie);
  GLClientUtil.getRemoteService().login(loginName, password, currentSessionToken, callback);
  //  _loginPersonId = 0;
  //  GLClientUtil.getRemoteService().login(loginName, password, new AsyncCallback<Integer>() {
  //    @Override
  //    public void onFailure(final Throwable caught) {
  //
  //    }
  //    @Override
  //    public void onSuccess(final Integer personId) {
  //      _loginPersonId = personId;
  //      _clientFactory.getFAPEventBus().fireEvent("PersonCache.login",
  //                                                new LoginResponseEvent(personId));
  //      _clientFactory.getRequestFactoryResender().resend();
  //    }
  //  });
}
//--------------------------------------------------------------------------------------------------
@UiHandler("okButton")
public void onOKButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  logIn(loginNameField.getValue(), passwordField.getValue());
}
//--------------------------------------------------------------------------------------------------
}