package dev.smootheez.scl.gui.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import dev.smootheez.scl.option.ConfigOption;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BooleanConfigWidget extends NamedConfigWidget {
    private final CyclingButtonWidget<Boolean> toggleButton;
    private final ButtonWidget resetButton;
    private final ConfigOption<Boolean> option;

    public BooleanConfigWidget(Text name, @Nullable List<OrderedText> description, ConfigOption<Boolean> option) {
        super(name, description);
        this.option = option;

        this.toggleButton = CyclingButtonWidget.onOffBuilder(option.getValue())
                .omitKeyText()
                .build(10, 5, 74, 20, name, (button, value) -> {
                    option.setValue(value);
                    updateResetButtonState();
                });

        resetButton = ButtonWidget.builder(Text.of("⭮"), button -> resetValue())
                .dimensions(0, 0, 20, 20)
                .build();

        this.children.add(this.toggleButton);
        this.children.add(this.resetButton);
        updateResetButtonState();
    }

    private void resetValue() {
        boolean defaultValue = option.getDefaultValue();
        option.setValue(defaultValue);
        toggleButton.setValue(defaultValue);
        updateResetButtonState();
    }

    private void updateResetButtonState() {
        resetButton.active = option.getValue() != option.getDefaultValue();
    }

    @Override
    public List<? extends Selectable> selectableChildren() {
        return List.of(this.toggleButton);
    }

    @Override
    public List<? extends Element> children() {
        return List.of(this.toggleButton, this.resetButton);
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        this.drawName(context, y, x);
        this.toggleButton.setX(x + entryWidth - 96);
        this.toggleButton.setY(y);
        this.toggleButton.render(context, mouseX, mouseY, tickDelta);

        this.resetButton.setX(x + entryWidth - 20);
        this.resetButton.setY(y);
        this.resetButton.render(context, mouseX, mouseY, tickDelta);
    }
}