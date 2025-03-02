package net.anvian.bee_info.fabric;

import net.anvian.bee_info.CommonMod;
import net.fabricmc.api.ModInitializer;

public final class FabricMod implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonMod.init();
    }
}
