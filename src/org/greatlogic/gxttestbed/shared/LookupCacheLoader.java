package org.greatlogic.gxttestbed.shared;

import org.greatlogic.glgwt.client.core.GLLookupCache;
import org.greatlogic.gxttestbed.shared.IGXTTestbedEnums.ELookupType;

public class LookupCacheLoader {
//--------------------------------------------------------------------------------------------------
public static void load(final GLLookupCache cache) {
  cache.addListCache(ELookupType.Sex, "F|Female", "M|Male", "U|Unknown");
}
//--------------------------------------------------------------------------------------------------
}