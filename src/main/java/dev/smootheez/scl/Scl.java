package dev.smootheez.scl;

import dev.smootheez.scl.registry.ConfigRegister;
import dev.smootheez.scl.test.ExampleConfig;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Scl implements ClientModInitializer {
    private static final String MOD_ID = "scl";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Scl initialized");
        ConfigRegister.getInstance().register(ExampleConfig.getInstance());
    }
}
