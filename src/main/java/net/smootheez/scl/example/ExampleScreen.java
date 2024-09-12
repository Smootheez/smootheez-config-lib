package net.smootheez.scl.example;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.smootheez.scl.gui.widget.AutoConfigListWidget;
import net.smootheez.scl.registry.ConfigRegister;

public class ExampleScreen extends Screen {
    private final Screen screen;

    protected ExampleScreen(Screen screen) {
        super(Text.of("Example Screen"));
        this.screen = screen;
    }

    @Override
    protected void init() {
        addDrawableChild(new AutoConfigListWidget(ExampleConfig.getInstance(), "exampleModId"));
        addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> close()).dimensions(this.width / 2 + 5, this.height - 27, 150, 20).build());

        addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            ConfigRegister.getInstance().save(ExampleConfig.class);
            close();
        }).dimensions(this.width / 2 - 155, this.height - 27, 150, 20).build());
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(screen);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 12, 0xFFFFFF);
    }
}
