package org.greatlogic.glgwt.client.widget.grid;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;
import org.greatlogic.glgwt.client.core.GLClientUtil;
import org.greatlogic.glgwt.client.core.GLLog;
import org.greatlogic.glgwt.client.core.IGLClientEnums.EGLContextMenuItemType;
import org.greatlogic.glgwt.client.core.IGLClientEnums.EGLGridContentPanelButtonType;
import org.greatlogic.glgwt.client.db.GLDBException;
import org.greatlogic.glgwt.client.db.GLListStore;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.db.GLSQL;
import org.greatlogic.glgwt.client.db.IGLListStoreLoadedCallback;
import org.greatlogic.glgwt.shared.GLRecordValidator;
import org.greatlogic.glgwt.shared.IGLColumn;
import org.greatlogic.glgwt.shared.IGLTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.TextMetrics;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.ProgressMessageBox;
import com.sencha.gxt.widget.core.client.container.ResizeContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
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
import com.sencha.gxt.widget.core.client.tips.QuickTip;

public abstract class GLGridWidget extends Composite {
//--------------------------------------------------------------------------------------------------
private static final int             _resizeColumnExtraPadding;
private static final TextMetrics     _textMetrics;

@UiField(provided = true)
GLGridButtonContainer                buttonContainer;
@UiField(provided = true)
Grid<GLRecord>                       grid;
@UiField
VerticalLayoutContainer              topLevelContainer;

private GLGridColumnModel            _columnModel;
private GLGridContextMenu            _contextMenu;
private GLGridEditingWrapper         _gridEditingWrapper;
private GridFilters<GLRecord>        _gridFilters;
private final boolean                _inlineEditing;
protected GLListStore                _listStore;
private final String                 _noRowsMessage;
private ResizeContainer              _parentContainer;
private final GLRecordValidator      _recordValidator;
private final boolean                _rowLevelCommits;
private final TreeSet<GLRecord>      _selectedRecordSet;
private GridSelectionModel<GLRecord> _selectionModel;
protected GLSQL                      _sql;
protected IGLTable                   _table;
private final boolean                _useCheckBoxSelection;
//--------------------------------------------------------------------------------------------------
interface IGLGridWidgetUiBinder extends UiBinder<Widget, GLGridWidget> { //
}
//--------------------------------------------------------------------------------------------------
static {
  _resizeColumnExtraPadding = 10;
  _textMetrics = TextMetrics.get();
}
//--------------------------------------------------------------------------------------------------
public GLGridWidget(final IGLTable table, final String noRowsMessage,
                    final GLRecordValidator recordValidator, final boolean inlineEditing,
                    final boolean useCheckBoxSelection, final boolean rowLevelCommits,
                    final IGLColumn[] columns) throws GLDBException {
  super();
  _table = table;
  _noRowsMessage = noRowsMessage == null ? "There are no results to display" : noRowsMessage;
  _recordValidator = recordValidator;
  _inlineEditing = inlineEditing;
  _useCheckBoxSelection = useCheckBoxSelection;
  _rowLevelCommits = rowLevelCommits;
  _listStore = new GLListStore(getSQL(), true, columns);
  _selectedRecordSet = new TreeSet<>();
  buttonContainer = new GLGridButtonContainer(this);
  addButtons();
}
//--------------------------------------------------------------------------------------------------
protected final void addButton(final String buttonLabel,
                               final EGLGridContentPanelButtonType contentPanelButtonType) {
  buttonContainer.addButton(buttonLabel, contentPanelButtonType);
}
//--------------------------------------------------------------------------------------------------
protected final void addButton(final String buttonLabel, final SelectHandler selectHandler) {
  buttonContainer.addButton(buttonLabel, selectHandler);
}
//--------------------------------------------------------------------------------------------------
protected void addButtons() {
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
          grid.getView().refresh(true);
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
          resizeNextColumn(messageBox, _useCheckBoxSelection ? 1 : 0, grid.getColumnModel()
                                                                          .getColumnCount() - 1);
        }
      });
      headerContextMenuEvent.getMenu().add(menuItem);
    }
  };
  grid.addHeaderContextMenuHandler(headerContextMenuHandler);
}
//--------------------------------------------------------------------------------------------------
public void clearAllRowSelectCheckboxes() {
  if (_useCheckBoxSelection) {
    _selectedRecordSet.clear();
  }
  grid.getView().refresh(false);
}
//--------------------------------------------------------------------------------------------------
private void createGrid(final ResizeContainer parentContainer) {
  GLLog.popup(60, "createGrid for table:" + _table);
  _parentContainer = parentContainer;
  _selectionModel = new GridSelectionModel<>();
  _columnModel = new GLGridColumnModel(this, _inlineEditing, _useCheckBoxSelection);
  grid = new Grid<>(_listStore, _columnModel);
  grid.setBorders(false);
  grid.setColumnReordering(true);
  grid.setLoadMask(true);
  grid.setSelectionModel(_selectionModel);
  grid.setView(createGridView());
  addHeaderContextMenuHandler();
  _gridEditingWrapper = new GLGridEditingWrapper(this, _inlineEditing, _recordValidator);
  addFilters();
  if (_gridFilters != null) {
    _gridFilters.initPlugin(grid);
  }
  _contextMenu = new GLGridContextMenu(this);
  grid.setContextMenu(_contextMenu.build());
  new QuickTip(grid);
  final IGLGridWidgetUiBinder uiBinder = GWT.create(IGLGridWidgetUiBinder.class);
  initWidget(uiBinder.createAndBindUi(this));
  if (_parentContainer != null) {
    _parentContainer.add(this);
  }
  forceLayout();
}
//--------------------------------------------------------------------------------------------------
private GridView<GLRecord> createGridView() {
  final GridView<GLRecord> result = new GridView<>();
  // ********** Use the following for row-level tool tips: **********
  //  final GridView<GLRecord> result = new GridView<GLRecord>() {
  //    @Override
  //    protected void processRows(final int startRow, final boolean skipStripe) {
  //      super.processRows(startRow, skipStripe);
  //      final NodeList<Element> rows = getRows();
  //      for (int i = startRow, len = rows.getLength(); i < len; i++) {
  //        final Element row = rows.getItem(i);
  //        row.setAttribute("title", "hey " + i);
  //      }
  //    }
  //  };
  result.setColumnLines(true);
  result.setEmptyText(_noRowsMessage);
  result.setForceFit(false);
  result.setStripeRows(true);
  return result;
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
private Filter<GLRecord, ?> createListFilter(final IGLColumn column,
                                             final ValueProvider<GLRecord, ?> valueProvider) {
  final ListStore<String> listStore = new ListStore<String>(new ModelKeyProvider<String>() {
    @Override
    public String getKey(final String item) {
      return item;
    }
  });
  final IGLTable table = column.getLookupType().getTable();
  if (table == null) {
    final ArrayList<String> list = GLClientUtil.getLookupCache() //
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
  return new ListFilter<GLRecord, String>((ValueProvider<GLRecord, String>)valueProvider, listStore);
}
//--------------------------------------------------------------------------------------------------
public void forceLayout() {
  if (_parentContainer != null) {
    _parentContainer.forceLayout();
    topLevelContainer.forceLayout();
  }
}
//--------------------------------------------------------------------------------------------------
GLGridColumnModel getColumnModel() {
  return _columnModel;
}
//--------------------------------------------------------------------------------------------------
IGLColumn[] getColumns() {
  return _listStore.getColumns();
}
//--------------------------------------------------------------------------------------------------
Grid<GLRecord> getGrid() {
  return grid;
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
protected GridSelectionModel<GLRecord> getSelectionModel() {
  return _selectionModel;
}
//--------------------------------------------------------------------------------------------------
public abstract GLSQL getSQL() throws GLDBException;
//--------------------------------------------------------------------------------------------------
public void loadData(final ResizeContainer parentContainer) {
  GLLog.popup(60, "loadData():" + _table);
  _listStore.load(new IGLListStoreLoadedCallback() {
    @Override
    public void onSuccess() {
      GLLog.popup(60, "loadData() onSuccess():" + _table);
      createGrid(parentContainer);
    }
  });
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
        _listStore.delete(record);
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
  final String valueAsString;
  valueAsString = dateTimeFormat == null ? value.toString() : dateTimeFormat.format((Date)value);
  return _textMetrics.getWidth(valueAsString);
}
//--------------------------------------------------------------------------------------------------
private void resizeColumnToFit(final int columnIndex) {
  final IGLColumn[] columns = _listStore.getColumns();
  final String columnName = columns[columnIndex - (_useCheckBoxSelection ? 1 : 0)].toString();
  final GLColumnConfig<?> columnConfig = _columnModel.getColumnConfig(columnName);
  final DateTimeFormat dateTimeFormat = columnConfig.getDateTimeFormat();
  _textMetrics.bind(grid.getView().getHeader().getAppearance().styles().head());
  int maxWidth = _textMetrics.getWidth(columnConfig.getHeader().asString()) + 6;
  if (_listStore.size() > 0) {
    final Element element = grid.getView().getCell(1, 1); // this assignment is necessary!
    final String className = element.getClassName();
    _textMetrics.bind(className);
    for (int recordIndex = 0; recordIndex < _listStore.size(); ++recordIndex) {
      final GLRecord record = _listStore.get(recordIndex);
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
        grid.getView().refresh(true);
        return;
      }
      final int numberOfColumnsToResize = lastColumnIndex + (_useCheckBoxSelection ? 0 : 1);
      messageBox.updateProgress((double)columnIndex / numberOfColumnsToResize, "{0}% Complete");
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
  grid.getView().refresh(false);
}
//--------------------------------------------------------------------------------------------------
}