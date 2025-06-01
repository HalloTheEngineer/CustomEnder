package de.hallotheengineer.customender.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Config {
    private static final Logger logger = LoggerFactory.getLogger("config");

    public static final File FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "customender.json");
    private static Config INSTANCE = new Config();

    public boolean active = true;
    public float spawnPercentage = 1;

    public List<String> blocks = getDefaultPlaceBlocks();


    public static void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        FILE.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(FILE)) {
            gson.toJson(INSTANCE, writer);
        } catch (IOException e) {
            logger.error("seedcracker couldn't save config", e);
        }
    }

    public static void load() {
        Gson gson = new Gson();

        if (!FILE.exists()) {
            save();
        }

        try (Reader reader = new FileReader(FILE)) {
            INSTANCE = gson.fromJson(reader, Config.class);
        } catch (Exception e) {
            logger.error("seedcracker couldn't load config, deleting it...", e);
            FILE.delete();
        }
    }

    public static Config get() {
        return INSTANCE;
    }

    private List<String> getDefaultPlaceBlocks() {
        return Stream.of(
                Blocks.DIRT,
                Blocks.GRASS_BLOCK,
                Blocks.PODZOL,
                Blocks.COARSE_DIRT,
                Blocks.MYCELIUM,
                Blocks.ROOTED_DIRT,
                Blocks.MOSS_BLOCK,
                Blocks.PALE_MOSS_BLOCK,
                Blocks.MUD,
                Blocks.MUDDY_MANGROVE_ROOTS,

                Blocks.SAND,
                Blocks.RED_SAND,
                Blocks.GRAVEL,
                //Blocks.BROWN_MUSHROOM,
                //Blocks.RED_MUSHROOM,
                Blocks.TNT,
                Blocks.CLAY,
                Blocks.PUMPKIN,
                Blocks.CARVED_PUMPKIN,
                Blocks.MELON,
                Blocks.CRIMSON_NYLIUM,
                Blocks.WARPED_NYLIUM
        ).map(block -> {
            Optional<RegistryKey<Block>> o = block.getRegistryEntry().getKey();
            return o.map(blockRegistryKey -> blockRegistryKey.getValue().toString()).orElse("minecraft:air");
        }).toList();
    }
}
