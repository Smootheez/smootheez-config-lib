package net.smootheez.scl;

import net.fabricmc.api.ClientModInitializer;
import net.smootheez.scl.api.ConfigProvider;
import net.smootheez.scl.example.ExampleConfig;
import net.smootheez.scl.file.ConfigFileWriter;
import net.smootheez.scl.registry.ConfigRegister;


public class Scl implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ConfigRegister.getInstance().register(ExampleConfig.getInstance());
    }
}
