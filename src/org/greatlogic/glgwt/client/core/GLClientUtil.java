package org.greatlogic.glgwt.client.core;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.greatlogic.glgwt.client.db.GLDBUpdater;
import org.greatlogic.glgwt.client.db.GLLookupCache;
import org.greatlogic.glgwt.client.db.GLRecord;
import org.greatlogic.glgwt.client.db.GLRecordDef;
import org.greatlogic.glgwt.client.db.IGLCreateNewRecordCallback;
import org.greatlogic.glgwt.client.event.GLEventBus;
import org.greatlogic.glgwt.client.event.GLNewRecordEvent;
import org.greatlogic.glgwt.client.widget.GLLoginWidget;
import org.greatlogic.glgwt.shared.GLRemoteServiceResponse;
import org.greatlogic.glgwt.shared.GLValidators;
import org.greatlogic.glgwt.shared.IGLRemoteServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.UmbrellaException;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;

public class GLClientUtil {
//--------------------------------------------------------------------------------------------------
private static GLClientFactory _clientFactory;
private static GLLoginWidget   _loginWidget;
private static Random          _random;
private static String          _sessionToken;
private static DateTimeFormat  _yyyymmddDateTimeFormat;
//--------------------------------------------------------------------------------------------------
static {
  _random = new Random(System.currentTimeMillis());
  _yyyymmddDateTimeFormat = DateTimeFormat.getFormat("yyyyMMdd");
}
//--------------------------------------------------------------------------------------------------
private static void addWindowClosingHandler(final String appDescription) {
  if (isProdMode()) {
    Window.addWindowClosingHandler(new Window.ClosingHandler() {
      @Override
      public void onWindowClosing(final Window.ClosingEvent closingEvent) {
        closingEvent.setMessage("You are about to leave " + appDescription);
      }
    });
  }
}
//--------------------------------------------------------------------------------------------------
public static void createNewRecord(final GLRecordDef recordDef,
                                   final IGLCreateNewRecordCallback createNewRecordCallback) {
  final AsyncCallback<GLRemoteServiceResponse> callback;
  callback = new AsyncCallback<GLRemoteServiceResponse>() {
    @Override
    public void onFailure(final Throwable caught) {
      if (createNewRecordCallback != null) {
        createNewRecordCallback.onFailure(caught);
      }
    }
    @Override
    public void onSuccess(final GLRemoteServiceResponse response) {
      final GLRecord record = new GLRecord(recordDef);
      record.put(recordDef.getTable().getPrimaryKeyColumn(), response.getNextId());
      _clientFactory.getEventBus().fireEvent(new GLNewRecordEvent(record));
      if (createNewRecordCallback != null) {
        createNewRecordCallback.onSuccess(record);
      }
    }
  };
  GLClientUtil.getRemoteServiceHelper().getNextId(recordDef.getTable(), 1, callback);
}
//--------------------------------------------------------------------------------------------------
public static List<EditorError> createValidatorResult(final Editor<?> editor, final String message) {
  final List<EditorError> result = new ArrayList<>();
  result.add(new DefaultEditorError(editor, message, ""));
  return result;
}
//--------------------------------------------------------------------------------------------------
@SuppressWarnings("deprecation")
public static String dateAddDays(final String originalDate, final int numberOfDays) {
  final Date newDate = _yyyymmddDateTimeFormat.parseStrict(originalDate);
  newDate.setDate(newDate.getDate() + numberOfDays);
  return _yyyymmddDateTimeFormat.format(newDate);
}
//--------------------------------------------------------------------------------------------------
private static void disableBackspace() {
  Event.addNativePreviewHandler(new NativePreviewHandler() {
    @Override
    public void onPreviewNativeEvent(final NativePreviewEvent event) {
      try {
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_BACKSPACE &&
            event.getNativeEvent().getEventTarget() != null) {
          final String tagName = Element.as(event.getNativeEvent().getEventTarget()).getTagName();
          if (!tagName.equalsIgnoreCase("input") && !tagName.equalsIgnoreCase("textarea")) {
            event.getNativeEvent().stopPropagation();
            event.getNativeEvent().preventDefault();
          }
        }
      }
      catch (final Exception e) {
        // ignore exceptions
      }
    }
  });
}
//--------------------------------------------------------------------------------------------------
/**
 * Returns an <code>Object</code> formatted as a <code>String</code> in a format that is consistent
 * with the GL "normal" formatting.
 * @param value The value to be formatted.
 * @return The original value formatted according to GL expectations.
 */
