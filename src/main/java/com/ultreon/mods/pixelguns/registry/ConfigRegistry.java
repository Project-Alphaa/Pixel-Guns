package com.ultreon.mods.pixelguns.registry;

import com.ultreon.mods.pixelguns.PixelGuns;
import eu.midnightdust.lib.config.MidnightConfig;

public class ConfigRegistry extends MidnightConfig {

    @Entry
    public static boolean enable_recoil = true;

    @Entry(min = 0, max = 1)
    public static double ads_sensitivity = 0.5F;

    @Entry(min = 0, max = 100)
    public static int hit_crosshair_ticks = 10;

    public static void registerConfig() {
        init(PixelGuns.MOD_ID, ConfigRegistry.class);
    }
}
