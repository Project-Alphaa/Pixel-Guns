package com.ultreon.mods.chunkyguns.item.gun.variant;

import com.ultreon.mods.chunkyguns.item.gun.GunItem;
import com.ultreon.mods.chunkyguns.registry.ItemRegistry;
import com.ultreon.mods.chunkyguns.registry.SoundRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class PistolItem extends GunItem {

    public PistolItem() {
        super(
                false,
                5.0f,
                128,
                4,
                12,
                ItemRegistry.LIGHT_BULLETS,
                26,
                0.25f,
                10.0f,
                1,
                LoadingType.CLIP,
                SoundRegistry.PISTOL_RELOAD,
                SoundRegistry.PISTOL_FIRE,
                1,
                false,
                new int[]{6, 16, 20}
        );
    }
}
