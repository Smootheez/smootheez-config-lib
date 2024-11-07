package dev.smootheez.scl.gui.screen;

import dev.smootheez.scl.api.ConfigProvider;
import dev.smootheez.scl.gui.widget.AutoConfigListWidget;
import dev.smootheez.scl.registry.ConfigRegister;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class AutoScreen extends Screen {
    private final Screen parent;
    private final ConfigProvider provider;
    private TextFieldWidget searchBox;
    private AutoConfigListWidget configList;

    public AutoScreen(Text title, Screen parent, ConfigProvider provider) {
        super(title);
        this.parent = parent;
        this.provider = provider;
    }

    @Override
    protected void init() {
        searchBox = new TextFieldWidget(
                this.textRenderer,
                this.width / 2 - 100,
                6,
                200,
                20,
                Text.of("Search...")
        );
        searchBox.setMaxLength(50);
        searchBox.setChangedListener(this::onSearchChanged);
        addDrawableChild(searchBox);

        configList = new AutoConfigListWidget(provider).sorted().build();
        addDrawableChild(configList);

        addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> close())
                .dimensions(this.width / 2 + 5, this.height - 27, 150, 20)
                .build());
        addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
            close();
            ConfigRegister.getInstance().save(provider.getClass());
        }).dimensions(this.width / 2 - 155, this.height - 27, 150, 20).build());
    }

    private void onSearchChanged(String searchTerm) {
        if (searchTerm.isEmpty()) {
            configList.resetView();
        } else {
            configList.search(searchTerm);
        }
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
        context.drawText(this.textRenderer, this.title, 12, 12, 0xffffff, false);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (searchBox.isFocused()) {
            return searchBox.keyPressed(keyCode, scanCode, modifiers);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (searchBox.isFocused()) {
            return searchBox.charTyped(chr, modifiers);
        }
        return super.charTyped(chr, modifiers);
    }
}