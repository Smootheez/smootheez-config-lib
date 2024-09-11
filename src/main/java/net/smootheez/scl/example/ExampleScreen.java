package net.smootheez.scl.example;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.smootheez.scl.gui.widget.ConfigListWidget;
import net.smootheez.scl.registry.ConfigRegister;


public class ExampleScreen extends Screen {
    private final Screen parent;
    private final TabManager tabManager = new TabManager(this::addDrawableChild, this::remove);
    private TabNavigationWidget tabNavigation;
    private GridWidget grid;
    public ConfigListWidget configList;

    public ExampleScreen(Screen parent) {
        super(Text.of("Configuration Screen"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        tabNavigation = TabNavigationWidget.builder(this.tabManager, this.width)
                .tabs(new ConfigTabOne(), new ConfigTabTwo(), new ConfigTabThree(), new ConfigTabFour())
                .build();
        addDrawableChild(tabNavigation);

        grid = new GridWidget().setColumnSpacing(10);
        GridWidget.Adder adder = grid.createAdder(2);
        adder.add(ButtonWidget.builder(ScreenTexts.DONE, button -> {
                    ConfigRegister.getInstance().save(ExampleConfig.class);
                    close();
                }).build());
        adder.add(ButtonWidget.builder(ScreenTexts.CANCEL, button -> close()).build());
        grid.forEachChild(child -> {
            child.setNavigationOrder(1);
            addDrawableChild(child);
        });

        tabNavigation.selectTab(0, false);
        initTabNavigation();
    }

    @Override
    protected void initTabNavigation() {
        if (tabNavigation != null && grid != null) {
            tabNavigation.setWidth(this.width);
            tabNavigation.init();
            grid.refreshPositions();
            SimplePositioningWidget.setPos(grid, 0, this.height - 36, this.width, 36);
            int i = tabNavigation.getNavigationFocus().getBottom();
            ScreenRect screenRect = new ScreenRect(0, i, this.width, this.grid.getY() - i);
            tabManager.setTabArea(screenRect);
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
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
    }

    class ConfigTabOne extends GridScreenTab {

        public ConfigTabOne() {
            super(Text.of("Tab 1"));
            GridWidget.Adder adder = this.grid.createAdder(1);
            adder.add(configList = new ConfigListWidget(ExampleScreen.this.client, ExampleScreen.this.width, ExampleScreen.this.height - 64, 32, 25, ExampleScreen.this));
        }

    }

    class ConfigTabTwo extends GridScreenTab {

        public ConfigTabTwo() {
            super(Text.of("Tab 2"));
            GridWidget.Adder adder = this.grid.createAdder(1);
            adder.add(configList = new ConfigListWidget(ExampleScreen.this.client, ExampleScreen.this.width, ExampleScreen.this.height - 64, 32, 25, ExampleScreen.this));
        }
    }

    static class ConfigTabThree extends GridScreenTab {

        public ConfigTabThree() {
            super(Text.of("Tab 3"));
        }
    }

    static class ConfigTabFour extends GridScreenTab {

        public ConfigTabFour() {
            super(Text.of("Tab 4"));
        }
    }
}
