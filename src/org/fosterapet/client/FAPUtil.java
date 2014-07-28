package org.fosterapet.client;

import org.fosterapet.client.widget.MainLayoutWidget;
import org.greatlogic.glgwt.client.core.GLClientUtil;

public class FAPUtil {
//--------------------------------------------------------------------------------------------------
public static ClientFactory _clientFactory;
//--------------------------------------------------------------------------------------------------
public static ClientFactory getClientFactory() {
  return _clientFactory;
}
//--------------------------------------------------------------------------------------------------
static void initialize() {
  _clientFactory = new ClientFactoryUI();
  GLClientUtil.initialize("Foster A Pet", _clientFactory);
  _clientFactory.setMainLayoutWidget(new MainLayoutWidget());
  _clientFactory.getMainLayoutWidget().getAppTabPanelWidget().createPetGrid(false, true, true);
}
//--------------------------------------------------------------------------------------------------
}