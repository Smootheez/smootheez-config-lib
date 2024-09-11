package net.smootheez.scl.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.smootheez.scl.example.ExampleConfig;
import net.smootheez.scl.option.ConfigOption;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ConfigListWidget extends ElementListWidget<ConfigListWidget.AbstractConfigWidget> {
    private final Screen screen;
    public ConfigListWidget(MinecraftClient client, int i, int j, int k, int l, Screen screen) {
        super(client, i, j, k, l);
        this.screen = screen;

//        Map<String, ConfigOption<Boolean>> options = new HashMap<>();
//        options.put(option.getTranslationKey(), option);
        var configOption = ExampleConfig.getInstance();

//        addWidgetsFromMap(options);
        addEntry(new CycleConfigWidget<>(Text.translatable(configOption.getExampleEnum().getTranslationKey()), null, configOption.getExampleEnum(), this.client));
        addEntry(new IntConfigTextWidget(Text.translatable(configOption.getExampleInteger().getTranslationKey()), null, configOption.getExampleInteger(), this.client));
        addEntry(new BooleanConfigWidget(Text.translatable(configOption.getExampleBoolean().getTranslationKey()), null, configOption.getExampleBoolean(), this.client));
        addEntry(new DoubleConfigTextWidget(Text.translatable(configOption.getExampleDouble().getTranslationKey()), null, configOption.getExampleDouble(), this.client));
        addEntry(new TextConfigWidget(Text.translatable(configOption.getExampleStringList().getTranslationKey()), null, configOption.getExampleStringList(), this.client));
    }

    private void addWidgetsFromMap(Map<String, ConfigOption<Boolean>> map) {
        for (Map.Entry<String, ConfigOption<Boolean>> entry : map.entrySet()) {
            String name = entry.getKey();
            ConfigOption<Boolean> option = entry.getValue();
            String string = option.getTranslationKey() + ".description";
            List<OrderedText> orderedTexts = I18n.hasTranslation(string) ? ConfigListWidget.this.client.textRenderer.wrapLines(Text.translatable(string), 200) : null;
            AbstractConfigWidget widget = new BooleanConfigWidget(Text.translatable(name), orderedTexts, option, this.client);
            addEntry(widget);
        }
    }

    @Override
    protected int getScrollbarPositionX() {
        return MinecraftClient.getInstance().getWindow().getWidth() / 2 - 11;
    }

    @Override
    public int getRowWidth() {
        return MinecraftClient.getInstance().getWindow().getWidth() / 2 - 20;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        AbstractConfigWidget abstractConfigWidget = this.getHoveredEntry();
            if (abstractConfigWidget != null && abstractConfigWidget.description != null) {
            screen.setTooltip(abstractConfigWidget.description);
        }
    }

    public abstract static class AbstractConfigWidget extends ElementListWidget.Entry<AbstractConfigWidget> {
        @Nullable
        final List<OrderedText> description;

        protected AbstractConfigWidget(@Nullable List<OrderedText> description) {
            this.description = description;
        }
    }
}
