package org.fosterapet.client.widget;

import org.fosterapet.shared.IDBEnums.EFAPTable;
import org.fosterapet.shared.IDBEnums.Pet;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.editor.GLRecordEditorDriver;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

public class PetDetailsWidget extends Composite {
//--------------------------------------------------------------------------------------------------
@UiField
FlowLayoutContainer                flowLayoutContainer;

private final GLRecordEditorDriver _editorDriver;
private final GLRecord             _pet;
//==================================================================================================
interface PetDetailsWidgetUiBinder extends UiBinder<Widget, PetDetailsWidget> { //
}
//==================================================================================================
public PetDetailsWidget(final GLRecord pet) {
  _pet = pet;
  final PetDetailsWidgetUiBinder uiBinder = GWT.create(PetDetailsWidgetUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
  flowLayoutContainer.setScrollMode(ScrollMode.AUTO);
  _editorDriver = new GLRecordEditorDriver(EFAPTable.class);
  _editorDriver.addWidget(flowLayoutContainer);
  _editorDriver.edit(_pet);
}
//--------------------------------------------------------------------------------------------------
@UiHandler({"updatePetButton"})
public void onUpdatePetButtonSelect(@SuppressWarnings("unused") final SelectEvent event) {
  _pet.set(Pet.PetName, "Hey " + GLClientUtil.getRandomInt(1000));
}
//--------------------------------------------------------------------------------------------------
}