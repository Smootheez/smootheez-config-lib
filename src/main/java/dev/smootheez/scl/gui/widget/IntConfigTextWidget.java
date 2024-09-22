package dev.smootheez.scl.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import dev.smootheez.scl.option.ConfigOption;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IntConfigTextWidget extends NamedConfigWidget {
    private final TextFieldWidget textField;

    public IntConfigTextWidget(Text name, @Nullable List<OrderedText> description, ConfigOption<Integer> option) {
        super(name, description);
        final MinecraftClient client = MinecraftClient.getInstance();
        textField = new TextFieldWidget(client.textRenderer, 10, 5, 74, 20, name);
        textField.setText(Integer.toString(option.getValue()));
        textField.setChangedListener(value -> {
            if (option.validateIntValue(value)
                    && Integer.parseInt(value) <= option.getMinValue()
                    && Integer.parseInt(value) <= option.getMaxValue()) {
                textField.setEditableColor(14737632);
                option.setValue(Integer.valueOf(value));
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
