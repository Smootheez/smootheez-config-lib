package net.smootheez.scl.option;

import net.minecraft.text.Text;
import net.smootheez.scl.serializer.ConfigSerializer;

import java.util.List;

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

    public Text getTranslationKey() {
        return Text.translatable("options." + key);
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

    public Class<T> getType() {
        return type;
    }

    public ConfigSerializer<T> getSerializer() {
        return serializer;
    }

    public static ConfigOption<Boolean> ofBoolean(String key, Boolean defaultValue) {
        return new ConfigOption<>(key, defaultValue, Boolean.class, new ConfigSerializer.BooleanSerializer());
    }

    @SuppressWarnings("unchecked")
    public static ConfigOption<List<String>> ofStringList(String key, List<String> defaultValue) {
        return new ConfigOption<>(key, defaultValue, (Class<List<String>>) (Class<?>) List.class, new ConfigSerializer.StringListSerializer());
    }

    public static ConfigOption<Integer> ofInteger(String key, Integer defaultValue, Integer maxValue) {
        return new ConfigOption<>(key, defaultValue, Integer.class, new ConfigSerializer.IntegerSerializer(), maxValue);
    }

    public static ConfigOption<Double> ofDouble(String key, Double defaultValue, Double maxValue) {
        return new ConfigOption<>(key, defaultValue, Double.class, new ConfigSerializer.FloatSerializer(), maxValue);
    }

    public static <E extends Enum<E>> ConfigOption<E> ofEnum(String key, E defaultValue) {
        @SuppressWarnings("unchecked")
        Class<E> enumClass = (Class<E>) defaultValue.getClass();
        return new ConfigOption<>(key, defaultValue, enumClass, new ConfigSerializer.EnumSerializer<>(enumClass));
    }
}