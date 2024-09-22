package dev.smootheez.scl.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.text.Text;

import java.util.List;

public class ConfigCategoryWidget extends ConfigListWidget.AbstractConfigWidget {
    final Text name;
    final MinecraftClient client = MinecraftClient.getInstance();
    protected ConfigCategoryWidget(Text name) {
        super(null);
        this.name = name;
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
        context.drawCenteredTextWithShadow(this.client.textRenderer, this.name, x + entryWidth / 2, y + 5, 16777215);
    }
}
