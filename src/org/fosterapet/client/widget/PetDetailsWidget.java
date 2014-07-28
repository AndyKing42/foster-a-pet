package org.fosterapet.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;

public class PetDetailsWidget extends Composite {
//--------------------------------------------------------------------------------------------------
@UiField
FlowLayoutContainer flowLayoutContainer;
//==================================================================================================
interface PetDetailsWidgetUiBinder extends UiBinder<Widget, PetDetailsWidget> { //
}
//==================================================================================================
public PetDetailsWidget() {
  final PetDetailsWidgetUiBinder uiBinder = GWT.create(PetDetailsWidgetUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
  flowLayoutContainer.setScrollMode(ScrollMode.AUTO);
}
//--------------------------------------------------------------------------------------------------
}