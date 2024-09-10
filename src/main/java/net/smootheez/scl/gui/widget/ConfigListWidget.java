package net.smootheez.scl.gui.widget;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.smootheez.scl.example.ExampleConfig;
import net.smootheez.scl.option.ConfigOption;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigListWidget extends ElementListWidget<ConfigListWidget.AbstractConfigWidget> {
    private final Screen screen;
    public ConfigListWidget(MinecraftClient client, int i, int j, int k, int l, Screen screen, ConfigOption<Boolean> option) {
        super(client, i, j, k, l);
        this.screen = screen;

        Map<String, ConfigOption<Boolean>> options = new HashMap<>();
        options.put(option.getTranslationKey(), option);

        addWidgetsFromMap(options);
        addEntry(new EnumCycleWidget<>(Text.of("Hello World"), null, ExampleConfig.getInstance().getExampleEnum()));
        addEntry(new SliderConfigWidget(Text.of("Example Double"), null, ExampleConfig.getInstance().getExampleDouble()));

//        addEntry(new BooleanConfigWidget(ExampleConfig.getInstance().getExampleBoolean1().getTranslationKey(), null, ExampleConfig.getInstance().getExampleBoolean1()));
//        addEntry(new BooleanConfigWidget(ExampleConfig.getInstance().getExampleBoolean2().getTranslationKey(), null, ExampleConfig.getInstance().getExampleBoolean2()));
//
//        addEntry(new CategoryEntry(Text.of(ExampleCategory.CATEGORY_ONE)));
//
//        addEntry(new BooleanConfigWidget(ExampleConfig.getInstance().getExampleBoolean().getTranslationKey(), orderedTexts, ExampleConfig.getInstance().getExampleBoolean()));
    }

    private void addWidgetsFromMap(Map<String, ConfigOption<Boolean>> map) {
        for (Map.Entry<String, ConfigOption<Boolean>> entry : map.entrySet()) {
            String name = entry.getKey();
            ConfigOption<Boolean> option = entry.getValue();
            String string = option.getTranslationKey() + ".description";
            List<OrderedText> orderedTexts = I18n.hasTranslation(string) ? ConfigListWidget.this.client.textRenderer.wrapLines(Text.translatable(string), 200) : null;
            AbstractConfigWidget widget = new BooleanConfigWidget(Text.translatable(name), orderedTexts, option);
            addEntry(widget);
        }
    }

    private void addCategoryWidget(Map<Text, Map<String, ConfigOption<Boolean>>> map) {
        for (Map.Entry<Text, Map<String, ConfigOption<Boolean>>> categoryEntry : map.entrySet()) {
            Text categoryName = categoryEntry.getKey();
            Map<String, ConfigOption<Boolean>> categoryOptions = categoryEntry.getValue();
            CategoryWidget categoryWidget = new CategoryWidget(categoryName);
            addEntry(categoryWidget);

            for (Map.Entry<String, ConfigOption<Boolean>> optionEntry : categoryOptions.entrySet()) {
                String optionNAme = optionEntry.getKey();
                ConfigOption<Boolean> option = optionEntry.getValue();
                String string = option.getTranslationKey() + ".description";
                List<OrderedText> orderedTexts = I18n.hasTranslation(string) ? ConfigListWidget.this.client.textRenderer.wrapLines(Text.translatable(string), 200) : null;
                AbstractConfigWidget widget = new BooleanConfigWidget(Text.translatable(optionNAme), orderedTexts, option);
                addEntry(widget);
            }
        }
    }

    @Override
    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 50;
    }

    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 100;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        AbstractConfigWidget abstractConfigWidget = this.getHoveredEntry();
            if (abstractConfigWidget != null && abstractConfigWidget.description != null) {
            screen.setTooltip(abstractConfigWidget.description);
        }
    }

    public class SliderConfigWidget extends NamedConfigWidget {
        private final SliderWidget slider;
        public SliderConfigWidget(Text name, List<OrderedText> description, ConfigOption<Double> option) {
            super(name, description);
            var value = option.getValue();
            slider = new SliderWidget(10, 5, 44, 20, Text.of(value.toString()), value) {
                @Override
                protected void updateMessage() {
                    slider.setMessage(Text.of(String.valueOf(value)));
                }

                @Override
                protected void applyValue() {
                    option.setValue(value);
                }
            };
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of(this.slider);
        }

        @Override
        public List<? extends Element> children() {
            return List.of(this.slider);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.drawName(context, y, x);
            this.slider.setX(x + entryWidth - 45);
            this.slider.setY(y);
            this.slider.render(context, mouseX, mouseY, tickDelta);
        }
    }

    public class EnumCycleWidget<E extends Enum<E>> extends NamedConfigWidget {
        private final CyclingButtonWidget<E> cycleButton;

        public EnumCycleWidget(Text name, List<OrderedText> description, ConfigOption<E> option) {
            super(name, description);
            E[] enumValues = option.getType().getEnumConstants();
            cycleButton = CyclingButtonWidget.<E>builder(e -> Text.of(e.name()))
                    .omitKeyText()
                    .values(enumValues)
                    .initially(option.getValue())
                    .build(10, 5, 44, 20, name, (button, value) -> option.setValue(value));
            this.children.add(this.cycleButton);
        }

        @Override
        public List<CyclingButtonWidget<E>> selectableChildren() {
            return List.of(this.cycleButton);
        }

        @Override
        public List<CyclingButtonWidget<E>> children() {
            return List.of(this.cycleButton);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.drawName(context, y, x);
            this.cycleButton.setX(x + entryWidth - 45);
            this.cycleButton.setY(y);
            this.cycleButton.render(context, mouseX, mouseY, tickDelta);
        }
    }


    public class BooleanConfigWidget extends NamedConfigWidget {
        private final CyclingButtonWidget<Boolean> toggleButton;

        public BooleanConfigWidget(Text name, List<OrderedText> description, ConfigOption<Boolean> option) {
            super(name, description);
            this.toggleButton = CyclingButtonWidget.onOffBuilder(option.getValue())
                    .omitKeyText()
                    .build(10, 5, 44, 20, name, (button, value) -> option.setValue(value));
            this.children.add(this.toggleButton);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of(this.toggleButton);
        }

        @Override
        public List<? extends Element> children() {
            return List.of(this.toggleButton);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.drawName(context, y, x);
            this.toggleButton.setX(x + entryWidth - 45);
            this.toggleButton.setY(y);
            this.toggleButton.render(context, mouseX, mouseY, tickDelta);
        }
    }

    public abstract class NamedConfigWidget extends AbstractConfigWidget {
        private final List<OrderedText> name;
        protected final List<ClickableWidget> children = Lists.newArrayList();

        public NamedConfigWidget(Text name, @Nullable List<OrderedText> description) {
            super(description);
            this.name = ConfigListWidget.this.client.textRenderer.wrapLines(name, 175);
        }

        @Override
        public List<? extends Element> children() {
            return this.children;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return this.children;
        }

        protected void drawName(DrawContext context, int x, int y) {
            if (this.name.size() == 1) {
                context.drawText(ConfigListWidget.this.client.textRenderer, this.name.get(0), y, x + 5, 16777215, false);
            } else if (this.name.size() >= 2) {
                context.drawText(ConfigListWidget.this.client.textRenderer, this.name.get(0), y, x, 16777215, false);
                context.drawText(ConfigListWidget.this.client.textRenderer, this.name.get(1), y, x + 10, 16777215, false);
            }
        }
    }

    public class CategoryWidget extends AbstractConfigWidget {
        private final Text text;

        public CategoryWidget(Text text) {
            super(null);
            this.text = text;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawCenteredTextWithShadow(ConfigListWidget.this.client.textRenderer, this.text, x + entryWidth / 2, y + 5, 16777215);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of();
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(new Selectable() {
                @Override
                public Selectable.SelectionType getType() {
                    return Selectable.SelectionType.HOVERED;
                }

                @Override
                public void appendNarrations(NarrationMessageBuilder builder) {
                    builder.put(NarrationPart.TITLE, CategoryWidget.this.text);
                }
            });
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
