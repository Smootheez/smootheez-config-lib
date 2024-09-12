package net.smootheez.scl.gui.widget;

import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class NamedConfigWidget extends ConfigListWidget.AbstractConfigWidget{
    private final List<OrderedText> name;
    protected final List<ClickableWidget> children = Lists.newArrayList();
    private final MinecraftClient client = MinecraftClient.getInstance();

    public NamedConfigWidget(Text name, @Nullable List<OrderedText> description) {
        super(description);
        this.name = this.client.textRenderer.wrapLines(name, 350);
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
            context.drawText(this.client.textRenderer, this.name.get(0), y, x + 5, 16777215, false);
        } else if (this.name.size() >= 2) {
            context.drawText(this.client.textRenderer, this.name.get(0), y, x, 16777215, false);
            context.drawText(this.client.textRenderer, this.name.get(1), y, x + 10, 16777215, false);
        }
    }
}
