package com.ultreon.mods.chunkyguns.client.handler;

import com.ultreon.mods.chunkyguns.registry.ConfigRegistry;

public class CrosshairHandler {

    private static int tick;
    private static boolean tickCrosshair;
    public static boolean renderCrosshair;

    public static void onRenderTick() {
        if (tickCrosshair) {
            tick++;
            if (tick % ConfigRegistry.hit_crosshair_ticks == 0) {
                tickCrosshair = false;
                renderCrosshair = false;
                tick = 0;
            }
        }
    }

    public static void renderCrosshair() {
        if (ConfigRegistry.hit_crosshair_ticks != 0) {
            tickCrosshair = true;
            renderCrosshair = true;
        }
    }
}
