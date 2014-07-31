package org.greatlogic.glgwt.client.db;

public interface IGLCreateNewRecordCallback {
//--------------------------------------------------------------------------------------------------
public void onFailure(final Throwable t);
public void onSuccess(final GLRecord record);
//--------------------------------------------------------------------------------------------------
}