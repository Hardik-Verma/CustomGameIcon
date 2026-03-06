package com.pheonix.customgameicon;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import com.mojang.brigadier.arguments.StringArgumentType;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import java.io.File;
import java.net.URL;

import java.nio.ByteBuffer;

import java.util.Random;

public class CustomGameIconClient implements ClientModInitializer {

    private static final File ICON_FOLDER = new File("config/CustomGameIcon");

    private static boolean RANDOM_MODE = false;

    @Override
    public void onInitializeClient() {

        System.out.println("[CustomGameIcon] ===============================");
        System.out.println("[CustomGameIcon] Mod loaded successfully");
        System.out.println("[CustomGameIcon] Config folder: config/CustomGameIcon");
        System.out.println("[CustomGameIcon] ===============================");

        loadIcons();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {

            dispatcher.register(ClientCommandManager.literal("customgameicon")

                    .then(ClientCommandManager.literal("help").executes(ctx -> {

                        ctx.getSource().sendFeedback(Text.literal("§b§lCustomGameIcon Commands"));
                        ctx.getSource().sendFeedback(Text.literal("§7/customgameicon reload"));
                        ctx.getSource().sendFeedback(Text.literal("§7/customgameicon random on/off"));
                        ctx.getSource().sendFeedback(Text.literal("§7/customgameicon seticon <player>"));

                        ctx.getSource().sendFeedback(Text.literal("§8Examples:"));
                        ctx.getSource().sendFeedback(Text.literal("§7/customgameicon reload"));
                        ctx.getSource().sendFeedback(Text.literal("§7/customgameicon random on"));
                        ctx.getSource().sendFeedback(Text.literal("§7/customgameicon seticon Notch"));

                        return 1;
                    }))

                    .then(ClientCommandManager.literal("reload").executes(ctx -> {

                        loadIcons();

                        ctx.getSource().sendFeedback(Text.literal("§a✔ Icons reloaded"));

                        return 1;
                    }))

                    .then(ClientCommandManager.literal("random")

                            .then(ClientCommandManager.literal("on").executes(ctx -> {

                                RANDOM_MODE = true;

                                ctx.getSource().sendFeedback(Text.literal("§a✔ Random icon mode enabled"));

                                return 1;
                            }))

                            .then(ClientCommandManager.literal("off").executes(ctx -> {

                                RANDOM_MODE = false;

                                ctx.getSource().sendFeedback(Text.literal("§c✖ Random icon mode disabled"));

                                return 1;
                            }))
                    )

                    .then(ClientCommandManager.literal("seticon")
                            .then(ClientCommandManager.argument("player", StringArgumentType.string())
                                    .executes(ctx -> {

                                        String player = StringArgumentType.getString(ctx, "player");

                                        fetchPlayerIcon(player);

                                        ctx.getSource().sendFeedback(Text.literal("§a✔ Icon set to player: §f" + player));

                                        return 1;
                                    })
                            )
                    )
            );
        });
    }

    private void loadIcons() {

        try {

            if (!ICON_FOLDER.exists()) {
                ICON_FOLDER.mkdirs();
            }

            File[] icons = ICON_FOLDER.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

            if (icons == null || icons.length == 0) {

                System.out.println("[CustomGameIcon] No icons found in config/CustomGameIcon");

                return;
            }

            File selected = icons[0];

            if (RANDOM_MODE) {
                selected = icons[new Random().nextInt(icons.length)];
            }

            BufferedImage image = ImageIO.read(selected);

            applyIcon(image);

            System.out.println("[CustomGameIcon] Loaded icon: " + selected.getName());

        } catch (Exception e) {

            System.out.println("[CustomGameIcon] Failed loading icons");
            e.printStackTrace();
        }
    }

    private void fetchPlayerIcon(String player) {

        try {

            URL url = new URL("https://minotar.net/avatar/" + player + "/128.png");

            BufferedImage image = ImageIO.read(url);

            if (image == null) {

                System.out.println("[CustomGameIcon] Failed fetching player icon");

                return;
            }

            applyIcon(image);

        } catch (Exception e) {

            System.out.println("[CustomGameIcon] Error fetching player icon");

            e.printStackTrace();
        }
    }

    private BufferedImage resize(BufferedImage img, int size) {

        BufferedImage resized = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = resized.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g.drawImage(img, 0, 0, size, size, null);

        g.dispose();

        return resized;
    }

    private void applyIcon(BufferedImage image) {

        try {

            BufferedImage icon16 = resize(image, 16);
            BufferedImage icon32 = resize(image, 32);
            BufferedImage icon48 = resize(image, 48);
            BufferedImage icon64 = resize(image, 64);

            MinecraftClient.getInstance().execute(() -> {

                long window = MinecraftClient.getInstance().getWindow().getHandle();

                GLFWImage.Buffer icons = GLFWImage.malloc(4);

                icons.put(0, createIcon(icon16));
                icons.put(1, createIcon(icon32));
                icons.put(2, createIcon(icon48));
                icons.put(3, createIcon(icon64));

                GLFW.glfwSetWindowIcon(window, icons);

                System.out.println("[CustomGameIcon] Applied new icon");

            });

        } catch (Exception e) {

            System.out.println("[CustomGameIcon] Failed applying icon");

            e.printStackTrace();
        }
    }

    private GLFWImage createIcon(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        int[] pixels = new int[width * height];

        image.getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

        for (int pixel : pixels) {

            buffer.put((byte)((pixel >> 16) & 0xFF));
            buffer.put((byte)((pixel >> 8) & 0xFF));
            buffer.put((byte)(pixel & 0xFF));
            buffer.put((byte)((pixel >> 24) & 0xFF));
        }

        buffer.flip();

        GLFWImage icon = GLFWImage.malloc();

        icon.set(width, height, buffer);

        return icon;
    }
}