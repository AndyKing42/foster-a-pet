package org.greatlogic.glgwt.client.db;

@SuppressWarnings("serial")
public class GLInvalidFieldOrColumnException extends Exception {
//--------------------------------------------------------------------------------------------------
public GLInvalidFieldOrColumnException(final String fieldName) {
  super(fieldName);
}
//--------------------------------------------------------------------------------------------------
}