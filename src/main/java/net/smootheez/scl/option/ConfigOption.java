package net.smootheez.scl.option;

import net.smootheez.scl.serializer.ConfigSerializer;

import java.util.Arrays;

public class ConfigOption<T> {
    protected final String key;
    protected T value;
    protected T maxValue;
    protected final Class<T> type;
    protected final ConfigSerializer<T> serializer;

    public ConfigOption(String key, T defaultValue, Class<T> type, ConfigSerializer<T> serializer) {
        this.key = key;
        this.value = defaultValue;
        this.type = type;
        this.serializer = serializer;
    }

    public ConfigOption(String key, T defaultValue, Class<T> type, ConfigSerializer<T> serializer, T maxValue) {
        this.key = key;
        this.value = defaultValue;
        this.type = type;
        this.serializer = serializer;
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

    public static ConfigOption<Boolean> create(String key, Boolean defaultValue) {
        return new ConfigOption<>(key, defaultValue, Boolean.class, new ConfigSerializer.BooleanSerializer());
    }

    public static ConfigOption<ConfigOptionList> create(String key, String... defaultValues) {
        return new ConfigOption<>(key, new ConfigOptionList(Arrays.asList(defaultValues)), ConfigOptionList.class, new ConfigSerializer.ConfigOptionListSerializer());
    }

    public static ConfigOption<Integer> create(String key, Integer defaultValue, Integer maxValue) {
        return new ConfigOption<>(key, defaultValue, Integer.class, new ConfigSerializer.IntegerSerializer(), maxValue);
    }

    public static ConfigOption<Double> create(String key, Double defaultValue) {
        return new ConfigOption<>(key, defaultValue, Double.class, new ConfigSerializer.FloatSerializer(), 1.0);
    }

    public static <E extends Enum<E>> ConfigOption<E> create(String key, E defaultValue) {
        @SuppressWarnings("unchecked")
        Class<E> enumClass = (Class<E>) defaultValue.getClass();
        return new ConfigOption<>(key, defaultValue, enumClass, new ConfigSerializer.EnumSerializer<>(enumClass));
    }
}