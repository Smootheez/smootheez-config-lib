package net.smootheez.scl;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Scl implements ClientModInitializer {
    private static final String MOD_ID = "scl";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Scl initialized");
    }
}
