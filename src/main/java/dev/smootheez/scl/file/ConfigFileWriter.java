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
        this.configProvider = configProvider;
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
        for (Field field : configProvider.getClass().getDeclaredFields()) {
            if (field.getType() == ConfigOption.class) {
                field.setAccessible(true);
                try {
                    ConfigOption<?> option = (ConfigOption<?>) field.get(configProvider);
                    Config.Category categoryAnnotation = field.getAnnotation(Config.Category.class);

                    if (categoryAnnotation != null) {
                        String category = categoryAnnotation.value();
                        categoryAdapters
                                .computeIfAbsent(category, k -> new TreeMap<>())
                                .put(option.getKey(), createAdapter(option));
                    } else {
                        uncategorizedAdapters.put(option.getKey(), createAdapter(option));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
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

        try (FileReader reader = new FileReader(configFile)) {
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            fromJson(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveConfig() {
        try (FileWriter writer = new FileWriter(configFile)) {
            JsonObject json = toJson();
            gson.toJson(json, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fromJson(JsonObject json) {
        for (Map.Entry<String, ConfigOptionAdapter<?>> entry : uncategorizedAdapters.entrySet()) {
            String key = entry.getKey();
            ConfigOptionAdapter<?> adapter = entry.getValue();
            if (json.has(key)) {
                JsonElement element = json.get(key);
                adapter.fromJson(element);
            }
        }

        for (Map.Entry<String, JsonElement> categoryEntry : json.entrySet()) {
            String category = categoryEntry.getKey();
            if (categoryAdapters.containsKey(category)) {
                JsonObject categoryObject = categoryEntry.getValue().getAsJsonObject();
                Map<String, ConfigOptionAdapter<?>> adapters = categoryAdapters.get(category);
                for (Map.Entry<String, ConfigOptionAdapter<?>> entry : adapters.entrySet()) {
                    String key = entry.getKey();
                    ConfigOptionAdapter<?> adapter = entry.getValue();
                    if (categoryObject.has(key)) {
                        JsonElement element = categoryObject.get(key);
                        adapter.fromJson(element);
                    }
                }
            }
        }
    }

    private JsonObject toJson() {
        JsonObject json = new JsonObject();

        for (Map.Entry<String, ConfigOptionAdapter<?>> entry : uncategorizedAdapters.entrySet()) {
            String key = entry.getKey();
            ConfigOptionAdapter<?> adapter = entry.getValue();
            json.add(key, adapter.toJson());
        }

        for (Map.Entry<String, Map<String, ConfigOptionAdapter<?>>> categoryEntry : categoryAdapters.entrySet()) {
            String category = categoryEntry.getKey();
            JsonObject categoryObject = new JsonObject();
            Map<String, ConfigOptionAdapter<?>> adapters = categoryEntry.getValue();

            for (Map.Entry<String, ConfigOptionAdapter<?>> entry : adapters.entrySet()) {
                String key = entry.getKey();
                ConfigOptionAdapter<?> adapter = entry.getValue();
                categoryObject.add(key, adapter.toJson());
            }

            json.add(category, categoryObject);
        }

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