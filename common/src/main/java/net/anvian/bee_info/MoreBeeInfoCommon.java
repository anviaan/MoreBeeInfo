package net.anvian.bee_info;

import net.anvian.anvianslib.util.LibUtil;
import net.anvian.bee_info.platform.Services;

public class MoreBeeInfoCommon {
    public static void init() {
        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            Constants.LOG.info("Hello from " + Constants.MOD_ID);

            LibUtil.setupTelemetry(Constants.MOD_ID, "1.4.1");
        }
    }
}