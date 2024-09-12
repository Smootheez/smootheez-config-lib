package net.smootheez.scl.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.smootheez.scl.option.ConfigOption;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CycleConfigWidget <T extends Enum<T>> extends NamedConfigWidget {
    private final CyclingButtonWidget<T> cycleButton;

    public CycleConfigWidget(Text name, @Nullable List<OrderedText> description, ConfigOption<T> option) {
        super(name, description);
        T[] enumValues = option.getType().getEnumConstants();
        cycleButton = CyclingButtonWidget.<T>builder(e -> Text.of(e.name()))
                .omitKeyText()
                .values(enumValues)
                .initially(option.getValue())
                .build(10, 5, 74, 20, name, (button, value) -> option.setValue(value));
        this.children.add(this.cycleButton);
    }

    @Override
    public List<CyclingButtonWidget<T>> selectableChildren() {
        return List.of(this.cycleButton);
    }

    @Override
    public List<CyclingButtonWidget<T>> children() {
        return List.of(this.cycleButton);
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        this.drawName(context, y, x);
        this.cycleButton.setX(x + entryWidth - 84);
        this.cycleButton.setY(y);
        this.cycleButton.render(context, mouseX, mouseY, tickDelta);
    }
}
