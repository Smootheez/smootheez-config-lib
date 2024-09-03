package net.smootheez.scl.option;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.smootheez.scl.example.ExampleConfig;
import net.smootheez.scl.serializer.ConfigSerializer;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SimpleOption<T> {
    protected final String key;
    protected T value;
    protected final Class<T> type;
    protected final ConfigSerializer<T> serializer;

    public SimpleOption(String key, T defaultValue, Class<T> type, ConfigSerializer<T> serializer) {
        this.key = key;
        this.value = defaultValue;
        this.type = type;
        this.serializer = serializer;
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

    public static SimpleOption<Boolean> ofBoolean(String key, Boolean defaultValue) {
        return new SimpleOption<>(key, defaultValue, Boolean.class, new ConfigSerializer.BooleanSerializer());
    }

    @SuppressWarnings("unchecked")
    public static SimpleOption<List<String>> ofStringList(String key, List<String> defaultValue) {
        return new SimpleOption<>(key, defaultValue, (Class<List<String>>) (Class<?>) List.class, new ConfigSerializer.StringListSerializer());
    }

    public static SimpleOption<Integer> ofInteger(String key, Integer defaultValue) {
        return new SimpleOption<>(key, defaultValue, Integer.class, new ConfigSerializer.IntegerSerializer());
    }

    public static SimpleOption<Float> ofFloat(String key, Float defaultValue) {
        return new SimpleOption<>(key, defaultValue, Float.class, new ConfigSerializer.FloatSerializer());
    }

    public static <E extends Enum<E>> SimpleOption<E> ofEnum(String key, E defaultValue) {
        @SuppressWarnings("unchecked")
        Class<E> enumClass = (Class<E>) defaultValue.getClass();
        return new SimpleOption<>(key, defaultValue, enumClass, new ConfigSerializer.EnumSerializer<>(enumClass));
    }

    interface Callbacks<T> {
        Function<SimpleOption<T>, ClickableWidget> getWidgetCreator(ExampleConfig config, int x, int y, int width, Consumer<T> changeCallback);
    }
}