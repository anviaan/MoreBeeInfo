package net.anvian.bee_info.forge;

import net.anvian.anvianslib.config.TelemetryConfigManager;
import net.anvian.anvianslib.util.LibUtil;
import net.anvian.bee_info.CommonMod;
import net.anvian.bee_info.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Constants.MOD_ID)
public final class ForgeMod {
    public ForgeMod() {
        CommonMod.init();

        LibUtil.generateConfigPath(Constants.MOD_ID, FMLPaths.CONFIGDIR.get());

        TelemetryConfigManager.initialize(FMLPaths.CONFIGDIR.get().resolve(Constants.MOD_ID).toFile());
        if (TelemetryConfigManager.getConfig().enableTelemetry) {
            TelemetryConfigManager.sendTelemetryData(
                    Constants.MOD_ID,
                    "1.4",
                    LibUtil.getMinecraftVersion(),
                    "Forge",
                    FMLLoader.isProduction()
            );
        }
    }
}
