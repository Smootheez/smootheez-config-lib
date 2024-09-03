package net.smootheez.scl.example;

import net.minecraft.client.option.SimpleOption;
import net.smootheez.scl.annotation.Config;
import net.smootheez.scl.api.ConfigFileProvider;


//TODO create the ExampleConfig
@Config(configName = "exampleConfig")
public class ExampleConfig implements ConfigFileProvider {
    @Config.Category(category = ExampleCategory.CATEGORY_ONE)
    private final SimpleOption<Boolean> exampleBoolean = SimpleOption.ofBoolean("exampleBoolean", true);
    @Config.Category(category = ExampleCategory.CATEGORY_ONE)
    private final SimpleOption<Boolean> exampleBoolean1 = SimpleOption.ofBoolean("exampleBoolean1", false);
}
