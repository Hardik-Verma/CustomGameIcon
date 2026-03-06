package com.pheonix.customgameicon;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class IconFinder {

    public static List<Path> findIcons() {

        List<Path> icons = new ArrayList<>();

        try {

            Path folder = FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("CustomGameIcon");

            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }

            Files.list(folder).forEach(path -> {

                String name = path.getFileName().toString().toLowerCase();

                if (name.endsWith(".png")
                        || name.endsWith(".jpg")
                        || name.endsWith(".jpeg")) {

                    icons.add(path);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return icons;
    }
}