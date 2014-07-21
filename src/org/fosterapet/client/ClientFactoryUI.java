package org.fosterapet.client;

import org.fosterapet.client.widget.MainLayoutWidget;

public class ClientFactoryUI extends ClientFactory {
//--------------------------------------------------------------------------------------------------
public ClientFactoryUI() {
  super();
}
//--------------------------------------------------------------------------------------------------
@Override
public void hidePleaseWait() {

}
//--------------------------------------------------------------------------------------------------
@Override
public void login() {

}
//--------------------------------------------------------------------------------------------------
@Override
public void setMainLayoutWidget(final MainLayoutWidget mainLayoutWidget) {
  _mainLayoutWidget = mainLayoutWidget;
}
//--------------------------------------------------------------------------------------------------
@Override
public void showPleaseWait() {

}
//--------------------------------------------------------------------------------------------------
}