package net.smootheez.scl.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.smootheez.scl.file.ConfigFileWriter;
import net.smootheez.scl.option.ConfigOption;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ConfigOptionList extends ElementListWidget<ConfigOptionList.Entry> {
    public ConfigOptionList(MinecraftClient minecraftClient, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
        final Map<String, Map<ConfigOption<?>, AbstractConfigWidget>> map = Maps.newHashMap();
    }

    public class BooleanConfigWidget extends NamedConfigWidget {
        private final CyclingButtonWidget<Boolean> toggleButton;
        protected BooleanConfigWidget(Text name, List<OrderedText> description, String configName, ConfigOption<Boolean> option) {
            super(description, name);
            this.toggleButton = CyclingButtonWidget.onOffBuilder(option.getValue())
                    .omitKeyText()
                    .build(10,5,44,20, name, (button, value) -> option.setValue(value));
            this.children.add(this.toggleButton);
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.drawName(context, y, x);
            this.toggleButton.setX(x + entryWidth - 45);
            this.toggleButton.setY(y);
            this.toggleButton.render(context, mouseX, mouseY, tickDelta);
        }
    }

    public static class ConfigCategoryWidget extends AbstractConfigWidget {
        final Text name;

        protected ConfigCategoryWidget(Text text) {
            super(null);
            this.name = text;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of();
        }

        @Override
        public List<? extends Element> children() {
            return List.of();
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {

        }
    }

    public abstract class NamedConfigWidget extends AbstractConfigWidget {
        private final List<OrderedText> name;
        protected final List<ClickableWidget> children = Lists.newArrayList();
        protected NamedConfigWidget(@Nullable List<OrderedText> description, Text name) {
            super(description);
            this.name = ConfigOptionList.this.client.textRenderer.wrapLines(name, 175);
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
                context.drawText(ConfigOptionList.this.client.textRenderer, this.name.get(0), y, x + 5, 16777215, false);
            } else if (this.name.size() >= 2) {
                context.drawText(ConfigOptionList.this.client.textRenderer, this.name.get(0), y, x, 16777215, false);
                context.drawText(ConfigOptionList.this.client.textRenderer, this.name.get(1), y, x + 10, 16777215, false);
            }
        }
    }

    public static class Entry extends ElementListWidget.Entry<Entry> {

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of();
        }

        @Override
        public List<? extends Element> children() {
            return List.of();
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {

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
        AbstractConfigWidget create(Text name, List<OrderedText> description, String configName, T config);
    }
}
