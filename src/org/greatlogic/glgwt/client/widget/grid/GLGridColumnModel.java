package org.greatlogic.glgwt.client.widget.grid;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.widget.grid.GLValueProviderClasses.GLBigDecimalValueProvider;
import org.greatlogic.glgwt.client.widget.grid.GLValueProviderClasses.GLBooleanValueProvider;
import org.greatlogic.glgwt.client.widget.grid.GLValueProviderClasses.GLDateValueProvider;
import org.greatlogic.glgwt.client.widget.grid.GLValueProviderClasses.GLForeignKeyValueProvider;
import org.greatlogic.glgwt.client.widget.grid.GLValueProviderClasses.GLIntegerValueProvider;
import org.greatlogic.glgwt.client.widget.grid.GLValueProviderClasses.GLStringValueProvider;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLSharedEnums.EGLColumnDataType;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.sencha.gxt.cell.core.client.NumberCell;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.event.ColumnWidthChangeEvent;
import com.sencha.gxt.widget.core.client.event.ColumnWidthChangeEvent.ColumnWidthChangeHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

class GLGridColumnModel extends ColumnModel<GLRecord> {
//--------------------------------------------------------------------------------------------------
private static final List<ColumnConfig<GLRecord, ?>> EmptyColumnConfigList;
private static final String                          RowSelectCheckboxColumnName;

private final HashSet<GLColumnConfig<?>>             _checkBoxSet;
private final TreeMap<String, GLColumnConfig<?>>     _columnConfigMap;           // column name -> GLColumnConfig
private final GLGridWidget                           _gridWidget;
private final boolean                                _inlineEditing;
private final boolean                                _useCheckBoxSelection;
//--------------------------------------------------------------------------------------------------
static {
  EmptyColumnConfigList = new ArrayList<>();
  RowSelectCheckboxColumnName = "RowSelectCheckbox";
}
//--------------------------------------------------------------------------------------------------
public GLGridColumnModel(final GLGridWidget gridWidget, final boolean inlineEditing,
                         final boolean useCheckBoxSelection) {
  super(EmptyColumnConfigList);
  _gridWidget = gridWidget;
  _inlineEditing = inlineEditing;
  _useCheckBoxSelection = useCheckBoxSelection;
  _checkBoxSet = new HashSet<>();
  _columnConfigMap = new TreeMap<>();
  createColumnConfigs();
  addCheckboxCentering();
}
//--------------------------------------------------------------------------------------------------
private void addCheckboxCentering() {
  addColumnWidthChangeHandler(new ColumnWidthChangeHandler() {
    @Override
    public void onColumnWidthChange(final ColumnWidthChangeEvent event) {
      final GLColumnConfig<?> columnConfig;
      columnConfig = (GLColumnConfig<?>)getColumns().get(event.getIndex());
      if (isCheckbox(columnConfig)) {
        centerCheckBox(columnConfig);
        _gridWidget.getGrid().getView().refresh(true);
      }
    }
  });
  for (final ColumnConfig<GLRecord, ?> columnConfig : _checkBoxSet) {
    centerCheckBox(columnConfig);
  }
}
//--------------------------------------------------------------------------------------------------
void centerCheckBox(final ColumnConfig<GLRecord, ?> columnConfig) {
  if (_inlineEditing) {
    final int leftPadding = (columnConfig.getWidth() - 12) / 2;
    columnConfig.setColumnTextStyle(SafeStylesUtils.fromTrustedString("padding: 3px 0px 0px " +
                                                                      leftPadding + "px;"));
  }
}
//--------------------------------------------------------------------------------------------------
void clearInvalidColumnConfigs() {
  for (final GLColumnConfig<?> columnConfig : _columnConfigMap.values()) {
    columnConfig.clearInvalid();
  }
}
//--------------------------------------------------------------------------------------------------
private GLColumnConfig<BigDecimal> createBigDecimalColumnConfig(final IGLColumn column) {
  final GLColumnConfig<BigDecimal> result;
  final ValueProvider<GLRecord, BigDecimal> valueProvider;
  valueProvider = new GLBigDecimalValueProvider(column, column.getDecimalPlacesOrLength());
  result = new GLColumnConfig<>(column, valueProvider, column.getTitle(), //
                                column.getDefaultGridColumnWidth());
  NumberFormat numberFormat;
  if (column.getDataType() == EGLColumnDataType.Currency) {
    numberFormat = NumberFormat.getSimpleCurrencyFormat();
  }
  else {
    numberFormat = NumberFormat.getDecimalFormat();
  }
  result.setCell(new NumberCell<BigDecimal>(numberFormat));
  return result;
}
//--------------------------------------------------------------------------------------------------
private GLColumnConfig<Boolean> createBooleanColumnConfig(final IGLColumn column) {
  final GLColumnConfig<Boolean> result;
  final ValueProvider<GLRecord, Boolean> valueProvider = new GLBooleanValueProvider(column);
  result = new GLColumnConfig<>(column, valueProvider, column.getTitle(), //
                                column.getDefaultGridColumnWidth());
  if (_inlineEditing) {
    result.setCell(new CheckBoxCell());
  }
  else {
    result.setCell(new SimpleSafeHtmlCell<Boolean>(new AbstractSafeHtmlRenderer<Boolean>() {
      @Override
      public SafeHtml render(final Boolean object) {
        // TODO: these should be drawn (use AbstractCell#render?)
        return SafeHtmlUtils.fromTrustedString(object ? "&#x2713;" : "&#x2717");
      }
    }));
  }
  result.setSortable(false);
  _checkBoxSet.add(result);
  return result;
}
//--------------------------------------------------------------------------------------------------
private void createColumnConfigs() {
  if (_useCheckBoxSelection) {
    createRowSelectColumnConfig();
  }
  for (final IGLColumn column : _gridWidget.getColumns()) {
    GLColumnConfig<?> columnConfig = null;
    switch (column.getDataType()) {
      case Boolean:
        columnConfig = createBooleanColumnConfig(column);
        break;
      case Currency:
        columnConfig = createBigDecimalColumnConfig(column);
        break;
      case Date:
        columnConfig = createDateTimeColumnConfig(column, "MM/dd/yyyy");
        break;
      case DateTime:
        columnConfig = createDateTimeColumnConfig(column, "MM/dd/yyyy hh:mma");
        break;
      case Decimal:
        columnConfig = createBigDecimalColumnConfig(column);
        break;
      case Int:
        if (column.getLookupType() == null || column.getLookupType().getTable() == null) {
          columnConfig = createIntegerColumnConfig(column);
        }
        else {
          columnConfig = createForeignKeyColumnConfig(column, column.getLookupType().getTable());
        }
        break;
      case String:
        columnConfig = createStringColumnConfig(column);
        break;
    }
    if (columnConfig != null) {
      columnConfig.setColumnIndex(configs.size());
      configs.add(columnConfig);
      _columnConfigMap.put(column.toString(), columnConfig);
    }
  }
}
//--------------------------------------------------------------------------------------------------
private GLColumnConfig<Date> createDateTimeColumnConfig(final IGLColumn column,
                                                        final String dateTimeFormatString) {
  final GLColumnConfig<Date> result;
  result = new GLColumnConfig<>(column, new GLDateValueProvider(column), column.getTitle(), //
                                column.getDefaultGridColumnWidth());
  final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(dateTimeFormatString);
  result.setDateTimeFormat(dateTimeFormat);
  result.setCell(new DateCell(dateTimeFormat));
  return result;
}
//--------------------------------------------------------------------------------------------------
private GLColumnConfig<String> createForeignKeyColumnConfig(final IGLColumn column,
                                                            final IGLTable lookupTable) {
  final GLColumnConfig<String> result;
  result = new GLColumnConfig<>(column, new GLForeignKeyValueProvider(lookupTable, column), //
                                column.getTitle(), column.getDefaultGridColumnWidth());
  result.setCell(new TextCell());
  return result;
}
//--------------------------------------------------------------------------------------------------
private GLColumnConfig<Integer> createIntegerColumnConfig(final IGLColumn column) {
  final GLColumnConfig<Integer> result;
  result = new GLColumnConfig<>(column, new GLIntegerValueProvider(column), column.getTitle(), //
                                column.getDefaultGridColumnWidth());
  result.setCell(new NumberCell<Integer>());
  return result;
}
//--------------------------------------------------------------------------------------------------
private GLColumnConfig<Boolean> createRowSelectCheckboxColumnConfig() {
  final GLColumnConfig<Boolean> result;
  final ValueProvider<GLRecord, Boolean> rowSelectValueProvider;
  rowSelectValueProvider = new ValueProvider<GLRecord, Boolean>() {
    @Override
    public String getPath() {
      return RowSelectCheckboxColumnName;
    }
    @Override
    public Boolean getValue(final GLRecord record) {
      return _gridWidget.getSelectedRecordSet().contains(record);
    }
    @Override
    public void setValue(final GLRecord record, final Boolean selected) { //
      GLLog.popup(10, "Select checkbox setValue");
    }
  };
  result = new GLColumnConfig<>(null, rowSelectValueProvider, "", 23);
  final CheckBoxCell checkBoxCell = new CheckBoxCell() {
    @Override
    protected void onClick(final XElement parent, final NativeEvent event) {
      super.onClick(parent, event);
      final Element eventTarget = Element.as(event.getEventTarget());
      if (eventTarget.getAttribute("type").equals("checkbox")) {
        final int rowIndex = _gridWidget.getGrid().getView().findRowIndex(eventTarget);
        if (rowIndex != -1) {
          final GLRecord record = _gridWidget.getListStore().get(rowIndex);
          if (!_gridWidget.getSelectedRecordSet().remove(record)) {
            _gridWidget.getSelectedRecordSet().add(record);
          }
        }
      }
    }
  };
  result.setCell(checkBoxCell);
  result.setFixed(true);
  result.setHideable(false);
  result.setMenuDisabled(true);
  result.setResizable(false);
  final SafeStyles textStyles = SafeStylesUtils.fromTrustedString("padding: 4px 5px 3px 5px;");
  result.setColumnTextStyle(textStyles);
  result.setSortable(false);
  return result;
}
//--------------------------------------------------------------------------------------------------
private void createRowSelectColumnConfig() {
  final GLColumnConfig<Boolean> rowSelectColumnConfig = createRowSelectCheckboxColumnConfig();
  configs.add(rowSelectColumnConfig);
  _columnConfigMap.put(RowSelectCheckboxColumnName, rowSelectColumnConfig);
}
//--------------------------------------------------------------------------------------------------
private GLColumnConfig<String> createStringColumnConfig(final IGLColumn column) {
  final GLColumnConfig<String> result;
  result = new GLColumnConfig<>(column, new GLStringValueProvider(column), column.getTitle(), //
                                column.getDefaultGridColumnWidth());
  result.setCell(new TextCell());
  return result;
}
//--------------------------------------------------------------------------------------------------
public HashSet<GLColumnConfig<?>> getCheckBoxSet() {
  return _checkBoxSet;
}
//--------------------------------------------------------------------------------------------------
public GLColumnConfig<?> getColumnConfig(final IGLColumn column) {
  return _columnConfigMap.get(column.toString());
}
//--------------------------------------------------------------------------------------------------
public GLColumnConfig<?> getColumnConfig(final String columnName) {
  return _columnConfigMap.get(columnName);
}
//--------------------------------------------------------------------------------------------------
public TreeMap<String, GLColumnConfig<?>> getColumnConfigMap() {
  return _columnConfigMap;
}
//--------------------------------------------------------------------------------------------------
public Collection<GLColumnConfig<?>> getColumnConfigs() {
  return _columnConfigMap.values();
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
public ValueProvider<GLRecord, ?> getValueProvider(final IGLColumn column) {
  return (ValueProvider<GLRecord, ?>)getColumnConfig(column).getValueProvider();
}
//--------------------------------------------------------------------------------------------------
public boolean isCheckbox(final GLColumnConfig<?> columnConfig) {
  return _checkBoxSet.contains(columnConfig);
}
//--------------------------------------------------------------------------------------------------
}