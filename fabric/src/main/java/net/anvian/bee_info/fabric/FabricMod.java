package net.anvian.bee_info.fabric;

import net.anvian.anvianslib.config.TelemetryConfigManager;
import net.anvian.anvianslib.util.LibUtil;
import net.anvian.bee_info.CommonMod;
import net.anvian.bee_info.Constants;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonMod.init();

        LibUtil.generateConfigPath(Constants.MOD_ID, FabricLoader.getInstance().getConfigDir());

        TelemetryConfigManager.initialize(FabricLoader.getInstance().getConfigDir().resolve(Constants.MOD_ID).toFile());
        if (TelemetryConfigManager.getConfig().enableTelemetry) {
            TelemetryConfigManager.sendTelemetryData(
                    Constants.MOD_ID,
                    "1.4",
                    LibUtil.getMinecraftVersion(),
                    "Fabric",
                    !FabricLoader.getInstance().isDevelopmentEnvironment()
            );
        }
    }
}
