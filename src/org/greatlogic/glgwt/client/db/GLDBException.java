package org.greatlogic.glgwt.client.db;
import org.greatlogic.glgwt.client.core.GLClientUtil;
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
import org.greatlogic.glgwt.shared.IGLEnums.EGLDBException;

public class GLDBException extends Exception {
//--------------------------------------------------------------------------------------------------
private static final long serialVersionUID = 0x4a53494442457863L; // "GLDBExc"

private final Throwable   _originalThrowable;
//--------------------------------------------------------------------------------------------------
static void validateParameterIsNotNull(final String parName, final Object parValue)
  throws GLDBException {
  if (parValue == null) {
    throw new GLDBException(EGLDBException.ParameterIsNull, parName);
  }
} // validateParameterIsNotNull()
//--------------------------------------------------------------------------------------------------
public GLDBException(final EGLDBException dbException) {
  this(dbException, null, null);
} // GLDBException()
//--------------------------------------------------------------------------------------------------
public GLDBException(final EGLDBException dbException, final CharSequence message) {
  this(dbException, message, null);
} // GLDBException()
//--------------------------------------------------------------------------------------------------
public GLDBException(final EGLDBException dbException, final Throwable originalThrowable) {
  this(dbException, null, originalThrowable);
} // GLDBException()
//--------------------------------------------------------------------------------------------------
public GLDBException(final EGLDBException dbException, final CharSequence message,
                     final Throwable originalThrowable) {
  super(dbException == null ? message.toString()
                           : dbException.toString() +
                             (GLClientUtil.isBlank(message) ? "" : ":" + message) +
                             (originalThrowable == null ? "" : " - " +
                                                               originalThrowable.getMessage()),
        originalThrowable);
  _originalThrowable = originalThrowable == null ? this : originalThrowable;
} // GLDBException()
//--------------------------------------------------------------------------------------------------
/**
 * Returns the original exception that caused the exception. For example, if a SQL exception was the
 * original cause of this exception then the throwable will be that exception.
 * @return The original <code>Throwable</code> that caused this exception.
 */
public Throwable getOriginalThrowable() {
  return _originalThrowable;
} // getOriginalThrowable()
//--------------------------------------------------------------------------------------------------
}