package dev.smootheez.scl.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import dev.smootheez.scl.option.ConfigOption;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IntConfigTextWidget extends NamedConfigWidget {
    private final TextFieldWidget textField;
    private final ButtonWidget resetButton;
    private final ConfigOption<Integer> option;

    public IntConfigTextWidget(Text name, @Nullable List<OrderedText> description, ConfigOption<Integer> option) {
        super(name, description);
        this.option = option;
        final MinecraftClient client = MinecraftClient.getInstance();

        textField = new TextFieldWidget(client.textRenderer, 10, 5, 74, 20, name);
        textField.setText(Integer.toString(option.getValue()));
        textField.setChangedListener(this::onTextChanged);

        resetButton = ButtonWidget.builder(Text.of("⭮"), button -> resetValue())
                .dimensions(0, 0, 20, 20)
                .build();

        this.children.add(textField);
        this.children.add(resetButton);

        updateResetButtonState();
    }

    private void onTextChanged(String value) {
        if (option.validateIntValue(value)
                && Integer.parseInt(value) >= option.getMinValue()
                && Integer.parseInt(value) <= option.getMaxValue()) {
            textField.setEditableColor(14737632);
            option.setValue(Integer.valueOf(value));
        } else {
            textField.setEditableColor(16711680);
        }
        updateResetButtonState();
    }

    private void resetValue() {
        int defaultValue = option.getDefaultValue();
        textField.setText(Integer.toString(defaultValue));
        option.setValue(defaultValue);
        textField.setEditableColor(14737632);
        updateResetButtonState();
    }

    private void updateResetButtonState() {
        int currentValue = option.getValue();
        int defaultValue = option.getDefaultValue();
        resetButton.active = currentValue != defaultValue; // Disable button if current value is default
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