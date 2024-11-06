package dev.smootheez.scl;

import dev.smootheez.scl.registry.ConfigRegister;
import dev.smootheez.scl.test.ExampleConfig;
import net.fabricmc.api.ClientModInitializer;


public class Scl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Constants.LOGGER.info("Scl initialized");
        ConfigRegister.getInstance().register(ExampleConfig.getInstance());
    }
}
