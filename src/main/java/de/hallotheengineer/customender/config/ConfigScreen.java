package de.hallotheengineer.customender.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.Identifier;

public class ConfigScreen {
    private static final Config config = Config.get();


    public Screen getConfigScreenByCloth(Screen parent) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("title"))
                .setDefaultBackgroundTexture(Identifier.parse("minecraft:textures/block/blackstone.png"))
                .setTransparentBackground(true);
        ConfigEntryBuilder eb = builder.entryBuilder();

        //=============================CONFIG========================
        ConfigCategory settings = builder.getOrCreateCategory(Component.translatable("settings"));

        settings.addEntry(eb.startBooleanToggle(Component.translatable("settings.active"), config.active)
                .setSaveConsumer(val -> config.active = val)
                .build());
        settings.addEntry(eb.startFloatField(Component.translatable("settings.spawnPercentage"), config.spawnPercentage)
                .setSaveConsumer(val -> config.spawnPercentage = val)
                .build());

        settings.addEntry(eb.startTextDescription(Component.translatable("settings.openConfig")
                .withStyle(style -> style.withClickEvent(new ClickEvent.OpenFile(Config.FILE)).withHoverEvent(new HoverEvent.ShowText(Component.translatable("settings.tooltip.openConfig"))).withColor(ChatFormatting.YELLOW)))
                .build());

        builder.setSavingRunnable(Config::save);

        return builder.build();
    }
}
