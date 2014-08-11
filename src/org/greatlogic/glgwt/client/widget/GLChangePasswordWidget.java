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
import org.greatlogic.glgwt.shared.GLChangePasswordResponse;
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

public class GLChangePasswordWidget extends Window {
//--------------------------------------------------------------------------------------------------
@UiField
FieldLabel                  errorMessageLabel;
@UiField
PasswordField               newPasswordField;
@UiField
TextButton                  okButton;
@UiField
PasswordField               oldPasswordField;
@UiField
PasswordField               repeatNewPasswordField;
@UiField
Window                      window;

private AsyncCallback<Void> _changePasswordSuccessfulCallback;
private boolean             _firstTime;
private int                 _personId;
private final String        _windowHeadingText;
//==================================================================================================
interface IGLLoginWidgetBinder extends UiBinder<Widget, GLChangePasswordWidget> { //
}
//==================================================================================================
public GLChangePasswordWidget(final String windowHeadingText) {
  _windowHeadingText = windowHeadingText;
  final IGLLoginWidgetBinder binder = GWT.create(IGLLoginWidgetBinder.class);
  binder.createAndBindUi(this);
}
//--------------------------------------------------------------------------------------------------
public void changePassword(final int personId,
                           final AsyncCallback<Void> changePasswordSuccessfulCallback) {
  _personId = personId;
  _changePasswordSuccessfulCallback = changePasswordSuccessfulCallback;
  window.setHeadingText(_windowHeadingText);
  newPasswordField.setValue("");
  repeatNewPasswordField.setValue("");
  window.show();
}
//--------------------------------------------------------------------------------------------------
private void changePassword(final String oldPassword, final String newPassword) {
  final AsyncCallback<GLChangePasswordResponse> callback;
  callback = new AsyncCallback<GLChangePasswordResponse>() {
    @Override
    public void onFailure(final Throwable caught) {
      GLLog.popup(10, "Change password failed");
      if (!_firstTime) {
        errorMessageLabel.setText("Change password failed (" + caught.getMessage() + ")");
      }
      _firstTime = false;
      changePassword(_personId, _changePasswordSuccessfulCallback);
    }
    @Override
    public void onSuccess(final GLChangePasswordResponse response) {
      if (!response.getSucceeded()) {
        GLLog.popup(10, "Login failed - " + response.getFailureReason());
        if (!_firstTime) {
          errorMessageLabel.setText("Change password failed - " + response.getFailureReason());
        }
        _firstTime = false;
        changePassword(_personId, _changePasswordSuccessfulCallback);
        return;
      }
      GLLog.popup(10, "Change password succeeded:" + response);
      errorMessageLabel.setText("");
      GLClientUtil.setSessionToken(response.getSessionToken());
      Cookies.setCookie(GLClientUtil.SessionTokenCookie, response.getSessionToken());
      window.hide();
      if (_changePasswordSuccessfulCallback != null) {
        _changePasswordSuccessfulCallback.onSuccess(null);
        _changePasswordSuccessfulCallback = null;
      }
    }
  };
  GLClientUtil.getRemoteService().changePassword(_personId, oldPassword, newPassword, callback);
}
//--------------------------------------------------------------------------------------------------
@UiHandler("okButton")
public void onOKButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  if (!repeatNewPasswordField.getValue().equals(newPasswordField.getValue())) {
    GLLog.popup(20, "The new password entries don't match");
    errorMessageLabel.setText("The new password entries don't match");
    return;
  }
  changePassword(oldPasswordField.getValue(), newPasswordField.getValue());
}
//--------------------------------------------------------------------------------------------------
}