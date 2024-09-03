package net.smootheez.scl.example;

import net.smootheez.scl.annotation.Config;
import net.smootheez.scl.api.ConfigFileProvider;
import net.smootheez.scl.option.ConfigOption;


//TODO create the ExampleConfig
@Config(configName = "exampleConfig")
public class ExampleConfig implements ConfigFileProvider {
    @Config.Category(category = ExampleCategory.CATEGORY_ONE)
    private final ConfigOption<Boolean> exampleBoolean = ConfigOption.ofBoolean("exampleBoolean", true);

    @Config.Category(category = ExampleCategory.CATEGORY_ONE)
    private final ConfigOption<Boolean> exampleBoolean1 = ConfigOption.ofBoolean("exampleBoolean1", false);

    private final ConfigOption<Boolean> exampleBoolean2 = ConfigOption.ofBoolean("exampleBoolean2", false);
}
