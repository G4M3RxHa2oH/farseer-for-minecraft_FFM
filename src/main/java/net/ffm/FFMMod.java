package net.ffm;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FFMMod implements ModInitializer {
    public static final String MOD_ID = "ffm";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Farseer for Minecraft (FFM) wurde erfolgreich geladen!");
    }
}
