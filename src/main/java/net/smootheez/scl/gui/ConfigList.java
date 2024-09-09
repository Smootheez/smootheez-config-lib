package net.smootheez.scl.gui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.smootheez.scl.example.ExampleCategory;
import net.smootheez.scl.example.ExampleConfig;
import net.smootheez.scl.option.ConfigOption;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigList extends ElementListWidget<ConfigList.AbstractConfigWidget> {
    private final Screen screen;
    public ConfigList(MinecraftClient client, int i, int j, int k, int l, Screen screen) {
        super(client, i, j, k, l);
        this.screen = screen;

        ExampleConfig config = ExampleConfig.getInstance();
        Map<String, ConfigOption<Boolean>> map = new HashMap<>();
        map.put(config.getExampleBoolean().getKey(), config.getExampleBoolean());
        map.put(config.getExampleBoolean1().getKey(), config.getExampleBoolean1());
        addWidgetsFromMap(map);

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
            List<OrderedText> orderedTexts = I18n.hasTranslation(string) ? ConfigList.this.client.textRenderer.wrapLines(Text.translatable(string), 200) : null;
            AbstractConfigWidget widget = new BooleanConfigWidget(Text.of(name), orderedTexts, option);
            addEntry(widget);
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

    public class BooleanConfigWidget extends NamedConfigWidget {
        private final CyclingButtonWidget<Boolean> toggleButton;

        public BooleanConfigWidget(Text name, List<OrderedText> description, ConfigOption<Boolean> option) {
            super(description, name);
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

        public NamedConfigWidget(@Nullable List<OrderedText> description, Text name) {
            super(description);
            this.name = ConfigList.this.client.textRenderer.wrapLines(name, 175);
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
                context.drawText(ConfigList.this.client.textRenderer, this.name.get(0), y, x + 5, 16777215, false);
            } else if (this.name.size() >= 2) {
                context.drawText(ConfigList.this.client.textRenderer, this.name.get(0), y, x, 16777215, false);
                context.drawText(ConfigList.this.client.textRenderer, this.name.get(1), y, x + 10, 16777215, false);
            }
        }
    }

    public class CategoryEntry extends AbstractConfigWidget {
        private final Text text;

        public CategoryEntry(Text text) {
            super(null);
            this.text = text;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawCenteredTextWithShadow(ConfigList.this.client.textRenderer, this.text, x + entryWidth / 2, y + 5, 16777215);
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
                    builder.put(NarrationPart.TITLE, CategoryEntry.this.text);
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

    interface ConfigWidgetFactory<T extends ConfigOption<T>> {
        AbstractConfigWidget create(Text name, List<OrderedText> description, T option);
    }
}
