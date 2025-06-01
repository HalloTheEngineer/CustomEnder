package de.hallotheengineer.customender.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class ConfigScreen {
    private static final Config config = Config.get();


    public Screen getConfigScreenByCloth(Screen parent) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title"))
                .setDefaultBackgroundTexture(Identifier.of("minecraft:textures/block/blackstone.png"))
                .setTransparentBackground(true);
        ConfigEntryBuilder eb = builder.entryBuilder();

        //=============================CONFIG========================
        ConfigCategory settings = builder.getOrCreateCategory(Text.translatable("settings"));

        settings.addEntry(eb.startBooleanToggle(Text.translatable("settings.active"), config.active)
                .setSaveConsumer(val -> config.active = val)
                .build());
        settings.addEntry(eb.startFloatField(Text.translatable("settings.spawnPercentage"), config.spawnPercentage)
                .setSaveConsumer(val -> config.spawnPercentage = val)
                .build());

        settings.addEntry(eb.startTextDescription(Text.translatable("settings.openConfig")
                .styled(style -> style.withClickEvent(new ClickEvent.OpenFile(Config.FILE)).withHoverEvent(new HoverEvent.ShowText(Text.translatable("settings.tooltip.openConfig"))).withColor(Formatting.YELLOW)))
                .build());

        builder.setSavingRunnable(Config::save);

        return builder.build();
    }
}
