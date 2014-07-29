package org.greatlogic.glgwt.client.editor;

import java.util.List;
import javax.validation.ConstraintViolation;
import org.greatlogic.glgwt.client.core.GLRecord;
import com.google.gwt.editor.client.EditorDriver;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.EditorVisitor;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.event.AddEvent;
import com.sencha.gxt.widget.core.client.event.AddEvent.AddHandler;
import com.sencha.gxt.widget.core.client.event.RemoveEvent;
import com.sencha.gxt.widget.core.client.event.RemoveEvent.RemoveHandler;

public class GLRecordEditorDriver implements EditorDriver<GLRecord> {
//--------------------------------------------------------------------------------------------------
@Override
public void accept(final EditorVisitor visitor) {
  // todo Auto-generated method stub
}
//--------------------------------------------------------------------------------------------------
private void addChildWidgets(final Container container) {
  for (final Widget childWidget : container) {
    if (childWidget instanceof Container) {
      addChildWidgets((Container)childWidget);
    }
    else {
      addWidget(childWidget);
    }
  }
}
//--------------------------------------------------------------------------------------------------
private void addWidget(final Widget widget) {
  if (widget_represents_a_column()) {
    add_widget();
  }
}
//--------------------------------------------------------------------------------------------------
public void edit(final GLRecord record) {

}
//--------------------------------------------------------------------------------------------------
@Override
public GLRecord flush() {
  // todo Auto-generated method stub
  return null;
}
//--------------------------------------------------------------------------------------------------
@Override
public List<EditorError> getErrors() {
  // todo Auto-generated method stub
  return null;
}
//--------------------------------------------------------------------------------------------------
@Override
public boolean hasErrors() {
  // todo Auto-generated method stub
  return false;
}
//--------------------------------------------------------------------------------------------------
public void initialize(final Container container) {
  addChildWidgets(container);
  container.addAddHandler(new AddHandler() {
    @Override
    public void onAdd(final AddEvent event) {
      final Widget widget = event.getWidget();
      if (widget instanceof Container) {
        addChildWidgets((Container)widget);
      }
      else {
        addWidget(widget);
      }
    }
  });
  container.addRemoveHandler(new RemoveHandler() {
    @Override
    public void onRemove(final RemoveEvent event) {
      // todo Auto-generated method stub

    }
  });
}
//--------------------------------------------------------------------------------------------------
@Override
public boolean isDirty() {
  // todo Auto-generated method stub
  return false;
}
//--------------------------------------------------------------------------------------------------
@Override
public boolean setConstraintViolations(final Iterable<ConstraintViolation<?>> violations) {
  // todo Auto-generated method stub
  return false;
}
//--------------------------------------------------------------------------------------------------
}