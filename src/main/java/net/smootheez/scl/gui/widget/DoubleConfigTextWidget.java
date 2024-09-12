package net.smootheez.scl.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.smootheez.scl.option.ConfigOption;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DoubleConfigTextWidget extends NamedConfigWidget {
    private final TextFieldWidget textField;
    public DoubleConfigTextWidget(Text name, @Nullable List<OrderedText> description, ConfigOption<Double> option) {
        super(name, description);
        final MinecraftClient client = MinecraftClient.getInstance();
        textField = new TextFieldWidget(client.textRenderer, 10, 5, 74, 20, name);
        textField.setText(Double.toString(option.getValue()));
        textField.setChangedListener(value -> {
            if (option.validateDoubleValue(value)
                    && Double.parseDouble(value) <= option.getMinValue()
                    && Double.parseDouble(value) <= option.getMaxValue()) {
                textField.setEditableColor(14737632);
                option.setValue(Double.valueOf(value));
            } else {
                textField.setEditableColor(16711680);
            }
        });
        this.children.add(textField);
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        this.drawName(context, y, x);
        this.textField.setX(x + entryWidth - 84);
        this.textField.setY(y);
        this.textField.render(context, mouseX, mouseY, tickDelta);
    }
}
