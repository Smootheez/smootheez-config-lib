package net.smootheez.scl.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.smootheez.scl.example.AnotherExampleScreen;
import net.smootheez.scl.example.ExampleScreen;


public class ModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return AnotherExampleScreen::new;
    }
}
