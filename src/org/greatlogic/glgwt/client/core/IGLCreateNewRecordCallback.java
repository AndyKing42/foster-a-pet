package org.greatlogic.glgwt.client.core;

public interface IGLCreateNewRecordCallback {
//--------------------------------------------------------------------------------------------------
public void onFailure(final Throwable t);
public void onSuccess(final GLRecord record);
//--------------------------------------------------------------------------------------------------
}