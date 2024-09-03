package net.smootheez.scl.registry;

import net.smootheez.scl.api.ConfigFileProvider;
import net.smootheez.scl.file.ConfigFileWriter;

import java.util.HashMap;
import java.util.Map;

public class ConfigRegister {
    private static final ConfigRegister INSTANCE = new ConfigRegister();
    private final Map<Class<? extends ConfigFileProvider>, ConfigFileWriter> configWriters;

    private ConfigRegister() {
        this.configWriters = new HashMap<>();
    }

    public static ConfigRegister getInstance() {
        return INSTANCE;
    }

    public <T extends ConfigFileProvider> void register(T config) {
        ConfigFileWriter writer = new ConfigFileWriter(config);
        configWriters.put(config.getClass(), writer);
        writer.loadConfig();
    }

    public <T extends ConfigFileProvider> void save(Class<T> configClass) {
        ConfigFileWriter writer = configWriters.get(configClass);
        if (writer != null) {
            writer.saveConfig();
        } else {
            throw new IllegalArgumentException("Config class not registered: " + configClass.getName());
        }
    }

    public <T extends ConfigFileProvider> void reload(Class<T> configClass) {
        ConfigFileWriter writer = configWriters.get(configClass);
        if (writer != null) {
            writer.loadConfig();
        } else {
            throw new IllegalArgumentException("Config class not registered: " + configClass.getName());
        }
    }

    public void saveAll() {
        for (ConfigFileWriter writer : configWriters.values()) {
            writer.saveConfig();
        }
    }

    public void reloadAll() {
        for (ConfigFileWriter writer : configWriters.values()) {
            writer.loadConfig();
        }
    }
}