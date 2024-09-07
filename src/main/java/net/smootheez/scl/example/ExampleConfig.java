package net.smootheez.scl.example;

import net.smootheez.scl.annotation.Config;
import net.smootheez.scl.api.ConfigProvider;
import net.smootheez.scl.option.ConfigOption;
import net.smootheez.scl.option.ConfigOptionList;


@Config("exampleConfig")
public class ExampleConfig implements ConfigProvider {
    private static final ExampleConfig INSTANCE = new ExampleConfig();

    @Config.Category(ExampleCategory.CATEGORY_ONE)
    private final ConfigOption<Boolean> exampleBoolean = ConfigOption.create("exampleBoolean",true);

    private final ConfigOption<Boolean> exampleBoolean1 = ConfigOption.create("exampleBoolean1", true);

    private final ConfigOption<Boolean> exampleBoolean2 = ConfigOption.create("exampleBoolean2", false);
    @Config.Category(ExampleCategory.CATEGORY_TWO)
    private final ConfigOption<ExampleEnum> exampleEnum = ConfigOption.create("exampleEnum", ExampleEnum.ONE);

    @Config.Category(ExampleCategory.CATEGORY_THREE)
    private final ConfigOption<Integer> exampleInteger = ConfigOption.create("exampleInteger", 0, 100);

    @Config.Category(ExampleCategory.CATEGORY_FOUR)
    private final ConfigOption<ConfigOptionList> exampleStringList = ConfigOption.create("exampleStringList", "exampleString", "exampleString2");

    @Config.Category(ExampleCategory.CATEGORY_FIVE)
    private final ConfigOption<Double> exampleDouble = ConfigOption.create("exampleDouble", 0.0);

    public static ExampleConfig getInstance() {
        return INSTANCE;
    }

    public ConfigOption<Integer> getExampleInteger() {
        return exampleInteger;
    }

    public ConfigOption<ExampleEnum> getExampleEnum() {
        return exampleEnum;
    }
    public ConfigOption<ConfigOptionList> getExampleStringList() {
        return exampleStringList;
    }

    public ConfigOption<Double> getExampleDouble() {
        return exampleDouble;
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