public static String formatObjectSpecial(final Object value) {
  return formatObjectSpecial(value, "");
}
//--------------------------------------------------------------------------------------------------
/**
 * Returns an <code>Object</code> formatted as a <code>String</code> in a format that is consistent
 * with the GL "normal" formatting.
 * @param value The value to be formatted.
 * @param defaultValue A default value to be returned in the value is null.
 * @return The original value formatted according to GL expectations.
 */
public static String formatObjectSpecial(final Object value, final String defaultValue) {
  String result;
  if (value == null) {
    result = defaultValue;
  }
  else if (value instanceof String) {
    result = (String)value;
  }
  else if (value instanceof Boolean) {
    result = (Boolean)value ? "Y" : "N";
  }
  else if (value instanceof Date) {
    result = DateTimeFormat.getFormat("yyyyMMddHHmmss").format((Date)value);
  }
  else if (value instanceof BigDecimal) {
    result = ((BigDecimal)value).toPlainString();
  }
  else {
    result = value.toString();
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
public static GLDBUpdater getDBUpdater() {
  return _clientFactory.getDBUpdater();
}
//--------------------------------------------------------------------------------------------------
public static GLEventBus getEventBus() {
  return _clientFactory.getEventBus();
}
//--------------------------------------------------------------------------------------------------
public static GLLookupCache getLookupCache() {
  return _clientFactory.getLookupCache();
}
//--------------------------------------------------------------------------------------------------
public static String getLowestLevelCSSClassName(final Element element, final String attributeName) {
  String result = null;
  for (int childIndex = 0; childIndex < element.getChildCount(); ++childIndex) {
    final Node node = element.getChild(childIndex);
    if (node instanceof Element) {
      result = getLowestLevelCSSClassName((Element)node, attributeName);
    }
  }
  return result == null ? element.getClassName() : result;
}
//--------------------------------------------------------------------------------------------------
/**
 * Returns a random value between 0 and the maxValue minus one.
 * @param maxValue The maximum value plus one for the random value.
 * @return A random integer between 0 (inclusive) and the specified value minus one.
 */
public static int getRandomInt(final int maxValue) {
  return _random.nextInt(maxValue);
}
//--------------------------------------------------------------------------------------------------
/**
 * Returns a random value between a minimum and maximum value minus one.
 * @param minValue The minimum value for the random value.
 * @param maxValue The maximum value plus one for the random value.
 * @return A random integer between the minimum and maximum value minus one.
 */
public static int getRandomInt(final int minValue, final int maxValue) {
  return _random.nextInt(maxValue - minValue) + minValue;
}
//--------------------------------------------------------------------------------------------------
public static IGLRemoteServiceAsync getRemoteService() {
  return _clientFactory.getRemoteService();
}
//--------------------------------------------------------------------------------------------------
public static GLRemoteServiceHelper getRemoteServiceHelper() {
  return _clientFactory.getRemoteServiceHelper();
}
//--------------------------------------------------------------------------------------------------
public static String getSessionToken() {
  return _sessionToken;
}
//--------------------------------------------------------------------------------------------------
public static GLValidators getValidators() {
  return _clientFactory.getValidators();
}
//--------------------------------------------------------------------------------------------------
public static void initialize(final String appDescription, final GLClientFactory clientFactory,
                              final String loginWidgetHeadingText) {
  _clientFactory = clientFactory;
  disableBackspace();
  addWindowClosingHandler(appDescription);
  _loginWidget = new GLLoginWidget(loginWidgetHeadingText);
  _loginWidget.logIn("", "");
}
//--------------------------------------------------------------------------------------------------
public static boolean isBlank(final CharSequence s) {
  return s == null || s.toString().trim().length() == 0;
}
//--------------------------------------------------------------------------------------------------
public static boolean isProdMode() {
  return GWT.isProdMode() || !GWT.isClient();
}
//--------------------------------------------------------------------------------------------------
/**
 * Converts a string containing a comma-delimited list of values into a <code>List</code> containing
 * those values.
 * @param commaDelimitedStrings A string containing a comma-delimited list of values.
 * @param sortList If this is <code>true</code> then the returned list will be sorted.
 * @return A <code>List</code> containing the separated values.
 */
public static ArrayList<String> loadListFromStrings(final String commaDelimitedStrings,
                                                    final boolean sortList) {
  return loadListFromStrings(null, commaDelimitedStrings, sortList);
}
//--------------------------------------------------------------------------------------------------
/**
 * Converts a string containing a comma-delimited list of values into a <code>List</code> containing
 * those values.
 * @param stringList The <code>List</code> into which the values will be placed. If this is
 * <code>null</code> then a new <code>List</code> will be created.
 * @param commaDelimitedStrings A string containing a comma-delimited list of values.
 * @param sortList If this is <code>true</code> then the returned list will be sorted.
 * @return A <code>List</code> containing the separated values.
 */
public static ArrayList<String> loadListFromStrings(final ArrayList<String> stringList,
                                                    final String commaDelimitedStrings,
                                                    final boolean sortList) {
  final ArrayList<String> result = stringList == null ? new ArrayList<String>(20) : stringList;
  try {
    final String[] strings = commaDelimitedStrings.split(",");
    result.clear();
    Collections.addAll(result, strings);
    if (sortList) {
      Collections.sort(result);
    }
  }
  catch (final Exception e) {
    // the list will have everything up to the first exception
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
public static void logIn() {
  _loginWidget.logIn();
}
//--------------------------------------------------------------------------------------------------
/**
 * Converts a string value to its equivalent boolean value. If the string does not represent a
 * "true" value then the result will be set to <code>false</code>.
 * @param stringValue The string that will be converted.
 * @return true if the string begins with "y" or equals "true" (ignoring case).
 */
public static boolean stringToBoolean(final String stringValue) {
  return stringToBoolean(stringValue, false);
}
//--------------------------------------------------------------------------------------------------
public static void setSessionToken(final String sessionToken) {
  _sessionToken = sessionToken;
}
//--------------------------------------------------------------------------------------------------
public static void setTheme(final String themeClassName) {
  // from: http://stackoverflow.com/questions/7960685/allow-different-gwt-visual-themes-for-different-users/24455939#24455939
  // TODO: use this from somewhere!
  // Use *** one *** of these:
  /* 1 */Document.get().getBody().setClassName(themeClassName);
  /* 2 */RootPanel.get().setStyleName(themeClassName);
  /* 3 */RootPanel.get().setStylePrimaryName(themeClassName);
}
//--------------------------------------------------------------------------------------------------
public static void setUncaughtExceptionHandler(final ScheduledCommand moduleLoadCommand) {
  if (isProdMode() && false) {
    // todo: unwrap the exception
    GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
      @Override
      public void onUncaughtException(final Throwable throwable) {
        Throwable unwrappedThrowable = throwable;
        if (throwable instanceof UmbrellaException) {
          for (final Throwable t : ((UmbrellaException)throwable).getCauses()) {
            unwrappedThrowable = t;
          }
        }
        GLLog.popup(20, unwrappedThrowable.getMessage());
        // todo:      GLLog.major(unwrappedThrowable);
        // todo: use https://code.google.com/p/gwt-log/
      }
    });
  }
  Scheduler.get().scheduleDeferred(moduleLoadCommand);
}
//--------------------------------------------------------------------------------------------------
/**
 * Converts a string value to its equivalent boolean value. The values "y" and "true", which are
 * compared with case ignored, are interpreted as true. If the string is null or is zero length then
 * the default value is returned.
 * @param stringValue The string that will be converted.
 * @param defaultValue The value that will be returned if the string is null or zero length.
 * @return true if the string begins with "y" or equals "true" (ignoring case).
 */
public static boolean stringToBoolean(final CharSequence stringValue, final boolean defaultValue) {
  boolean result;
  if (isBlank(stringValue)) {
    result = defaultValue;
  }
  else {
    result = stringValue.charAt(0) == 'y' || stringValue.charAt(0) == 'Y' || //
             stringValue.toString().equalsIgnoreCase("true");
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
/**
 * Converts a string value in the format "YYYYMMDDHHMMSS" to a Date value.
 * @param yyyymmddhhmmss The string value in the format "YYYYMMDDHHMMSS" (the "HHMMSS" is optional).
 * @return A Date object based upon the string value.
 */
@SuppressWarnings("deprecation")
public static Date stringToDate(final String yyyymmddhhmmss) {
  int hour;
  int minute;
  int second;
  try {
    if (yyyymmddhhmmss.length() >= 14) {
      hour = GLClientUtil.stringToInt(yyyymmddhhmmss.substring(8, 10));
      minute = GLClientUtil.stringToInt(yyyymmddhhmmss.substring(10, 12));
      second = GLClientUtil.stringToInt(yyyymmddhhmmss.substring(12, 14));
      hour = hour < 0 || hour > 23 ? 0 : hour;
      minute = minute < 0 || minute > 59 ? 0 : minute;
      second = second < 0 || second > 59 ? 0 : second;
    }
    else {
      hour = 0;
      minute = 0;
      second = 0;
    }
    return new Date(Integer.parseInt(yyyymmddhhmmss.substring(0, 4)) - 1900, //
                    Integer.parseInt(yyyymmddhhmmss.substring(4, 6)) - 1, //
                    Integer.parseInt(yyyymmddhhmmss.substring(6, 8)), //
                    hour, minute, second);
  }
  catch (final Exception e) {
    return null;
  }
}
//--------------------------------------------------------------------------------------------------
/**
 * Converts a string value to its equivalent BigDecimal value. If the string is not a valid numeric
 * value then BigDecimal.ZERO is returned. If there are commas in the value then they will be
 * removed before converting to a decimal (this makes this non-portable to some locations).
 * @param stringValue The string that will be converted.
 * @return the BigDecimal value or BigDecimal.ZERO if the value is not valid.
 */
public static BigDecimal stringToDec(final String stringValue) {
  return stringToDec(stringValue, BigDecimal.ZERO);
} // stringToDec()
//--------------------------------------------------------------------------------------------------
/**
 * Converts a string value to its equivalent BigDecimal value. If the string is not a valid numeric
 * value then the default value is returned. If there are commas in the value then they will be
 * removed before converting to a decimal (this makes this non-portable to some locations).
 * @param stringValue The string that will be converted.
 * @param defaultValue The value that will be returned if the string is invalid.
 * @return the BigDecimal value (using the BigDecimal constructor or the defaultValue if the string
 * is not a numeric value).
 */
public static BigDecimal stringToDec(final CharSequence stringValue, final BigDecimal defaultValue) {
  BigDecimal result;
  try {
    result = new BigDecimal(stringToNumericAdjust(stringValue, false));
  }
  catch (final Exception e) {
    result = defaultValue;
  }
  return result;
} // stringToDec()
//--------------------------------------------------------------------------------------------------
/**
 * Converts a string value to its equivalent double value. If the string is not a valid numeric
 * value then zero is returned. If there are commas in the value then they will be removed before
 * converting to a double (this makes this non-portable to some locations).
 * @param stringValue The string that will be converted.
 * @return The double value.
 */
public static double stringToDouble(final CharSequence stringValue) {
  return stringToDouble(stringValue, 0);
} // stringToDouble()
//--------------------------------------------------------------------------------------------------
/**
 * Converts a string value to its equivalent double value. If the string is not a valid numeric
 * value then the default value is returned. If there are commas in the value then they will be
 * removed before converting to a double (this makes this non-portable to some locations).
 * @param stringValue The string that will be converted.
 * @param defaultValue The value that will be returned if the string is invalid.
 * @return The double value.
 */
public static double stringToDouble(final CharSequence stringValue, final double defaultValue) {
  double result;
  try {
    result = Double.parseDouble(stringToNumericAdjust(stringValue, false));
  }
  catch (final Exception e) {
    result = defaultValue;
  }
  return result;
} // stringToDouble()
//--------------------------------------------------------------------------------------------------
/**
 * Converts a string value to its equivalent int value. If the string is not a valid int value then
 * zero is returned. If there are commas in the value then they will be removed before converting to
 * an integer (this makes this non-portable to some locations).
 * @param stringValue The string that will be converted.
 * @return The int value or zero if the value is not valid.
 */
public static int stringToInt(final CharSequence stringValue) {
  return stringToInt(stringValue, 0);
} // stringToInt()
//--------------------------------------------------------------------------------------------------
/**
 * Converts a string value to its equivalent int value. If the string is not a valid int value then
 * the default value is returned. If there are commas in the value then they will be removed before
 * converting to an integer (this makes this non-portable to some locations).
 * @param stringValue The string that will be converted.
 * @param defaultValue The value that will be returned if the string is invalid.
 * @return The int value (using Integer.parseInt or the defaultValue if the string is not an int
 * value).
 */
public static int stringToInt(final CharSequence stringValue, final long defaultValue) {
  int result;
  try {
    result = Integer.parseInt(stringToNumericAdjust(stringValue, true));
  }
  catch (final Exception e) {
    result = (int)defaultValue;
  }
  return result;
} // stringToInt()
//--------------------------------------------------------------------------------------------------
/**
 * Converts a string value to its equivalent long value. If the string is not a valid long value
 * then zero is returned. If there are commas in the value then they will be removed before
 * converting to a long (this makes this non-portable to some locations).
 * @param stringValue The string that will be converted.
 * @return The long value or zero if the value is not valid.
 */
public static long stringToLong(final CharSequence stringValue) {
  return stringToLong(stringValue, 0);
} // stringToLong()
//--------------------------------------------------------------------------------------------------
/**
 * Converts a string value to its equivalent long value. If the string is not a valid long value
 * then the default value is returned. If there are commas in the value then they will be removed
 * before converting to a long (this makes this non-portable to some locations).
 * @param stringValue The string that will be converted.
 * @param defaultValue The value that will be returned if the string is invalid.
 * @return The long value (using <code>Long.parseLong</code> or the defaultValue if the string is
 * not a valid <code>long</code> value).
 */
public static long stringToLong(final CharSequence stringValue, final long defaultValue) {
  long result;
  try {
    result = Long.parseLong(stringToNumericAdjust(stringValue, true));
  }
  catch (final Exception e) {
    result = defaultValue;
  }
  return result;
} // stringToLong()
//--------------------------------------------------------------------------------------------------
private static String stringToNumericAdjust(final CharSequence stringValue,
                                            final boolean removeDecimals) {
  String result = stringValue.toString();
  if (result.indexOf(',') > 0) {
    result = result.replace(",", "");
  }
  if (result.length() > 0 && (result.charAt(0) == ' ' || result.charAt(result.length() - 1) == ' ')) {
    result = result.trim();
  }
  if (result.endsWith("-") && result.length() > 1) {
    result = "-" + result.substring(0, result.length() - 1);
  }
  if (result.startsWith("+") && result.length() > 1) {
    result = result.substring(1);
  }
  if (result.length() > 0 && (result.charAt(0) == ' ' || result.charAt(result.length() - 1) == ' ')) {
    result = result.trim();
  }
  if (removeDecimals) {
    final int dotIndex = result.indexOf('.');
    boolean validDecimal = true;
    if (dotIndex >= 0 && result.length() > dotIndex + 1) {
      final String decimal = result.substring(dotIndex + 1);
      try {
        Long.parseLong(decimal);
      }
      catch (final Exception e) {
        validDecimal = false;
      }
    }
    if (validDecimal) {
      if (dotIndex == 0) {
        result = "0";
      }
      else if (dotIndex > 0) {
        result = result.substring(0, dotIndex);
      }
    }
  }
  return result;
}
//--------------------------------------------------------------------------------------------------
}