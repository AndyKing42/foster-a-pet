package org.fosterapet.databaseupdate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import org.fosterapet.shared.IDBEnums.EFAPTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.greatlogic.glbase.gldb.GLDBException;
import com.greatlogic.glbase.gldb.GLSQL;
import com.greatlogic.glbase.gldb.IGLColumn;
import com.greatlogic.glbase.gldb.IGLDBEnums.EGLDBOp;
import com.greatlogic.glbase.gldb.IGLTable;
import com.greatlogic.glbase.gllib.GLLog;
import com.greatlogic.glbase.gllib.IGLLibEnums.EGLLogLevel;
import com.ibm.icu.impl.Row;

class LoadTestData {
//--------------------------------------------------------------------------------------------------
private static final char                 CondSeparator      = ';';
static final DecimalFormat                TestDataNumberFormat;
private static char                       TestDataValueBegin = '[';
private static char                       TestDataValueEnd   = ']';

private List<IGLColumn>                   _columnList;
private Set<Integer>                      _keyColumnIndexSet;
private final Map<String, String>         _rowTestDataValueMap;
private final Map<String, TestDataSource> _testDataSourceMap;      // substitution value name -> TestDataSource
private IGLTable                          _table;
private XSSFWorkbook                      _workbook;
//--------------------------------------------------------------------------------------------------
static {
  TestDataNumberFormat = new DecimalFormat("####################.##########");
} // static initializer
//==================================================================================================
enum EComparisonOperator {
GreaterThan(">"),
GreaterThanOrEqual(">="),
LessThan("<"),
LessThanOrEqual("<="),
NotEqual("!=");
public static EComparisonOperator[] _comparisonOperatorsByLength;
private String                      _operatorString;
private EComparisonOperator(final String operatorString) {
  _operatorString = operatorString;
} // EComparisonOperator()
static EComparisonOperator lookup(final String operator) {
  if (_comparisonOperatorsByLength == null) {
    _comparisonOperatorsByLength = EComparisonOperator.values();
    Arrays.sort(_comparisonOperatorsByLength, new Comparator<EComparisonOperator>() {
      @Override
      public int compare(final EComparisonOperator operator1, final EComparisonOperator operator2) {
        final int result = operator2._operatorString.length() - operator1._operatorString.length();
        return result;
      }
    });
  }
  for (final EComparisonOperator comparisonOperator : _comparisonOperatorsByLength) {
    if (operator.startsWith(comparisonOperator._operatorString)) {
      return comparisonOperator;
    }
  }
  return null;
} // lookup()
} // enum EComparisonOperator
//==================================================================================================
public LoadTestData(final String inputFilename) throws GLDBException {
  _rowTestDataValueMap = Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER);
  _testDataSourceMap = Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER);
  try {
    final FileInputStream ssInputStream = new FileInputStream(inputFilename);
    try {
      _workbook = new XSSFWorkbook(ssInputStream);
      for (int sheetIndex = 0; sheetIndex < _workbook.getNumberOfSheets(); ++sheetIndex) {
        if (!_workbook.isSheetHidden(sheetIndex)) {
          processSheet(_workbook.getSheetAt(sheetIndex));
        }
      }
    }
    finally {
      ssInputStream.close();
    }
  }
  catch (final FileNotFoundException fnfe) {
    GLLog.major("File not found:" + inputFilename, fnfe);
  }
  catch (final IOException ioe) {
    GLLog.major("I/O error in file:" + inputFilename, ioe);
  }
} // LoadTestData()
//--------------------------------------------------------------------------------------------------
private void deleteDBRow(final List<String> cellDataList) throws GLDBException {
  final GLSQL deleteSQL = GLSQL.delete(_table);
  for (final int columnIndex : _keyColumnIndexSet) {
    deleteSQL.whereAnd(_columnList.get(columnIndex), EGLDBOp.Equals, cellDataList.get(columnIndex));
  }
  deleteSQL.execute();
} // deleteDBRow()
//--------------------------------------------------------------------------------------------------
private void insertDBRow(final List<String> cellDataList) throws GLDBException {
  final GLSQL insertSQL = GLSQL.insert(_table, false);
  for (int columnIndex = 0; columnIndex < _columnList.size(); ++columnIndex) {
    final String columnValue = cellDataList.get(columnIndex);
    if (!StringUtils.isBlank(columnValue)) {
      insertSQL.setValue(_columnList.get(columnIndex), columnValue);
    }
  }
  insertSQL.execute();
} // insertDBRow()
//--------------------------------------------------------------------------------------------------
private boolean loadColumnNames(final Row row) {
  _columnList = Lists.newArrayList();
  _keyColumnIndexSet = Sets.newTreeSet();
  final Map<String, IGLColumn> columnMap;
  columnMap = new TreeMap<String, IGLColumn>(String.CASE_INSENSITIVE_ORDER);
  for (final Enum<?> column : _table.getColumnEnumClass().getEnumConstants()) {
    columnMap.put(column.toString(), (IGLColumn)column);
  }
  for (final Cell cell : row) {
    if (cell == null || cell.getCellType() != Cell.CELL_TYPE_STRING) {
      break;
    }
    String columnName = cell.getStringCellValue();
    if (columnName.length() == 0) {
      break;
    }
    if (columnName.startsWith("*")) {
      columnName = columnName.substring(1);
      _keyColumnIndexSet.add(_columnList.size());
    }
    final IGLColumn column = columnMap.get(columnName);
    if (column == null) {
      GLLog.major("The column " + _table + "." + columnName + " is not defined in the column enums");
      return false;
    }
    _columnList.add(column);
  }
  if (_keyColumnIndexSet.size() == 0) {
    _keyColumnIndexSet.add(0);
  }
  return true;
} // loadColumnNames()
//--------------------------------------------------------------------------------------------------
/**
 * Loads the data from a single row into the database.
 * @param rowNumber The row number in the spreadsheet.
 * @param row The row containing the data.
 */
