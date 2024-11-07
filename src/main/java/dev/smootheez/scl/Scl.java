package dev.smootheez.scl;

import net.fabricmc.api.ClientModInitializer;


public class Scl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Constants.LOGGER.info("Scl initialized");
    }
}
