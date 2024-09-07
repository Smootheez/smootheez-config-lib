package net.smootheez.scl.serializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.smootheez.scl.option.ConfigOptionList;

import java.util.ArrayList;
import java.util.List;

public interface ConfigSerializer<T> {
    JsonElement serialize(T value);
    T deserialize(JsonElement json);

    class BooleanSerializer implements ConfigSerializer<Boolean> {
        @Override
        public JsonElement serialize(Boolean value) {
            return new JsonPrimitive(value);
        }

        @Override
        public Boolean deserialize(JsonElement json) {
            return json.getAsBoolean();
        }
    }

    class ConfigOptionListSerializer implements ConfigSerializer<ConfigOptionList> {
        @Override
        public JsonElement serialize(ConfigOptionList value) {
            JsonArray array = new JsonArray();
            for (String s : value.values()) {
                array.add(new JsonPrimitive(s));
            }
            return array;
        }

        @Override
        public ConfigOptionList deserialize(JsonElement json) {
            JsonArray array = json.getAsJsonArray();
            List<String> list = new ArrayList<>();
            for (JsonElement element : array) {
                list.add(element.getAsString());
            }
            return new ConfigOptionList(list);
        }
    }

    class IntegerSerializer implements ConfigSerializer<Integer> {
        @Override
        public JsonElement serialize(Integer value) {
            return new JsonPrimitive(value);
        }

        @Override
        public Integer deserialize(JsonElement json) {
            return json.getAsInt();
        }
    }

    class FloatSerializer implements ConfigSerializer<Double> {
        @Override
        public JsonElement serialize(Double value) {
            return new JsonPrimitive(value);
        }

        @Override
        public Double deserialize(JsonElement json) {
            return json.getAsDouble();
        }
    }

    class EnumSerializer<E extends Enum<E>> implements ConfigSerializer<E> {
        private final Class<E> enumClass;

        public EnumSerializer(Class<E> enumClass) {
            this.enumClass = enumClass;
        }

        @Override
        public JsonElement serialize(E value) {
            return new JsonPrimitive(value.name());
        }

        @Override
        public E deserialize(JsonElement json) {
            return Enum.valueOf(enumClass, json.getAsString());
        }
    }
}