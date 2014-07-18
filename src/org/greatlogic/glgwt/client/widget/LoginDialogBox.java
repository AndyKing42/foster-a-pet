package org.greatlogic.glgwt.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class LoginDialogBox extends DialogBox {
//--------------------------------------------------------------------------------------------------
@UiField
Label                       errorMessageLabel;
@UiField
TextBox                     loginNameTextBox;
@UiField
Button                      okButton;
@UiField
PasswordTextBox             passwordTextBox;

private HandlerRegistration _loginResponseEventHandler;
//==================================================================================================
interface ILoginDialogBoxBinder extends UiBinder<Widget, LoginDialogBox> { //
} // interface ILoginDialogBoxBinder
//==================================================================================================
public LoginDialogBox() {
  final ILoginDialogBoxBinder binder = GWT.create(ILoginDialogBoxBinder.class);
  setHTML("Login");
  setWidget(binder.createAndBindUi(this));
}
//--------------------------------------------------------------------------------------------------
public void login() {
  setGlassEnabled(true);
  show();
  center();
}
//--------------------------------------------------------------------------------------------------
public void login(final String loginName, final String password) {
  //  _loginPersonId = 0;
  //  GLUtil.getRemoteService().login(loginName, password, new AsyncCallback<Integer>() {
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
public void onOKButtonClick(@SuppressWarnings("unused") final ClickEvent clickEvent) {
  //  _loginResponseEventHandler =
  //                               _clientFactory.getFAPEventBus()
  //                                             .addHandler(LoginResponseEvent.LoginResponseEventType,
  //                                                         this);
  //  _clientFactory.getCacheManager().getPersonCache()
  //                .login(loginNameTextBox.getText(), passwordTextBox.getText());
}
//--------------------------------------------------------------------------------------------------
//@Override
//public void onLoginResponseEvent(final LoginResponseEvent loginResponseEvent) {
//  _loginResponseEventHandler.removeHandler();
//  if (loginResponseEvent.getPersonId() > 0) {
//    hide();
//    return;
//  }
//  errorMessageLabel.setText("Login failed ... please try again");
//}
//--------------------------------------------------------------------------------------------------
}