package dev.smootheez.scl.gui.widget;

import dev.smootheez.scl.annotation.Config;
import dev.smootheez.scl.api.ConfigProvider;
import dev.smootheez.scl.option.ConfigOption;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class AutoConfigListWidget extends ConfigListWidget {
    private final ConfigProvider provider;
    private final String modId;
    private final Map<String, Map<String, ConfigOption<?>>> categoryWidgets = new HashMap<>();
    private final Map<String, ConfigOption<?>> uncategorizedWidgets = new HashMap<>();
    private boolean sorted = false;

    public AutoConfigListWidget(ConfigProvider provider) {
        this.provider = Objects.requireNonNull(provider, "Provider cannot be null");
        Config configAnnotation = provider.getClass().getAnnotation(Config.class);
        if (configAnnotation == null) {
            throw new IllegalArgumentException("AutoConfigListWidget must be annotated with @Config");
        }
        this.modId = configAnnotation.value();
    }

    public AutoConfigListWidget sorted() {
        this.sorted = true;
        return this;
    }

    public AutoConfigListWidget build() {
        autoConfigEntries();
        return this;
    }

    private void autoConfigEntries() {
        for (Field field : provider.getClass().getDeclaredFields()) {
            if (field.getType() == ConfigOption.class) {
                field.setAccessible(true);
                try {
                    ConfigOption<?> option = (ConfigOption<?>) field.get(provider);
                    Config.Category categoryAnnotation = field.getAnnotation(Config.Category.class);
                    if (categoryAnnotation != null) {
                        categoryWidgets.computeIfAbsent(categoryAnnotation.value(), k -> new HashMap<>())
                                .put(option.getKey(), option);
                    } else {
                        uncategorizedWidgets.put(option.getKey(), option);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error accessing field", e);
                }
            }
        }
        addWidgetsFromMap(sorted ? new TreeMap<>(uncategorizedWidgets) : uncategorizedWidgets, modId);
        addCategoryWidget(categoryWidgets);
    }

    private void addWidgetsFromMap(Map<String, ConfigOption<?>> map, String modId) {
        map.forEach((key, option) -> {
            var widget = createWidget(option, modId);
            addEntry(widget);
        });
    }

    private void addCategoryWidget(Map<String, Map<String, ConfigOption<?>>> map) {
        var sortedCategoryWidgets = new TreeMap<>(map);
        sortedCategoryWidgets.forEach((categoryName, categoryOptions) -> {
            var categoryWidget = new ConfigCategoryWidget(Text.translatable("category.%s.%s", modId, categoryName)
                    .formatted(Formatting.BOLD, Formatting.GOLD));
            addEntry(categoryWidget);
            addWidgetsFromMap(sorted ? new TreeMap<>(categoryOptions) : categoryOptions, modId);
        });
    }
}
