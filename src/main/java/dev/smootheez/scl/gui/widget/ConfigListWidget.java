package dev.smootheez.scl.gui.widget;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import dev.smootheez.scl.option.ConfigOption;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConfigListWidget extends ElementListWidget<ConfigListWidget.AbstractConfigWidget> {
    public ConfigListWidget() {
        super(MinecraftClient.getInstance(), MinecraftClient.getInstance().currentScreen != null ? MinecraftClient.getInstance().currentScreen.width : 0, (MinecraftClient.getInstance().currentScreen != null ? MinecraftClient.getInstance().currentScreen.height : 0) - 64, 32, 25);
    }

    public <T> AbstractConfigWidget createWidget(ConfigOption<T> option, String modId) {
        List<OrderedText> orderedTexts = createOrderedTextList(option, modId);
        return option.getWidgetHandler().createWidget(option, orderedTexts, modId);
    }

    private List<OrderedText> createOrderedTextList(ConfigOption<?> option, String modId) {
        String descriptionKey = option.getTranslationKey(modId) + ".description";
        Text descriptionText = Text.translatable(descriptionKey);
        Text defaultValueText = Text.translatable("options.scl.default", Text.literal(option.getDefaultValue().toString())).formatted(Formatting.GRAY);

        if (I18n.hasTranslation(descriptionKey)) {
            ImmutableList.Builder<OrderedText> builder = ImmutableList.builder();
            builder.add(Text.literal(option.getKey()).formatted(Formatting.YELLOW).asOrderedText());
            this.client.textRenderer.wrapLines(descriptionText, 200).forEach(builder::add);
            builder.add(defaultValueText.asOrderedText());
            return builder.build();
        } else {
            return ImmutableList.of(Text.literal(option.getKey()).formatted(Formatting.YELLOW).asOrderedText(), defaultValueText.asOrderedText());
        }
    }

    @Override
    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 76;
    }

    @Override
    public int getRowWidth() {
        return 400;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        AbstractConfigWidget abstractConfigWidget = this.getHoveredEntry();
        if (abstractConfigWidget != null && abstractConfigWidget.description != null && this.client.currentScreen != null) {
            this.client.currentScreen.setTooltip(abstractConfigWidget.description);
        }
    }

    public abstract static class AbstractConfigWidget extends Entry<AbstractConfigWidget> {
        @Nullable
        final List<OrderedText> description;

        protected AbstractConfigWidget(@Nullable List<OrderedText> description) {
            this.description = description;
        }
    }
}
