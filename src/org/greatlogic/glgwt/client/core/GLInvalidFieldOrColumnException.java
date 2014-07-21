package org.greatlogic.glgwt.client.core;

@SuppressWarnings("serial")
public class GLInvalidFieldOrColumnException extends Exception {
//--------------------------------------------------------------------------------------------------
public GLInvalidFieldOrColumnException(final String fieldName) {
  super(fieldName);
}
//--------------------------------------------------------------------------------------------------
}