package org.greatlogic.glgwt.client.db;

public interface IGLSQLSelectCallback {
//--------------------------------------------------------------------------------------------------
public void onFailure(final Throwable t);
public void onSuccess();
//--------------------------------------------------------------------------------------------------
}