package com.ultreon.mods.pixelguns.item.gun.variant;

import com.ultreon.mods.pixelguns.item.gun.GunItem;
import com.ultreon.mods.pixelguns.registry.ItemRegistry;
import com.ultreon.mods.pixelguns.registry.SoundRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class CombatShotgunItem extends GunItem {

    public CombatShotgunItem() {
        super(
                false,
                10.0f,
                128,
                14,
                6,
                ItemRegistry.SHOTGUN_SHELL,
                26,
                9.25f,
                30.0f,
                5,
                LoadingType.INDIVIDUAL,
                SoundRegistry.COMBAT_SHOTGUN_RELOAD,
                SoundRegistry.COMBAT_SHOTGUN_FIRE,
                6,
                false,
                new int[]{1, 4, 13}
        );
    }
}