private void loadDataFromRow(final int rowNumber, final Row row) throws GLDBException {
  int insertAttemptsRemaining = 10;
  boolean duplicateKeyValues = false;
  GLDBException savedException = null;
  do {
    final List<String> cellDataList = Lists.newArrayList();
    boolean allCellValuesAreEmpty = true;
    _rowTestDataValueMap.clear();
    for (int columnIndex = 0; columnIndex < _columnList.size(); ++columnIndex) {
      final Cell cell = row.getCell(columnIndex);
      String cellValue = "";
      if (cell != null) {
        switch (cell.getCellType()) {
          case Cell.CELL_TYPE_FORMULA:
            switch (cell.getCachedFormulaResultType()) {
              case Cell.CELL_TYPE_NUMERIC:
                cellValue = TestDataNumberFormat.format(cell.getNumericCellValue());
                break;
              case Cell.CELL_TYPE_STRING:
                cellValue = cell.getStringCellValue();
                break;
            }
            break;
          case Cell.CELL_TYPE_NUMERIC:
            cellValue = TestDataNumberFormat.format(cell.getNumericCellValue());
            break;
          case Cell.CELL_TYPE_STRING:
            cellValue = replaceTestDataValues(cell.getStringCellValue());
            break;
        }
      }
      cellValue = StringUtils.trimToEmpty(cellValue);
      allCellValuesAreEmpty &= cellValue.isEmpty();
      cellDataList.add(cellValue);
    }
    if (!allCellValuesAreEmpty) {
      deleteDBRow(cellDataList);
      try {
        insertDBRow(cellDataList);
        duplicateKeyValues = false;
      }
      catch (final GLDBException dbe) {
        savedException = dbe;
        duplicateKeyValues = dbe.getMessage().contains("Duplicate");
        if (!duplicateKeyValues) {
          throw dbe;
        }
        --insertAttemptsRemaining;
      }
    }
  } while (duplicateKeyValues && insertAttemptsRemaining > 0);
  if (insertAttemptsRemaining == 0 && savedException != null) {
    throw savedException;
  }
} // loadDataFromRow()
//--------------------------------------------------------------------------------------------------
private void processSheet(final XSSFSheet sheet) throws GLDBException {
  _columnList = null;
  final EGLLogLevel saveLogLevel = GLLog.setThreadLogLevel(EGLLogLevel.Minor);
  try {
    if (sheet.getSheetName().equalsIgnoreCase("TestDataValues")) {
      processTestDataValuesSheet(sheet);
      return;
    }
    _table = EFAPTable.lookup(sheet.getSheetName(), null);
    if (_table == null) {
      GLLog.major("Unknown table for sheet:" + sheet.getSheetName());
      return;
    }
    resetDataSources();
    int rowCount = 0;
    for (final Row row : sheet) {
      ++rowCount;
      if (_columnList == null) {
        if (!loadColumnNames(row)) {
          return;
        }
      }
      else {
        loadDataFromRow(rowCount, row);
      }
    }
  }
  finally {
    GLLog.setThreadLogLevel(saveLogLevel);
  }
} // processSheet()
//--------------------------------------------------------------------------------------------------
private void processTestDataValuesSheet(final XSSFSheet sheet) {
  for (final Row row : sheet) {
    final TestDataSource testDataSource = new TestDataSource(row);
    _testDataSourceMap.put(testDataSource.getName(), testDataSource);
  }
} // processTestDataValuesSheet()
//--------------------------------------------------------------------------------------------------
private String replaceTestDataValues(final String originalValue) {
  String result;
  if (originalValue.indexOf(TestDataValueBegin) < 0) {
    return originalValue;
  }
  result = "";
  int greaterThanIndex = -1;
  do {
    final int lessThanIndex = originalValue.indexOf(TestDataValueBegin, greaterThanIndex + 1);
    if (lessThanIndex < 0) {
      result += originalValue.substring(greaterThanIndex + 1);
      greaterThanIndex = -1;
    }
    else {
      if (lessThanIndex > greaterThanIndex + 1) {
        result += originalValue.substring(greaterThanIndex + 1, lessThanIndex);
      }
      greaterThanIndex = originalValue.indexOf(TestDataValueEnd, lessThanIndex);
      if (greaterThanIndex < 0) {
        result += originalValue.substring(lessThanIndex);
      }
      else if (greaterThanIndex == lessThanIndex + 1) {
        result += originalValue.substring(lessThanIndex, greaterThanIndex + 1);
      }
      else {
        String testDataValueName = originalValue.substring(lessThanIndex + 1, greaterThanIndex);
        final int condSeparatorIndex = testDataValueName.indexOf(CondSeparator);
        String cond = null;
        if (condSeparatorIndex > 2 && condSeparatorIndex < testDataValueName.length() - 1) {
          cond = testDataValueName.substring(0, condSeparatorIndex);
          testDataValueName = testDataValueName.substring(condSeparatorIndex + 1);
        }
        final TestDataSource testDataSource = _testDataSourceMap.get(testDataValueName);
        if (testDataSource == null) {
          result += originalValue.substring(lessThanIndex, greaterThanIndex + 1);
        }
        else {
          EComparisonOperator comparisonOperator = null;
          String comparisonTestDataValue = null;
          if (cond != null) {
            comparisonOperator = EComparisonOperator.lookup(cond);
            comparisonTestDataValue = _rowTestDataValueMap.get(cond.substring(2));
          }
          result += testDataSource.chooseValue(comparisonOperator, comparisonTestDataValue,
                                               _rowTestDataValueMap);
        }
      }
    }
  } while (greaterThanIndex > 0 && greaterThanIndex < originalValue.length() - 1);
  return result;
} // replaceTestDataValues()
//--------------------------------------------------------------------------------------------------
private void resetDataSources() {
  for (final TestDataSource testDataSource : _testDataSourceMap.values()) {
    testDataSource.reset();
  }
} // resetDataSources()
//--------------------------------------------------------------------------------------------------
}