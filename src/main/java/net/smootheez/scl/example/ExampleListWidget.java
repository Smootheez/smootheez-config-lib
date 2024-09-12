package net.smootheez.scl.example;

import net.minecraft.client.MinecraftClient;
import net.smootheez.scl.gui.widget.ConfigListWidget;

public class ExampleListWidget extends ConfigListWidget {
    public ExampleListWidget(MinecraftClient client) {
        super(client);

        var configOption = ExampleConfig.getInstance();

        addEntry(createWidget(configOption.getExampleInteger()));
        addEntry(createWidget(configOption.getExampleStringList()));
        addEntry(createWidget(configOption.getExampleDouble()));
    }
}
