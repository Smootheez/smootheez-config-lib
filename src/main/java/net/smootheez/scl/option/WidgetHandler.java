package net.smootheez.scl.option;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.smootheez.scl.gui.widget.*;

import java.util.List;

public interface WidgetHandler<T> {
    ConfigListWidget.AbstractConfigWidget createWidget(ConfigOption<T> option, List<OrderedText> description, MinecraftClient client);

    class BooleanWidgetHandler implements WidgetHandler<Boolean> {
        @Override
        public ConfigListWidget.AbstractConfigWidget createWidget(ConfigOption<Boolean> option, List<OrderedText> description, MinecraftClient client) {
            return new BooleanConfigWidget(Text.translatable(option.getTranslationKey()), description, option, client);
        }
    }

    class CycleWidgetHandler<E extends Enum<E>> implements WidgetHandler<E> {
        @Override
        public ConfigListWidget.AbstractConfigWidget createWidget(ConfigOption<E> option, List<OrderedText> description, MinecraftClient client) {
            return new CycleConfigWidget<>(Text.translatable(option.getTranslationKey()), description, option, client);
        }
    }

    class IntWidgetHandler implements WidgetHandler<Integer> {
        @Override
        public ConfigListWidget.AbstractConfigWidget createWidget(ConfigOption<Integer> option, List<OrderedText> description, MinecraftClient client) {
            return new IntConfigTextWidget(Text.translatable(option.getTranslationKey()), description, option, client);
        }
    }

    class TextWidgetHandler implements WidgetHandler<ConfigOptionList> {
        @Override
        public ConfigListWidget.AbstractConfigWidget createWidget(ConfigOption<ConfigOptionList> option, List<OrderedText> description, MinecraftClient client) {
            return new TextConfigWidget(Text.translatable(option.getTranslationKey()), description, option, client);
        }
    }

    class DoubleWidgetHandler implements WidgetHandler<Double> {
        @Override
        public ConfigListWidget.AbstractConfigWidget createWidget(ConfigOption<Double> option, List<OrderedText> description, MinecraftClient client) {
            return new DoubleConfigTextWidget(Text.translatable(option.getTranslationKey()), description, option, client);
        }
    }
}
