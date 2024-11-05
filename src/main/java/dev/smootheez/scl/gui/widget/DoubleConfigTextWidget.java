package dev.smootheez.scl.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import dev.smootheez.scl.option.ConfigOption;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DoubleConfigTextWidget extends NamedConfigWidget {
    private final TextFieldWidget textField;
    private final ButtonWidget resetButton;
    private final ConfigOption<Double> option;

    public DoubleConfigTextWidget(Text name, @Nullable List<OrderedText> description, ConfigOption<Double> option) {
        super(name, description);
        this.option = option;
        final MinecraftClient client = MinecraftClient.getInstance();

        textField = new TextFieldWidget(client.textRenderer, 10, 5, 74, 20, name);
        textField.setText(Double.toString(option.getValue()));
        textField.setChangedListener(this::onTextChanged);

        resetButton = ButtonWidget.builder(Text.of("⭮"), button -> resetValue())
                .dimensions(0, 0, 20, 20)
                .build();

        this.children.add(textField);
        this.children.add(resetButton);

        updateResetButtonState();
    }

    private void onTextChanged(String value) {
        if (option.validateDoubleValue(value)
                && Double.parseDouble(value) >= option.getMinValue()
                && Double.parseDouble(value) <= option.getMaxValue()) {
            textField.setEditableColor(14737632);
            option.setValue(Double.valueOf(value));
        } else {
            textField.setEditableColor(16711680);
        }
        updateResetButtonState();
    }

    private void resetValue() {
        double defaultValue = option.getDefaultValue();
        textField.setText(Double.toString(defaultValue));
        option.setValue(defaultValue);
        textField.setEditableColor(14737632);
        updateResetButtonState();
    }

    private void updateResetButtonState() {
        double currentValue = option.getValue();
        double defaultValue = option.getDefaultValue();
        resetButton.active = currentValue != defaultValue;
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        this.drawName(context, y, x);
        this.textField.setX(x + entryWidth - 96);
        this.textField.setY(y);
        this.textField.render(context, mouseX, mouseY, tickDelta);

        this.resetButton.setX(x + entryWidth - 20);
        this.resetButton.setY(y);
        this.resetButton.render(context, mouseX, mouseY, tickDelta);
    }
}