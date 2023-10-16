package com.ultreon.mods.chunkyguns.item.gun.variant;

import com.ultreon.mods.chunkyguns.item.gun.GunItem;
import com.ultreon.mods.chunkyguns.registry.ItemRegistry;
import com.ultreon.mods.chunkyguns.registry.SoundRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class RevolverItem extends GunItem {

    public RevolverItem() {
        super(
                false,
                11.0f,
                128,
                10,
                6,
                ItemRegistry.MEDIUM_BULLETS,
                40,
                0.125f,
                10.0f,
                1,
                LoadingType.CLIP,
                SoundRegistry.REVOLVER_RELOAD,
                SoundRegistry.REVOLVER_FIRE,
                1,
                false,
                new int[]{1, 26, 34}
        );
    }
}
