package dev.smootheez.scl.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import dev.smootheez.scl.option.ConfigOption;
import dev.smootheez.scl.option.ConfigOptionList;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TextConfigWidget extends NamedConfigWidget {
    private final TextFieldWidget textField;

    public TextConfigWidget(Text name, @Nullable List<OrderedText> description, ConfigOption<ConfigOptionList> option) {
        super(name, description);
        final MinecraftClient client = MinecraftClient.getInstance();
        textField = new TextFieldWidget(client.textRenderer, 10, 5, 74, 20, name);
        textField.setMaxLength(Integer.MAX_VALUE);
        textField.setText(getConfigOptionListString(option.getValue()));
        textField.setChangedListener(value -> {
            try {
                textField.setEditableColor(14737632);
                option.setValue(stringToConfigOptionList(value));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        this.children.add(textField);
    }

    private ConfigOptionList stringToConfigOptionList(String value) {
        String cleanedValue = value.replace(" ", "").replace("\"", "");
        return new ConfigOptionList(List.of(cleanedValue.split(",")));
    }

    private String getConfigOptionListString(ConfigOptionList configOptionList) {
        StringBuilder sb = new StringBuilder();
        for (String option : configOptionList.values()) {
            if (!sb.isEmpty()) {
                sb.append(", ");
            }
            sb.append(option);
        }
        return "\"" + sb + "\"";
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        this.drawName(context, y, x);
        this.textField.setX(x +  entryWidth - 84);
        this.textField.setY(y);
        this.textField.render(context, mouseX, mouseY, tickDelta);
    }
}
