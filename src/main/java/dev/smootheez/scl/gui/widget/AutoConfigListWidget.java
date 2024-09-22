package dev.smootheez.scl.gui.widget;

import com.google.common.collect.Maps;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import dev.smootheez.scl.annotation.Config;
import dev.smootheez.scl.api.ConfigProvider;
import dev.smootheez.scl.option.ConfigOption;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class AutoConfigListWidget extends ConfigListWidget {
    private final ConfigProvider provider;
    private final Map<String, Map<String, ConfigOption<?>>> categoryWidgets = Maps.newHashMap();
    private final Map<String, ConfigOption<?>> uncategorizedWidgets = Maps.newHashMap();

    public AutoConfigListWidget(ConfigProvider provider, String modId) {
        this.provider = provider;

        Config configAnnotation = provider.getClass().getAnnotation(Config.class);
        if (configAnnotation == null) {
            throw new IllegalArgumentException("AutoConfigListWidget must be annotated with @Config");
        }
        autoConfigEntrys(modId);
    }

    private void autoConfigEntrys(String modId) {
        for (Field field : provider.getClass().getDeclaredFields()) {
            if (field.getType() == ConfigOption.class) {
                field.setAccessible(true);
                try {
                    ConfigOption<?> option = (ConfigOption<?>) field.get(provider);
                    Config.Category categoryAnnotation = field.getAnnotation(Config.Category.class);
                    if (categoryAnnotation != null) {
                        String category = categoryAnnotation.value();
                        categoryWidgets.computeIfAbsent(category, k -> new HashMap<>())
                                .put(option.getKey(), option);
                    } else {
                        uncategorizedWidgets.put(option.getKey(), option);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        addWidgetsFromMap(new TreeMap<>(uncategorizedWidgets), modId);
        addCategoryWidget(categoryWidgets, modId);
    }

    private void addWidgetsFromMap(Map<String, ConfigOption<?>> map, String modId) {
        map.forEach((key, option) -> {
            AbstractConfigWidget widget = createWidget(option, modId);
            addEntry(widget);
        });
    }

    private void addCategoryWidget(Map<String, Map<String, ConfigOption<?>>> map, String modId) {
        Map<String, Map<String, ConfigOption<?>>> sortedCategoryWidgets = new TreeMap<>(map);
        sortedCategoryWidgets.forEach((categoryName, categoryOptions) -> {
            ConfigCategoryWidget categoryWidget = new ConfigCategoryWidget(Text.translatable("category." + modId + "." + categoryName).formatted(Formatting.BOLD, Formatting.GOLD));
            addEntry(categoryWidget);

            addWidgetsFromMap(new TreeMap<>(categoryOptions), modId);
        });
    }
}
