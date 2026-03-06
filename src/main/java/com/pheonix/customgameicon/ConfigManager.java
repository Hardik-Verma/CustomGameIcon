package com.pheonix.customgameicon;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pheonix.customgameicon.config.ModConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {

    public static ModConfig CONFIG = new ModConfig();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void load() {

        try {

            Path folder = FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("CustomGameIcon");

            Path file = folder.resolve("config.json");

            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }

            if (!Files.exists(file)) {

                try (Writer writer = Files.newBufferedWriter(file)) {
                    GSON.toJson(CONFIG, writer);
                }

                return;
            }

            try (Reader reader = Files.newBufferedReader(file)) {
                CONFIG = GSON.fromJson(reader, ModConfig.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}