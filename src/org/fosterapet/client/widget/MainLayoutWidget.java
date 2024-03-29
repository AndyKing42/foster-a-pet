package org.fosterapet.client.widget;
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
import org.fosterapet.client.FAPUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class MainLayoutWidget extends Composite {
//--------------------------------------------------------------------------------------------------
@UiField
AppTabPanelWidget       appTabPanelWidget;
@UiField
VerticalLayoutContainer mainLayoutTopLevelContainer;
@UiField
MainMenuWidget          mainMenuWidget;
//==================================================================================================
interface MainLayoutWidgetUiBinder extends UiBinder<Widget, MainLayoutWidget> { //
}
//==================================================================================================
public MainLayoutWidget() {
  super();
  final MainLayoutWidgetUiBinder uiBinder = GWT.create(MainLayoutWidgetUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
  FAPUtil.getClientFactory().setAppTabPanelWidget(appTabPanelWidget);
}
//--------------------------------------------------------------------------------------------------
public AppTabPanelWidget getAppTabPanelWidget() {
  return appTabPanelWidget;
}
//--------------------------------------------------------------------------------------------------
}