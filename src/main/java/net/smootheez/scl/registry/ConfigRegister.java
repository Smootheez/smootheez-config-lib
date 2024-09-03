package net.smootheez.scl.registry;

import net.smootheez.scl.api.ConfigFileProvider;
import net.smootheez.scl.file.ConfigFileBuilder;

import java.util.HashMap;
import java.util.Map;

public class ConfigRegister {
    private static final ConfigRegister INSTANCE = new ConfigRegister();
    private final Map<Class<? extends ConfigFileProvider>, ConfigFileBuilder> configWriters;

    private ConfigRegister() {
        this.configWriters = new HashMap<>();
    }

    public static ConfigRegister getInstance() {
        return INSTANCE;
    }

    public <T extends ConfigFileProvider> void register(T config) {
        ConfigFileBuilder writer = new ConfigFileBuilder(config);
        configWriters.put(config.getClass(), writer);
        writer.loadConfig();
    }

    public <T extends ConfigFileProvider> void save(Class<T> configClass) {
        ConfigFileBuilder writer = configWriters.get(configClass);
        if (writer != null) {
            writer.saveConfig();
        } else {
            throw new IllegalArgumentException("Config class not registered: " + configClass.getName());
        }
    }

    public <T extends ConfigFileProvider> void reload(Class<T> configClass) {
        ConfigFileBuilder writer = configWriters.get(configClass);
        if (writer != null) {
            writer.loadConfig();
        } else {
            throw new IllegalArgumentException("Config class not registered: " + configClass.getName());
        }
    }

    public void saveAll() {
        for (ConfigFileBuilder writer : configWriters.values()) {
            writer.saveConfig();
        }
    }

    public void reloadAll() {
        for (ConfigFileBuilder writer : configWriters.values()) {
            writer.loadConfig();
        }
    }
}