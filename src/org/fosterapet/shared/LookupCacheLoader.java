package org.fosterapet.shared;

import org.fosterapet.shared.IFAPEnums.ELookupType;
import org.greatlogic.glgwt.client.core.GLLookupCache;

public class LookupCacheLoader {
//--------------------------------------------------------------------------------------------------
public static void load(final GLLookupCache cache) {
  cache.addListCache(ELookupType.Sex, "F|Female", "M|Male", "U|Unknown");
}
//--------------------------------------------------------------------------------------------------
}