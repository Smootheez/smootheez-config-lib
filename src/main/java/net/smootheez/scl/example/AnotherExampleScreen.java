package net.smootheez.scl.example;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.smootheez.scl.gui.widget.AutoConfigListWidget;
import net.smootheez.scl.registry.ConfigRegister;


public class AnotherExampleScreen extends Screen {
    private final Screen parent;

    public AnotherExampleScreen(Screen parent) {
        super(Text.of("Configuration Screen"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        addDrawableChild(new AutoConfigListWidget(this.client, ExampleConfig.getInstance()));

        addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> close()).dimensions(this.width / 2 + 5, this.height - 27, 150, 20).build());

        addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            ConfigRegister.getInstance().save(ExampleConfig.class);
            close();
        }).dimensions(this.width / 2 - 155, this.height - 27, 150, 20).build());
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
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
    }
}
