package org.greatlogic.glgwt.client.core;

import java.math.BigDecimal;
import java.util.Date;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLEnums.EGLColumnDataType;
import com.google.gwt.i18n.client.NumberFormat;
import com.sencha.gxt.widget.core.client.form.BigDecimalField;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.BigDecimalPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;

public class GLFieldInitializers {
//--------------------------------------------------------------------------------------------------
private static final DateTimePropertyEditor DatePropertyEditor;
private static final DateTimePropertyEditor DateTimePropertyEditor;
private static final String                 Zeroes;
//--------------------------------------------------------------------------------------------------
static {
  DatePropertyEditor = new DateTimePropertyEditor("MM/dd/yyyy");
  DateTimePropertyEditor = new DateTimePropertyEditor("MM/dd/yyyy hh:mm:ss");
  Zeroes = "0000000000000000000000000000000000000000";
}
//--------------------------------------------------------------------------------------------------
public static void initialize(final CheckBox checkBox, final IGLColumn column) {
  final Validator<?> validator = GLClientUtil.getValidators().getColumnValidator(column);
  if (validator != null) {
    checkBox.addValidator((Validator<Boolean>)validator);
  }
}
//--------------------------------------------------------------------------------------------------
public static void initialize(final BigDecimalField bigDecimalField, final IGLColumn column) {
  final NumberFormat format;
  if (column.getDataType() == EGLColumnDataType.Currency) {
    format = NumberFormat.getFormat("0.00");
  }
  else {
    format = NumberFormat.getFormat("#0." + //
                                    Zeroes.substring(0, column.getDecimalPlacesOrLength()));
  }
  bigDecimalField.setPropertyEditor(new BigDecimalPropertyEditor(format));
  final Validator<?> validator = GLClientUtil.getValidators().getColumnValidator(column);
  if (validator != null) {
    bigDecimalField.addValidator((Validator<BigDecimal>)validator);
  }
  bigDecimalField.setAllowBlank(column.getNullable());
  bigDecimalField.setClearValueOnParseError(false);
}
//--------------------------------------------------------------------------------------------------
public static void initialize(final DateField dateField, final IGLColumn column) {
  dateField.setPropertyEditor(column.getDataType() == EGLColumnDataType.Date ? DatePropertyEditor
                                                                        : DateTimePropertyEditor);
  final Validator<?> validator = GLClientUtil.getValidators().getColumnValidator(column);
  if (validator != null) {
    dateField.addValidator((Validator<Date>)validator);
  }
  dateField.setAllowBlank(column.getNullable());
  dateField.setClearValueOnParseError(false);
}
//--------------------------------------------------------------------------------------------------
public static void initialize(final IntegerField integerField, final IGLColumn column) {
  final Validator<?> validator = GLClientUtil.getValidators().getColumnValidator(column);
  if (validator != null) {
    integerField.addValidator((Validator<Integer>)validator);
  }
  integerField.setAllowBlank(column.getNullable());
  integerField.setClearValueOnParseError(false);
}
//--------------------------------------------------------------------------------------------------
public static void initialize(final TextField textField, final IGLColumn column) {
  final Validator<?> validator = GLClientUtil.getValidators().getColumnValidator(column);
  if (validator != null) {
    textField.addValidator((Validator<String>)validator);
  }
  textField.setAllowBlank(column.getNullable());
  textField.setClearValueOnParseError(false);
}
//--------------------------------------------------------------------------------------------------
}