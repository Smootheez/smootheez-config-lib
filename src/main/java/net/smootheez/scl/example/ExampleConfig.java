package net.smootheez.scl.example;

import net.smootheez.scl.annotation.Config;
import net.smootheez.scl.api.ConfigProvider;
import net.smootheez.scl.option.ConfigOption;

@Config("exampleConfig")
public class ExampleConfig implements ConfigProvider {
    private static final ExampleConfig INSTANCE = new ExampleConfig();

    private final ConfigOption<Boolean> optionOption = ConfigOption.create("exampleOption", true);
    private final ConfigOption<Boolean> optionOption1 = ConfigOption.create("optionOption1", true);
    private final ConfigOption<Boolean> optionOption2 = ConfigOption.create("optionOption2", true);
    private final ConfigOption<Boolean> optionOption3 = ConfigOption.create("optionOption3", true);
    private final ConfigOption<Boolean> optionOption4 = ConfigOption.create("optionOption4", true);
    private final ConfigOption<Boolean> optionOption5 = ConfigOption.create("optionOption5", true);
    private final ConfigOption<Boolean> optionOption6 = ConfigOption.create("optionOption6", true);
    private final ConfigOption<Boolean> optionOption7 = ConfigOption.create("optionOption7", true);
    private final ConfigOption<Boolean> optionOption8 = ConfigOption.create("optionOption8", true);
    private final ConfigOption<Boolean> optionOption9 = ConfigOption.create("optionOption9", true);
    private final ConfigOption<Boolean> optionOption10 = ConfigOption.create("optionOption10", true);
    private final ConfigOption<Boolean> optionOption11 = ConfigOption.create("optionOption11", true);
    private final ConfigOption<Boolean> optionOption12 = ConfigOption.create("optionOption12", true);
    private final ConfigOption<Boolean> optionOption13 = ConfigOption.create("optionOption13", true);
    private final ConfigOption<Boolean> optionOption14 = ConfigOption.create("optionOption14", true);
    private final ConfigOption<Boolean> optionOption15 = ConfigOption.create("optionOption15", true);

    public static ExampleConfig getInstance() {
        return INSTANCE;
    }
}