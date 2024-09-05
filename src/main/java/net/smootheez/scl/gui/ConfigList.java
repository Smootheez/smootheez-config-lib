package net.smootheez.scl.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.smootheez.scl.Scl;
import net.smootheez.scl.example.ExampleConfig;
import net.smootheez.scl.option.ConfigOption;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConfigList extends ElementListWidget<ConfigList.Entry> {
    public ConfigList(MinecraftClient client, int i, int j, int k, int l) {
        super(client, i, j, k, l);
        for (int m = 0; m < 100; ++m) {
            addEntry(new CategoryEntry(Text.of("Example List " + (m + 1))));
        }
        addEntry(new BooleanConfigWidget(ExampleConfig.getInstance().getExampleBoolean()));
        addEntry(new BooleanConfigWidget(ExampleConfig.getInstance().getExampleBoolean1()));
    }

    @Override
    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + 40;
    }

    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 80;
    }

    public static class BooleanConfigWidget extends Entry {
        private final CyclingButtonWidget<Boolean> toggleButton;

        public BooleanConfigWidget(ConfigOption<Boolean> option) {
            this.toggleButton = CyclingButtonWidget.onOffBuilder(option.getValue())
                    .build(0, 0, 300, 20, Text.of("Example Button"), (button, value) -> option.setValue(value));
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
            this.toggleButton.setX(x);
            this.toggleButton.setY(y);
            this.toggleButton.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return toggleButton.mouseClicked(mouseX, mouseY, button);
        }
    }

    public class CategoryEntry extends Entry {
        private final Text text;
        private final int textWidth;

        public CategoryEntry(Text text) {
            this.text = text;
            this.textWidth = ConfigList.this.client.textRenderer.getWidth(this.text);
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
            context.drawText(ConfigList.this.client.textRenderer, this.text,
                    ConfigList.this.client.currentScreen.width / 2 - this.textWidth / 2,
                    y + entryHeight - 9 - 1,
                    16777215,
                    false);
        }
    }

    public abstract static class Entry extends ElementListWidget.Entry<Entry> {
    }
}
