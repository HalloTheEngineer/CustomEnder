package de.hallotheengineer.customender.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuEntry implements ModMenuApi {
    ConfigScreen configscreen = new ConfigScreen();

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return configscreen::getConfigScreenByCloth;
    }
}
