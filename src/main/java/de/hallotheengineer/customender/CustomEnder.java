package de.hallotheengineer.customender;

import de.hallotheengineer.customender.config.Config;
import net.fabricmc.api.ModInitializer;

public class CustomEnder implements ModInitializer {

    @Override
    public void onInitialize() {
        Config.load();
    }
}
