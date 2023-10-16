package com.ultreon.mods.chunkyguns.item.gun.variant;

import com.ultreon.mods.chunkyguns.item.gun.GunItem;
import com.ultreon.mods.chunkyguns.registry.ItemRegistry;
import com.ultreon.mods.chunkyguns.registry.SoundRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class AssaultRifleItem extends GunItem {

    public AssaultRifleItem() {
        super(
                true,
                8.0f,
                128,
                3,
                50,
                ItemRegistry.MEDIUM_BULLETS,
                48,
                0.125f,
                3.0f,
                1,
                LoadingType.CLIP,
                SoundRegistry.ASSAULT_RIFLE_RELOAD,
                SoundRegistry.ASSAULT_RIFLE_FIRE,
                1,
                false,
                new int[]{3, 12, 18}
        );
    }
}
