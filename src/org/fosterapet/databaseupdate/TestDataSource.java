package org.fosterapet.databaseupdate;


class TestDataSource {
////--------------------------------------------------------------------------------------------------
//private final Set<String>  _alreadyUsedValueSet;
//private final List<String> _discreteValueList;
//private String             _name;
//private String             _rangeFromDate;
//private int                _rangeFromInt;
//private String             _rangeToDate;
//private int                _rangeToInt;
//private int                _rangeToPlusOne;
//private Boolean            _removeFromList;
//private ETestDataType      _testDataType;
////==================================================================================================
//private enum ETestDataType {
//DateRange,
//Discrete,
//Range;
//public static ETestDataType lookup(final String lookupString) {
//  return (ETestDataType)GLUtil.enumLookup(ETestDataType.class, lookupString, ETestDataType.Discrete);
//} // lookup()
//} // enum ETestDataType
////==================================================================================================
//TestDataSource(final Row testDataValuesRow) {
//  _rangeFromInt = Integer.MAX_VALUE;
//  _rangeToInt = Integer.MIN_VALUE;
//  _rangeToPlusOne = Integer.MIN_VALUE;
//  _discreteValueList = Lists.newArrayList();
//  _alreadyUsedValueSet = Sets.newTreeSet();
//  processTestDataValuesRow(testDataValuesRow);
//} // SubstitutionValueInfo()
////--------------------------------------------------------------------------------------------------
//private boolean checkComparison(final int testDataValue,
//                                final EComparisonOperator comparisonOperator,
//                                final int comparisonTestDataValue) {
//  if (comparisonOperator == null) {
//    return true;
//  }
//  switch (comparisonOperator) {
//    case GreaterThan:
//      return testDataValue > comparisonTestDataValue;
//    case GreaterThanOrEqual:
//      return testDataValue >= comparisonTestDataValue;
//    case LessThan:
//      return testDataValue < comparisonTestDataValue;
//    case LessThanOrEqual:
//      return testDataValue <= comparisonTestDataValue;
//    case NotEqual:
//      return testDataValue != comparisonTestDataValue;
//  }
//  return true;
//} // checkComparison()
////--------------------------------------------------------------------------------------------------
//private boolean checkComparison(final String testDataValue,
//                                final EComparisonOperator comparisonOperator,
//                                final String comparisonTestDataValue) {
//  if (testDataValue == null || comparisonOperator == null) {
//    return true;
//  }
//  final int compareResult = testDataValue.compareTo(comparisonTestDataValue);
//  switch (comparisonOperator) {
//    case GreaterThan:
//      return compareResult > 0;
//    case GreaterThanOrEqual:
//      return compareResult >= 0;
//    case LessThan:
//      return compareResult < 0;
//    case LessThanOrEqual:
//      return compareResult <= 0;
//    case NotEqual:
//      return compareResult != 0;
//  }
//  return true;
//} // checkComparison()
////--------------------------------------------------------------------------------------------------
//String chooseValue(final EComparisonOperator comparisonOperator,
//                   final String comparisonTestDataValue,
//                   final Map<String, String> rowTestDataValueMap) {
//  String result = rowTestDataValueMap.get(_name);
//  if (result != null) {
//    return result;
//  }
//  switch (_testDataType) {
//    case DateRange:
//      final int daysBetween = GLUtil.getDaysBetween(_rangeFromDate, _rangeToDate, false);
//      do {
//        result = GLUtil.dateAddDays(_rangeFromDate, GLUtil.getRandomInt(daysBetween));
//      } while (!checkComparison(result, comparisonOperator, comparisonTestDataValue) ||
//               (_removeFromList && _alreadyUsedValueSet.contains(result)));
//      break;
//    case Discrete:
//      if (_removeFromList && _alreadyUsedValueSet.size() >= _discreteValueList.size()) {
//        GLLog.major("There are no more test data values for:" + _name);
//        return "";
//      }
//      do {
//        result = _discreteValueList.get(GLUtil.getRandomInt(_discreteValueList.size()));
//      } while (!checkComparison(result, comparisonOperator, comparisonTestDataValue) ||
//               (_removeFromList && _alreadyUsedValueSet.contains(result)));
//      break;
//    case Range:
//      final int comparisonTestDataValueInt = GLUtil.stringToInt(comparisonTestDataValue);
//      int intResult;
//      do {
//        intResult = GLUtil.getRandomInt(_rangeFromInt, _rangeToPlusOne);
//      } while (!checkComparison(intResult, comparisonOperator, comparisonTestDataValueInt) ||
//               (_removeFromList && _alreadyUsedValueSet.contains(result)));
//      result = Integer.toString(intResult);
//      break;
//  }
//  _alreadyUsedValueSet.add(result);
//  rowTestDataValueMap.put(_name, result);
//  return result;
//} // chooseValue()
////--------------------------------------------------------------------------------------------------
//String getName() {
//  return _name;
//} // getName()
////--------------------------------------------------------------------------------------------------
//private void processTestDataValuesRow(final Row row) {
//  for (final Cell cell : row) {
//    String cellValue = "";
//    if (cell != null) {
//      switch (cell.getCellType()) {
//        case Cell.CELL_TYPE_FORMULA:
//          switch (cell.getCachedFormulaResultType()) {
//            case Cell.CELL_TYPE_NUMERIC:
//              cellValue = LoadTestData.TestDataNumberFormat.format(cell.getNumericCellValue());
//              break;
//            case Cell.CELL_TYPE_STRING:
//              cellValue = cell.getStringCellValue();
//              break;
//          }
//          break;
//        case Cell.CELL_TYPE_NUMERIC:
//          cellValue = LoadTestData.TestDataNumberFormat.format(cell.getNumericCellValue());
//          break;
//        case Cell.CELL_TYPE_STRING:
//          cellValue = cell.getStringCellValue();
//          break;
//      }
//      cellValue = cellValue.trim();
//      if (!StringUtils.isEmpty(cellValue)) {
//        if (_name == null) {
//          _name = cellValue;
//        }
//        else if (_testDataType == null) {
//          _testDataType = ETestDataType.lookup(cellValue);
//        }
//        else if (_removeFromList == null) {
//          _removeFromList = cellValue.equalsIgnoreCase("y");
//        }
//        else {
//          switch (_testDataType) {
//            case DateRange:
//              if (_rangeFromDate == null) {
//                if (cellValue.indexOf('-') == 8 && cellValue.length() == 17) {
//                  _rangeFromDate = cellValue.substring(0, 8);
//                  _rangeToDate = cellValue.substring(9);
//                }
//                else {
//                  GLLog.major("Invalid date range:" + cellValue);
//                }
//              }
//              break;
//            case Discrete:
//              if (cellValue.indexOf(',') >= 0) {
//                try {
//                  for (final String value : GLCSV.extract(cellValue)) {
//                    _discreteValueList.add(value);
//                  }
//                }
//                catch (final GLCSVException csve) {
//                  GLLog.major("CSV error in:" + StringUtils.substring(cellValue, 0, 50), csve);
//                }
//              }
//              else {
//                _discreteValueList.add(cellValue);
//              }
//              break;
//            case Range:
//              final int hyphenIndex = cellValue.indexOf('-');
//              if (hyphenIndex > 0 && cellValue.length() > 2) {
//                _rangeFromInt = GLUtil.stringToInt(cellValue.substring(0, hyphenIndex));
//                _rangeToInt = GLUtil.stringToInt(cellValue.substring(hyphenIndex + 1));
//              }
//              else {
//                GLLog.major("Invalid range:" + cellValue);
//              }
//              break;
//          }
//        }
//      }
//    }
//  }
//  _rangeFromInt = _rangeFromInt == Integer.MAX_VALUE ? 0 : _rangeFromInt;
//  _rangeToInt = _rangeToInt == Integer.MIN_VALUE ? Integer.MAX_VALUE : _rangeToInt;
//  _rangeToPlusOne = _rangeToInt == Integer.MAX_VALUE ? _rangeToInt : _rangeToInt + 1;
//} // processTestDataValuesRow()
////--------------------------------------------------------------------------------------------------
//void reset() {
//  _alreadyUsedValueSet.clear();
//} // reset()
////--------------------------------------------------------------------------------------------------
}