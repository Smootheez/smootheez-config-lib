package net.smootheez.scl.option;

import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.smootheez.scl.gui.widget.*;

import java.util.List;

public interface WidgetHandler<T> {
    ConfigListWidget.AbstractConfigWidget createWidget(ConfigOption<T> option, List<OrderedText> description, String modId);

    class BooleanWidgetHandler implements WidgetHandler<Boolean> {
        @Override
        public ConfigListWidget.AbstractConfigWidget createWidget(ConfigOption<Boolean> option, List<OrderedText> description, String modId) {
            return new BooleanConfigWidget(Text.translatable(option.getTranslationKey(modId)), description, option);
        }
    }

    class CycleWidgetHandler<E extends Enum<E>> implements WidgetHandler<E> {
        @Override
        public ConfigListWidget.AbstractConfigWidget createWidget(ConfigOption<E> option, List<OrderedText> description, String modId) {
            return new CycleConfigWidget<>(Text.translatable(option.getTranslationKey(modId)), description, option);
        }
    }

    class IntWidgetHandler implements WidgetHandler<Integer> {
        @Override
        public ConfigListWidget.AbstractConfigWidget createWidget(ConfigOption<Integer> option, List<OrderedText> description, String modId) {
            return new IntConfigTextWidget(Text.translatable(option.getTranslationKey(modId)), description, option);
        }
    }

    class TextWidgetHandler implements WidgetHandler<ConfigOptionList> {
        @Override
        public ConfigListWidget.AbstractConfigWidget createWidget(ConfigOption<ConfigOptionList> option, List<OrderedText> description, String modId) {
            return new TextConfigWidget(Text.translatable(option.getTranslationKey(modId)), description, option);
        }
    }

    class DoubleWidgetHandler implements WidgetHandler<Double> {
        @Override
        public ConfigListWidget.AbstractConfigWidget createWidget(ConfigOption<Double> option, List<OrderedText> description, String modId) {
            return new DoubleConfigTextWidget(Text.translatable(option.getTranslationKey(modId)), description, option);
        }
    }
}
