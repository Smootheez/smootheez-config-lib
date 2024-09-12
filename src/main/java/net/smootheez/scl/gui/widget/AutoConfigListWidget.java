package net.smootheez.scl.gui.widget;

import com.google.common.collect.Maps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.smootheez.scl.annotation.Config;
import net.smootheez.scl.api.ConfigProvider;
import net.smootheez.scl.option.ConfigOption;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

public class AutoConfigListWidget extends ConfigListWidget {
    private final ConfigProvider provider;
    private final Map<String, Map<String, ConfigOption<?>>> categoryWidgets = Maps.newHashMap();
    private final Map<String, ConfigOption<?>> uncategorizedWidgets = Maps.newHashMap();

    public AutoConfigListWidget(MinecraftClient client, ConfigProvider provider) {
        super(client);
        this.provider = provider;

        Config configAnnotation = provider.getClass().getAnnotation(Config.class);
        if (configAnnotation == null) {
            throw new IllegalArgumentException("AutoConfigListWidget must be annotated with @Config");
        }
        autoConfigEntrys();
    }

    private void autoConfigEntrys() {
        for (Field field : provider.getClass().getDeclaredFields()) {
            if (field.getType() == ConfigOption.class) {
                field.setAccessible(true);
                try {
                    ConfigOption<?> option = (ConfigOption<?>) field.get(provider);
                    Config.Category categoryAnnotation = field.getAnnotation(Config.Category.class);
                    if (categoryAnnotation != null) {
                        String categoryName = categoryAnnotation.value();
                    } else {
                        uncategorizedWidgets.put(option.getKey(), option);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
