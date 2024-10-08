package net.smootheez.scl.option;

import net.smootheez.scl.serializer.ConfigSerializer;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class ConfigOption<T> {
    protected final String key;
    protected T value;
    protected T maxValue;
    protected final T defaultValue;

    protected final Class<T> type;
    protected final ConfigSerializer<T> serializer;
    protected final WidgetHandler<T> widgetHandler;

    public ConfigOption(String key, T defaultValue, Class<T> type, ConfigSerializer<T> serializer, WidgetHandler<T> widgetHandler) {
        this.key = key;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.type = type;
        this.serializer = serializer;
        this.widgetHandler = widgetHandler;
    }

    public ConfigOption(String key, T defaultValue, Class<T> type, ConfigSerializer<T> serializer, WidgetHandler<T> widgetHandler, T maxValue) {
        this.key = key;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.type = type;
        this.serializer = serializer;
        this.widgetHandler = widgetHandler;
        this.maxValue = maxValue;
    }

    public String getKey() {
        return key;
    }

    public String getTranslationKey() {
        return "options." + key;
    }

    public T getMaxValue() {
        return maxValue;
    }

    public Class<T> getType() {
        return type;
    }

    public WidgetHandler<T> getWidgetHandler() {
        return widgetHandler;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        if (type.isInstance(value)) {
            this.value = value;
        } else {
            throw new IllegalArgumentException("Value must be of type " + type.getSimpleName());
        }
    }

    public ConfigSerializer<T> getSerializer() {
        return serializer;
    }

    public boolean validateIntValue(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean validateDoubleValue(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static ConfigOption<Boolean> create(String key, Boolean defaultValue) {
        return new ConfigOption<>(key,
                defaultValue, Boolean.class, new ConfigSerializer.BooleanSerializer(), new WidgetHandler.BooleanWidgetHandler());
    }

    public static ConfigOption<ConfigOptionList> create(String key, String... defaultValues) {
        return new ConfigOption<>(key,
                new ConfigOptionList(Arrays.asList(defaultValues)), ConfigOptionList.class, new ConfigSerializer.ConfigOptionListSerializer(), new WidgetHandler.TextWidgetHandler());
    }

    public static ConfigOption<Integer> create(String key, Integer defaultValue, Integer maxValue) {
        return new ConfigOption<>(key,
                defaultValue, Integer.class, new ConfigSerializer.IntegerSerializer(), new WidgetHandler.IntWidgetHandler(), maxValue);
    }

    public static ConfigOption<Double> create(String key, Double defaultValue) {
        return new ConfigOption<>(key,
                defaultValue, Double.class, new ConfigSerializer.FloatSerializer(), new WidgetHandler.DoubleWidgetHandler(), 1.0);
    }

    public static <E extends Enum<E>> ConfigOption<E> create(String key, E defaultValue) {
        @SuppressWarnings("unchecked")
        Class<E> enumClass = (Class<E>) defaultValue.getClass();
        return new ConfigOption<>(key,
                defaultValue, enumClass, new ConfigSerializer.EnumSerializer<>(enumClass), new WidgetHandler.CycleWidgetHandler<>());
    }
}