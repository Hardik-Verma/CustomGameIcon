package com.pheonix.customgameicon;

import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageResize;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.system.MemoryUtil.memAlloc;

public class IconLoader {

    private static final int[] SIZES = {16, 32, 48, 64};

    public static GLFWImage.Buffer loadAll(Path path) {

        try (MemoryStack stack = MemoryStack.stackPush()) {

            ByteBuffer imageBuffer = ByteBuffer.wrap(Files.readAllBytes(path));

            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            ByteBuffer image = STBImage.stbi_load_from_memory(imageBuffer, w, h, channels, 4);

            if (image == null) {
                throw new RuntimeException("Failed to load icon");
            }

            int width = w.get();
            int height = h.get();

            GLFWImage.Buffer buffer = GLFWImage.malloc(SIZES.length);

            for (int size : SIZES) {

                ByteBuffer resized = memAlloc(size * size * 4);

                STBImageResize.stbir_resize_uint8(
                        image,
                        width,
                        height,
                        0,
                        resized,
                        size,
                        size,
                        0,
                        4
                );

                GLFWImage icon = GLFWImage.malloc();
                icon.set(size, size, resized);

                buffer.put(icon);
            }

            buffer.flip();
            return buffer;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}