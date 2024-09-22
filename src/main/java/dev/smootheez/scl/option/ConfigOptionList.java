package dev.smootheez.scl.option;

import java.util.ArrayList;
import java.util.List;

public record ConfigOptionList(List<String> values) {

    public ConfigOptionList(List<String> values) {
        this.values = new ArrayList<>(values);
    }

    public List<String> values() {
        return new ArrayList<>(values);
    }

    public void addValue(String value) {
        values.add(value);
    }

    public void removeValue(String value) {
        values.remove(value);
    }

    public String getValue(int index) {
        if (index >= 0 && index < values.size()) {
            return values.get(index);
        }
        throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + values.size());
    }
}
