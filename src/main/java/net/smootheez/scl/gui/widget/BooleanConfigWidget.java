package net.smootheez.scl.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.smootheez.scl.option.ConfigOption;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BooleanConfigWidget extends NamedConfigWidget{
    private final CyclingButtonWidget<Boolean> toggleButton;

    public BooleanConfigWidget(Text name, @Nullable List<OrderedText> description, ConfigOption<Boolean> option) {
        super(name, description);
        this.toggleButton = CyclingButtonWidget.onOffBuilder(option.getValue())
                .omitKeyText()
                .build(10, 5, 74, 20, name, (button, value) -> option.setValue(value));
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
        this.toggleButton.setX(x + entryWidth - 84);
        this.toggleButton.setY(y);
        this.toggleButton.render(context, mouseX, mouseY, tickDelta);
    }
}
