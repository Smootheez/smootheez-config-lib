package dev.smootheez.scl.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import dev.smootheez.scl.annotation.Config;
import dev.smootheez.scl.api.ConfigProvider;
import dev.smootheez.scl.option.ConfigOption;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class ConfigFileWriter {
    private final Gson gson;
    private final File configFile;
    private final ConfigProvider configProvider;
    private final Map<String, Map<String, ConfigOptionAdapter<?>>> categoryAdapters;
    private final Map<String, ConfigOptionAdapter<?>> uncategorizedAdapters;

    public ConfigFileWriter(ConfigProvider configProvider) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.configProvider = Objects.requireNonNull(configProvider, "ConfigProvider cannot be null");
        this.categoryAdapters = new TreeMap<>();
        this.uncategorizedAdapters = new TreeMap<>();

        Config configAnnotation = configProvider.getClass().getAnnotation(Config.class);
        if (configAnnotation == null) {
            throw new IllegalArgumentException("ConfigFileWriter must be annotated with @Config");
        }
        String configName = configAnnotation.value();
        this.configFile = FabricLoader.getInstance().getConfigDir().resolve(configName + ".json").toFile();

        initializeAdapters();
    }

    private void initializeAdapters() {
        Set<String> usedKeys = new HashSet<>();

        for (Field field : configProvider.getClass().getDeclaredFields()) {
            if (field.getType() == ConfigOption.class) {
                field.setAccessible(true);
                try {
                    ConfigOption<?> option = (ConfigOption<?>) field.get(configProvider);
                    String key = option.getKey();

                    if (usedKeys.contains(key)) {
                        throw new IllegalStateException("Duplicate key found: " + key);
                    }
                    usedKeys.add(key);

                    Config.Category categoryAnnotation = field.getAnnotation(Config.Category.class);

                    if (categoryAnnotation != null) {
                        String category = categoryAnnotation.value();
                        categoryAdapters
                                .computeIfAbsent(category, k -> new TreeMap<>())
                                .put(key, createAdapter(option));
                    } else {
                        uncategorizedAdapters.put(key, createAdapter(option));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error accessing field", e);
                }
            }
        }
    }

    private <T> ConfigOptionAdapter<T> createAdapter(ConfigOption<T> option) {
        return new ConfigOptionAdapter<>(option);
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            saveConfig();
            return;
        }

        try (var reader = new FileReader(configFile)) {
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            fromJson(json);
        } catch (IOException e) {
            throw new RuntimeException("Error loading config", e);
        }
    }

    public void saveConfig() {
        try (var writer = new FileWriter(configFile)) {
            JsonObject json = toJson();
            gson.toJson(json, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error saving config", e);
        }
    }

    private void fromJson(JsonObject json) {
        uncategorizedAdapters.forEach((key, adapter) -> {
            if (json.has(key)) {
                adapter.fromJson(json.get(key));
            }
        });

        json.entrySet().forEach(categoryEntry -> {
            String category = categoryEntry.getKey();
            if (categoryAdapters.containsKey(category)) {
                JsonObject categoryObject = categoryEntry.getValue().getAsJsonObject();
                categoryAdapters.get(category).forEach((key, adapter) -> {
                    if (categoryObject.has(key)) {
                        adapter.fromJson(categoryObject.get(key));
                    }
                });
            }
        });
    }

    private JsonObject toJson() {
        JsonObject json = new JsonObject();

        uncategorizedAdapters.forEach((key, adapter) -> json.add(key, adapter.toJson()));

        categoryAdapters.forEach((category, adapters) -> {
            JsonObject categoryObject = new JsonObject();
            adapters.forEach((key, adapter) -> categoryObject.add(key, adapter.toJson()));
            json.add(category, categoryObject);
        });

        return json;
    }

    private record ConfigOptionAdapter<T>(ConfigOption<T> option) {
        void fromJson(JsonElement json) {
            option.setValue(option.getSerializer().deserialize(json));
        }

        JsonElement toJson() {
            return option.getSerializer().serialize(option.getValue());
        }
    }
}