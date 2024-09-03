package net.smootheez.scl.example;

import net.smootheez.scl.annotation.Config;
import net.smootheez.scl.api.ConfigFileProvider;
import net.smootheez.scl.option.ConfigOption;

import java.util.List;


//TODO create the ExampleConfig
@Config(configName = "exampleConfig")
public class ExampleConfig implements ConfigFileProvider {
    @Config.Category(category = ExampleCategory.CATEGORY_ONE)
    private final ConfigOption<Boolean> exampleBoolean = ConfigOption.ofBoolean("exampleBoolean", true);

    @Config.Category(category = ExampleCategory.CATEGORY_ONE)
    private final ConfigOption<Boolean> exampleBoolean1 = ConfigOption.ofBoolean("exampleBoolean1", false);

    private final ConfigOption<Boolean> exampleBoolean2 = ConfigOption.ofBoolean("exampleBoolean2", false);
    private final ConfigOption<ExampleEnum> exampleEnum = ConfigOption.ofEnum("exampleEnum", ExampleEnum.ONE);


    @Config.Category(category = ExampleCategory.CATEGORY_TWO)
    private final ConfigOption<Integer> exampleInteger = ConfigOption.ofInteger("exampleInteger", 0);

    @Config.Category(category = ExampleCategory.CATEGORY_THREE)
    private final ConfigOption<List<String>> exampleStringList = ConfigOption.ofStringList("exampleStringList", List.of("exampleString", "exampleString2"));

    @Config.Category(category = ExampleCategory.CATEGORY_FOUR)
    private final ConfigOption<Float> exampleFloat = ConfigOption.ofFloat("exampleFloat", 0.0f);

    public ConfigOption<Integer> getExampleInteger() {
        return exampleInteger;
    }

    public ConfigOption<ExampleEnum> getExampleEnum() {
        return exampleEnum;
    }
    public ConfigOption<List<String>> getExampleStringList() {
        return exampleStringList;
    }

    public ConfigOption<Float> getExampleFloat() {
        return exampleFloat;
    }

    public ConfigOption<Boolean> getExampleBoolean() {
        return exampleBoolean;
    }

    public ConfigOption<Boolean> getExampleBoolean1() {
        return exampleBoolean1;
    }

    public ConfigOption<Boolean> getExampleBoolean2() {
        return exampleBoolean2;
    }
}
