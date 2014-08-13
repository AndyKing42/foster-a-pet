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
import org.greatlogic.glgwt.shared.GLLoginResponse;
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
@UiField
FieldLabel                  errorMessageLabel;
@UiField
TextField                   loginNameField;
@UiField
TextButton                  okButton;
@UiField
PasswordField               passwordField;
@UiField
Window                      window;

private boolean             _firstTime;
private AsyncCallback<Void> _loginSuccessfulCallback;
private final String        _windowHeadingText;
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
public void logIn(final AsyncCallback<Void> loginSuccessfulCallback) {
  _loginSuccessfulCallback = loginSuccessfulCallback;
  window.setHeadingText(_windowHeadingText);
  passwordField.setValue("");
  window.setFocusWidget(loginNameField);
  window.show();
}
//--------------------------------------------------------------------------------------------------
public void logInUsingNameAndPassword(final String loginName, final String password) {
  final AsyncCallback<GLLoginResponse> callback = new AsyncCallback<GLLoginResponse>() {
    @Override
    public void onFailure(final Throwable caught) {
      GLLog.popup(10, "Login failed");
      if (!_firstTime) {
        errorMessageLabel.setText("Login failed ... please try again");
      }
      _firstTime = false;
      logIn(_loginSuccessfulCallback);
    }
    @Override
    public void onSuccess(final GLLoginResponse response) {
      if (!response.getSucceeded()) {
        GLLog.popup(10, "Login failed");
        if (!_firstTime) {
          errorMessageLabel.setText("Login failed ... please try again");
        }
        _firstTime = false;
        logIn(_loginSuccessfulCallback);
        return;
      }
      GLLog.popup(10, "Login succeeded:" + response);
      errorMessageLabel.setText("");
      GLClientUtil.setSessionToken(response.getSessionToken());
      Cookies.setCookie(GLClientUtil.SessionTokenCookie, response.getSessionToken());
      window.hide();
      if (_loginSuccessfulCallback != null) {
        _loginSuccessfulCallback.onSuccess(null);
        _loginSuccessfulCallback = null;
      }
    }
  };
  GLClientUtil.getRemoteService().login(loginName, password,
                                        Cookies.getCookie(GLClientUtil.SessionTokenCookie),
                                        callback);
}
//--------------------------------------------------------------------------------------------------
@UiHandler("okButton")
public void onOKButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  final String loginName = loginNameField.getValue();
  final String password = passwordField.getValue();
  if (loginName.isEmpty() || password.isEmpty()) {
    GLLog.popup(10, "Login name and password are required");
    errorMessageLabel.setText("Login name and password are required");
    return;
  }
  logInUsingNameAndPassword(loginName, password);
}
//--------------------------------------------------------------------------------------------------
}