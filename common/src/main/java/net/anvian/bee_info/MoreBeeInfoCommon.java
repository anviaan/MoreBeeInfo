package net.anvian.bee_info;

import net.anvian.anvianslib.config.TelemetryConfigManager;
import net.anvian.anvianslib.util.LibUtil;
import net.anvian.bee_info.platform.Services;

public class MoreBeeInfoCommon {
    public static void init() {
        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            Constants.LOG.info("Hello from " + Constants.MOD_ID);

            LibUtil.generateConfigPath(Constants.MOD_ID, Services.PLATFORM.getGameConfigDirectory());

            TelemetryConfigManager.initialize(Services.PLATFORM.getGameConfigDirectory().resolve(Constants.MOD_ID).toFile());
            if (TelemetryConfigManager.getConfig().enableTelemetry){
                TelemetryConfigManager.sendTelemetryData(
                        Constants.MOD_ID,
                        "1.4",
                        LibUtil.getMinecraftVersion(),
                        Services.PLATFORM.getPlatformName(),
                        !Services.PLATFORM.isDevelopmentEnvironment()
                );
            }
        }
    }
}