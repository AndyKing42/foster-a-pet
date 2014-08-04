package org.fosterapet.login;

import org.fosterapet.client.ClientFactoryBase;
import org.fosterapet.client.event.LoginResponseEvent;
import org.fosterapet.client.event.LoginResponseEvent.ILoginResponseEventHandler;
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

public class LoginDialogBox extends DialogBox implements ILoginResponseEventHandler {
//--------------------------------------------------------------------------------------------------
@UiField
Label                           errorMessageLabel;
@UiField
TextBox                         loginNameTextBox;
@UiField
Button                          okButton;
@UiField
PasswordTextBox                 passwordTextBox;

private final ClientFactoryBase _clientFactory;
private HandlerRegistration     _loginResponseEventHandler;
//==================================================================================================
interface ILoginDialogBoxBinder extends UiBinder<Widget, LoginDialogBox> { //
} // interface ILoginDialogBoxBinder
//==================================================================================================
public LoginDialogBox(final ClientFactoryBase clientFactory) {
  _clientFactory = clientFactory;
  final ILoginDialogBoxBinder binder = GWT.create(ILoginDialogBoxBinder.class);
  setHTML("Login");
  setWidget(binder.createAndBindUi(this));
} // LoginDialogBox()
//--------------------------------------------------------------------------------------------------
public void login() {
  setGlassEnabled(true);
  show();
  center();
} // login()
//--------------------------------------------------------------------------------------------------
@UiHandler("okButton")
public void onOKButtonClick(@SuppressWarnings("unused") final ClickEvent clickEvent) {
  _loginResponseEventHandler = _clientFactory.getFAPEventBus().addHandler(LoginResponseEvent.LoginResponseEventType,
                                                                          this);
  _clientFactory.getCacheManager().getPersonCache().login(loginNameTextBox.getText(),
                                                          passwordTextBox.getText());
} // onOKButtonClick()
//--------------------------------------------------------------------------------------------------
@Override
public void onLoginResponseEvent(final LoginResponseEvent loginResponseEvent) {
  _loginResponseEventHandler.removeHandler();
  if (loginResponseEvent.getPersonId() > 0) {
    hide();
    return;
  }
  errorMessageLabel.setText("Login failed ... please try again");
} // onLoginResponseEvent()
//--------------------------------------------------------------------------------------------------
}