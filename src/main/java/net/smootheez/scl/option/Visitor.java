package net.smootheez.scl.option;

public interface Visitor {
    default <T extends ConfigOption<T>> void visit(ConfigOption<T> option) {
    }

    default void visitBoolean(ConfigOption<Boolean> option) {
    }
}
