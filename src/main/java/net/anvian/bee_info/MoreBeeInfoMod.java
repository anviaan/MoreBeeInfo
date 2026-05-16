package net.anvian.bee_info;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.fabricmc.api.ModInitializer;

public class MoreBeeInfoMod implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(MoreBeeInfoMod.class);

    @Override
    public void onInitialize() {

        LOGGER.debug("Hello Fabric world!");
    }
}
