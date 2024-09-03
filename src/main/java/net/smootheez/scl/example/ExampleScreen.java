package net.smootheez.scl.example;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;



public class ExampleScreen extends Screen {
    private final Screen parent;

    public ExampleScreen(Screen parent) {
        super(Text.of("Configuration Screen"));
        this.parent = parent;
    }

    @Override
    protected void init() {

        addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> close()).dimensions(width / 2 - 100, height - 30, 200, 20).build());
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 16777215);
    }
}
