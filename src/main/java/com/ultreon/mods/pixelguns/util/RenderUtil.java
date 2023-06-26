package com.ultreon.mods.pixelguns.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class RenderUtil {

    public static void scissor(int x, int y, int width, int height) {
        MinecraftClient mc = MinecraftClient.getInstance();
        int scale = (int) mc.getWindow().getScaleFactor();
        GL11.glScissor(x * scale, mc.getWindow().getHeight() - y * scale - height * scale, Math.max(0, width * scale), Math.max(0, height * scale));
    }

    public static BakedModel getModel(ItemStack item) {
        return MinecraftClient.getInstance().getItemRenderer().getModels().getModel(item);
    }

    public static boolean isMouseWithin(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}