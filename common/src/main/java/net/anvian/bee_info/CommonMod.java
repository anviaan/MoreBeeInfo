package net.anvian.bee_info;

import net.anvian.anvianslib.util.LibUtil;

public final class CommonMod {
    public static void init() {
        LibUtil.setupTelemetry(Constants.MOD_ID, "1.4.1");
    }
}