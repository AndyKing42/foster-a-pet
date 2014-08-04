package org.fosterapet.login;

import org.fosterapet.client.event.LoginResponseEvent.ILoginResponseEventHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event;

public class LoginResponseEvent extends Event<ILoginResponseEventHandler> {
//--------------------------------------------------------------------------------------------------
private final int                                    _personId;
//--------------------------------------------------------------------------------------------------
public static final Type<ILoginResponseEventHandler> LoginResponseEventType = new Type<ILoginResponseEventHandler>();
//==================================================================================================
public interface ILoginResponseEventHandler extends EventHandler {
public void onLoginResponseEvent(final LoginResponseEvent loginResponseEvent);
} // interface ILoginResponseEventHandler
//==================================================================================================
public LoginResponseEvent(final Integer personId) {
  super();
  _personId = personId;
} // LoginResponseEvent()
//--------------------------------------------------------------------------------------------------
@Override
protected void dispatch(final ILoginResponseEventHandler handler) {
  handler.onLoginResponseEvent(this);
} // dispatch()
//--------------------------------------------------------------------------------------------------
@Override
public Type<ILoginResponseEventHandler> getAssociatedType() {
  return LoginResponseEventType;
} // getAssociatedType()
//--------------------------------------------------------------------------------------------------
public int getPersonId() {
  return _personId;
} // getPersonId()
//--------------------------------------------------------------------------------------------------
}
