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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.TreeSet;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.IGLClientEnums.EGLContextMenuItemType;
import org.greatlogic.glgwt.client.core.IGLClientEnums.EGLGridContentPanelButtonType;
import org.greatlogic.glgwt.client.db.GLListStore;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.event.GLLookupTableLoadedEvent;
import org.greatlogic.glgwt.client.event.GLLookupTableLoadedEvent.IGLLookupTableLoadedEventHandler;
import org.greatlogic.glgwt.shared.GLRecordValidator;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLLookupType;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.Event.Type;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.TextMetrics;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.ProgressMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.HeaderContextMenuEvent;
import com.sencha.gxt.widget.core.client.event.HeaderContextMenuEvent.HeaderContextMenuHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.DoublePropertyEditor;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.IntegerPropertyEditor;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;
import com.sencha.gxt.widget.core.client.grid.filters.BooleanFilter;
import com.sencha.gxt.widget.core.client.grid.filters.DateFilter;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.ListFilter;
import com.sencha.gxt.widget.core.client.grid.filters.NumericFilter;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public abstract class GLGridWidget implements IsWidget {
//--------------------------------------------------------------------------------------------------
private static final int             _resizeColumnExtraPadding;
private static final TextMetrics     _textMetrics;

private GLGridColumnModel            _columnModel;
protected IGLColumn[]                _columns;
private final GLGridContentPanel     _contentPanel;
private GLGridContextMenu            _contextMenu;
protected Grid<GLRecord>             _grid;
private GLGridEditingWrapper         _gridEditingWrapper;
private GridFilters<GLRecord>        _gridFilters;
private final boolean                _inlineEditing;
protected GLListStore                _listStore;
private HandlerRegistration          _lookupTableLoadedHandlerRegistration;
private final String                 _noRowsMessage;
private final GLRecordValidator      _recordValidator;
private final boolean                _rowLevelCommits;
private final TreeSet<GLRecord>      _selectedRecordSet;
private GridSelectionModel<GLRecord> _selectionModel;
private final boolean                _useCheckBoxSelection;
//--------------------------------------------------------------------------------------------------
static {
  _resizeColumnExtraPadding = 10;
  _textMetrics = TextMetrics.get();
}
//--------------------------------------------------------------------------------------------------
protected GLGridWidget(final String headingText, final String noRowsMessage,
                       final GLRecordValidator recordValidator, final boolean inlineEditing,
                       final boolean useCheckBoxSelection, final boolean rowLevelCommits,
                       final IGLColumn[] columns) {
  super();
  _noRowsMessage = noRowsMessage == null ? "There are no results to display" : noRowsMessage;
  _recordValidator = recordValidator;
  _inlineEditing = inlineEditing;
  _useCheckBoxSelection = useCheckBoxSelection;
  _rowLevelCommits = rowLevelCommits;
  _columns = columns;
  _listStore = new GLListStore();
  _selectedRecordSet = new TreeSet<>();
  _contentPanel = new GLGridContentPanel(this, headingText);
  addContentPanelButtons();
  waitForComboBoxData();
}
//--------------------------------------------------------------------------------------------------
protected final void addContentPanelButton(final String buttonLabel,
                                           final EGLGridContentPanelButtonType contentPanelButtonType) {
  _contentPanel.addContentPanelButton(buttonLabel, contentPanelButtonType);
}
//--------------------------------------------------------------------------------------------------
protected final void addContentPanelButton(final String buttonLabel,
                                           final SelectHandler selectHandler) {
  _contentPanel.addContentPanelButton(buttonLabel, selectHandler);
}
//--------------------------------------------------------------------------------------------------
protected void addContentPanelButtons() {
  //
}
//--------------------------------------------------------------------------------------------------
protected final void addContextMenuItem(final String menuLabel,
                                        final EGLContextMenuItemType contextMenuItemType) {
  _contextMenu.addContextMenuItem(menuLabel, contextMenuItemType);
}
//--------------------------------------------------------------------------------------------------
protected final void addContextMenuItem(final String menuLabel,
                                        final IGLGridContextMenuSelectionHandler selectionHandler) {
  _contextMenu.addContextMenuItem(menuLabel, selectionHandler);
}
//--------------------------------------------------------------------------------------------------
protected void addContextMenuItems() {
  //
}
//--------------------------------------------------------------------------------------------------
protected final void addFilter(final IGLColumn column) {
  addFilter(column, null, null);
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
protected final void addFilter(final IGLColumn column, final Date minDate, final Date maxDate) {
  final ValueProvider<GLRecord, ?> vp = _columnModel.getValueProvider(column);
  Filter<GLRecord, ?> filter = null;
  if (column.getLookupType() != null && column.getLookupType().getTable() == null) {
    filter = createListFilter(column, vp);
  }
  else {
    switch (column.getDataType()) {
      case Boolean:
        filter = new BooleanFilter<GLRecord>((ValueProvider<GLRecord, Boolean>)vp);
        break;
      case Currency:
        filter = new NumericFilter<GLRecord, Double>((ValueProvider<GLRecord, Double>)vp, //
                                                     new DoublePropertyEditor());
        break;
      case Date:
        filter = new DateFilter<GLRecord>((ValueProvider<GLRecord, Date>)vp);
        if (minDate != null) {
          ((DateFilter<GLRecord>)filter).setMinDate(minDate);
        }
        if (maxDate != null) {
          ((DateFilter<GLRecord>)filter).setMaxDate(maxDate);
        }
        break;
      case DateTime:
        filter = new DateFilter<GLRecord>((ValueProvider<GLRecord, Date>)vp);
        if (minDate != null) {
          ((DateFilter<GLRecord>)filter).setMinDate(minDate);
        }
        if (maxDate != null) {
          ((DateFilter<GLRecord>)filter).setMaxDate(maxDate);
        }
        break;
      case Decimal:
        filter = new NumericFilter<GLRecord, Double>((ValueProvider<GLRecord, Double>)vp, //
                                                     new DoublePropertyEditor());
        break;
      case Int:
        if (column.getLookupType() == null || column.getLookupType().getTable() == null) {
          filter = new NumericFilter<GLRecord, Integer>((ValueProvider<GLRecord, Integer>)vp, //
                                                        new IntegerPropertyEditor());
        }
        else {
          filter = createListFilter(column, vp);
        }
        break;
      case String:
        filter = new StringFilter<GLRecord>((ValueProvider<GLRecord, String>)vp);
        break;
    }
  }
  addFilter(filter);
}
//--------------------------------------------------------------------------------------------------
protected final void addFilter(final Filter<GLRecord, ?> filter) {
  if (filter != null) {
    if (_gridFilters == null) {
      _gridFilters = new GridFilters<GLRecord>();
      _gridFilters.setLocal(true);
    }
    _gridFilters.addFilter(filter);
  }
}
//--------------------------------------------------------------------------------------------------
protected void addFilters() {
  //
}
//--------------------------------------------------------------------------------------------------
private void addHeaderContextMenuHandler() {
  final HeaderContextMenuHandler headerContextMenuHandler = new HeaderContextMenuHandler() {
    @Override
    public void onHeaderContextMenu(final HeaderContextMenuEvent headerContextMenuEvent) {
      MenuItem menuItem = new MenuItem("Size To Fit");
      menuItem.addSelectionHandler(new SelectionHandler<Item>() {
        @Override
        public void onSelection(final SelectionEvent<Item> selectionEvent) {
          resizeColumnToFit(headerContextMenuEvent.getColumnIndex());
          _grid.getView().refresh(true);
        }
      });
      headerContextMenuEvent.getMenu().add(menuItem);
      menuItem = new MenuItem("Size All To Fit");
      menuItem.addSelectionHandler(new SelectionHandler<Item>() {
        @Override
        public void onSelection(final SelectionEvent<Item> selectionEvent) {
          final ProgressMessageBox messageBox = new ProgressMessageBox("Size All Columns", //
                                                                       "Resizing Columns...");
          messageBox.setProgressText("Calculating...");
          messageBox.show();
          resizeNextColumn(messageBox, _useCheckBoxSelection ? 1 : 0, _grid.getColumnModel()
                                                                           .getColumnCount() - 1);
        }
      });
      headerContextMenuEvent.getMenu().add(menuItem);
    }
  };
  _grid.addHeaderContextMenuHandler(headerContextMenuHandler);
}
//--------------------------------------------------------------------------------------------------
/**
 * If there are lookup tables that are needed by any of the columns in the grid then the creation of
 * the grid must be deferred until all of those lookup tables have been loaded into the cache. The
 * lookup tables are added to the lookupTableSet; when the LookupLoadedEvent is fired the table
 * that's been loaded is removed from the set; when all tables have been loaded the grid is created.
 * @param lookupTableSet The set that contains the list of tables that need to be loaded prior to
 * creating the grid.
 */
private void addLookupTableLoadedEventHandler(final HashSet<IGLTable> lookupTableSet) {
  final IGLLookupTableLoadedEventHandler handler = new IGLLookupTableLoadedEventHandler() {
    @Override
    public void onLookupTableLoadedEvent(final GLLookupTableLoadedEvent lookupTableLoadedEvent) {
      lookupTableSet.remove(lookupTableLoadedEvent.getTable());
      if (lookupTableSet.size() == 0) {
        _lookupTableLoadedHandlerRegistration.removeHandler();
        _lookupTableLoadedHandlerRegistration = null;
        createGrid();
      }
    }
  };
  final Type<IGLLookupTableLoadedEventHandler> eventType;
  eventType = GLLookupTableLoadedEvent.LookupTableLoadedEventType;
  _lookupTableLoadedHandlerRegistration = GLClientUtil.getEventBus().addHandler(eventType, //
                                                                                handler);
}
//--------------------------------------------------------------------------------------------------
@Override
public Widget asWidget() {
  return _contentPanel;
}
//--------------------------------------------------------------------------------------------------
public void clearAllRowSelectCheckboxes() {
  if (_useCheckBoxSelection) {
    _selectedRecordSet.clear();
  }
  _grid.getView().refresh(false);
}
//--------------------------------------------------------------------------------------------------
private void createGrid() {
  _selectionModel = new GridSelectionModel<>();
  _columnModel = new GLGridColumnModel(this, _inlineEditing, _useCheckBoxSelection);
  _grid = new Grid<>(_listStore, _columnModel);
  _grid.setBorders(true);
  _grid.setColumnReordering(true);
  _grid.setLoadMask(true);
  _grid.setSelectionModel(_selectionModel);
  _grid.setView(createGridView());
  addHeaderContextMenuHandler();
  _gridEditingWrapper = new GLGridEditingWrapper(this, _inlineEditing, _recordValidator);
  addFilters();
  if (_gridFilters != null) {
    _gridFilters.initPlugin(_grid);
  }
  _contextMenu = new GLGridContextMenu(this);
  _grid.setContextMenu(_contextMenu.build());
  _contentPanel.add(_grid);
  _contentPanel.forceLayout();
}
//--------------------------------------------------------------------------------------------------
private GridView<GLRecord> createGridView() {
  final GridView<GLRecord> result = new GridView<>();
  result.setColumnLines(true);
  result.setEmptyText(_noRowsMessage);
  result.setForceFit(false);
  result.setStripeRows(true);
  return result;
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
private Filter<GLRecord, ?> createListFilter(final IGLColumn column,
                                             final ValueProvider<GLRecord, ?> vp) {
  final ListStore<String> listStore = new ListStore<String>(new ModelKeyProvider<String>() {
    @Override
    public String getKey(final String item) {
      return item;
    }
  });
  final IGLTable table = column.getLookupType().getTable();
  if (table == null) {
    final ArrayList<String> list =
                                   GLClientUtil.getLookupCache()
                                               .getAbbrevList(column.getLookupType());
    for (final String listEntry : list) {
      listStore.add(listEntry);
    }
  }
  else {
    final GLListStore recordListStore = GLClientUtil.getLookupCache().getListStore(table);
    for (int recordIndex = 0; recordIndex < recordListStore.size(); ++recordIndex) {
      listStore.add(recordListStore.get(recordIndex).asString(table.getComboboxColumnMap().get(1)));
    }
  }
  return new ListFilter<GLRecord, String>((ValueProvider<GLRecord, String>)vp, listStore);
}
//--------------------------------------------------------------------------------------------------
GLGridColumnModel getColumnModel() {
  return _columnModel;
}
//--------------------------------------------------------------------------------------------------
IGLColumn[] getColumns() {
  return _columns;
}
//--------------------------------------------------------------------------------------------------
Grid<GLRecord> getGrid() {
  return _grid;
}
//--------------------------------------------------------------------------------------------------
GLGridEditingWrapper getGridEditingWrapper() {
  return _gridEditingWrapper;
}
//--------------------------------------------------------------------------------------------------
public GLListStore getListStore() {
  return _listStore;
}
//--------------------------------------------------------------------------------------------------
boolean getRowLevelCommits() {
  return _rowLevelCommits;
}
//--------------------------------------------------------------------------------------------------
TreeSet<GLRecord> getSelectedRecordSet() {
  return _selectedRecordSet;
}
//--------------------------------------------------------------------------------------------------
GridSelectionModel<GLRecord> getSelectionModel() {
  return _selectionModel;
}
//--------------------------------------------------------------------------------------------------
public void requestRowDeleteConfirmation(final GridRowEditing<GLRecord> gridRowEditing) {
  final ConfirmMessageBox messageBox;
  messageBox = new ConfirmMessageBox("Delete Row", "Are you sure you want to delete this record?");
  messageBox.addDialogHideHandler(new DialogHideHandler() {
    @Override
    public void onDialogHide(final DialogHideEvent hideEvent) {
      if (hideEvent.getHideButton() == PredefinedButton.YES) {
        final GLRecord record = getSelectionModel().getSelectedItem();
        _listStore.remove(record);
        if (gridRowEditing != null) {
          gridRowEditing.cancelEditing();
        }
      }
    }
  });
  messageBox.show();
}
//--------------------------------------------------------------------------------------------------
private int resizeColumnGetWidth(final Object value, final DateTimeFormat dateTimeFormat) {
  if (value == null) {
    return 0;
  }
  final String valueAsString = dateTimeFormat == null ? value.toString() //
                                                     : dateTimeFormat.format((Date)value);
  return _textMetrics.getWidth(valueAsString);
}
//--------------------------------------------------------------------------------------------------
private void resizeColumnToFit(final int columnIndex) {
  final String columnName = _columns[columnIndex - (_useCheckBoxSelection ? 1 : 0)].toString();
  final GLColumnConfig<?> columnConfig = _columnModel.getColumnConfig(columnName);
  final DateTimeFormat dateTimeFormat = columnConfig.getDateTimeFormat();
  _textMetrics.bind(_grid.getView().getHeader().getAppearance().styles().head());
  int maxWidth = _textMetrics.getWidth(columnConfig.getHeader().asString()) + 6;
  if (_listStore.size() > 0) {
    final String className = _grid.getView().getCell(1, 1).getClassName();
    _textMetrics.bind(className);
    for (final GLRecord record : _listStore.getAll()) {
      final Object value = columnConfig.getValueProvider().getValue(record);
      final int width = resizeColumnGetWidth(value, dateTimeFormat) + _resizeColumnExtraPadding;
      maxWidth = width > maxWidth ? width : maxWidth;
    }
    for (final Store<GLRecord>.Record record : _listStore.getModifiedRecords()) {
      final Object value = record.getValue(columnConfig.getValueProvider());
      final int width = resizeColumnGetWidth(value, dateTimeFormat) + _resizeColumnExtraPadding;
      maxWidth = width > maxWidth ? width : maxWidth;
    }
  }
  columnConfig.setWidth(maxWidth < 40 ? 40 : maxWidth);
  if (_columnModel.isCheckbox(columnConfig)) {
    _columnModel.centerCheckBox(columnConfig);
  }
}
//--------------------------------------------------------------------------------------------------
private void resizeNextColumn(final ProgressMessageBox messageBox, final int columnIndex,
                              final int lastColumnIndex) {
  Scheduler.get().scheduleDeferred(new ScheduledCommand() {
    @Override
    public void execute() {
      resizeColumnToFit(columnIndex);
      if (columnIndex == lastColumnIndex) {
        messageBox.hide();
        _grid.getView().refresh(true);
        return;
      }
      messageBox.updateProgress((double)columnIndex / (lastColumnIndex + 1), "{0}% Complete");
      resizeNextColumn(messageBox, columnIndex + 1, lastColumnIndex);
    }
  });
}
//--------------------------------------------------------------------------------------------------
public void selectAllRowSelectCheckboxes() {
  if (_useCheckBoxSelection) {
    for (int recordIndex = 0; recordIndex < _listStore.size(); ++recordIndex) {
      _selectedRecordSet.add(_listStore.get(recordIndex));
    }
  }
  _grid.getView().refresh(false);
}
//--------------------------------------------------------------------------------------------------
private void waitForComboBoxData() {
  final HashSet<IGLTable> lookupTableSet = new HashSet<>();
  for (final IGLColumn column : _columns) {
    final IGLLookupType lookupType = column.getLookupType();
    if (lookupType != null) {
      final IGLTable table = lookupType.getTable();
      if (table != null) {
        lookupTableSet.add(table);
      }
    }
  }
  if (lookupTableSet.size() == 0) {
    createGrid();
  }
  else {
    addLookupTableLoadedEventHandler(lookupTableSet);
    // use a copy of the lookup table set to prevent a ConcurrentModificationException
    final IGLTable[] lookupTables = lookupTableSet.toArray(new IGLTable[lookupTableSet.size()]);
    for (final IGLTable table : lookupTables) {
      GLClientUtil.getLookupCache().reload(table, true, false);
    }
  }
}
//--------------------------------------------------------------------------------------------------
}