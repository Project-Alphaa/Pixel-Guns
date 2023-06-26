package com.ultreon.mods.pixelguns.item.gun.variant;

import com.ultreon.mods.pixelguns.item.gun.GunItem;
import com.ultreon.mods.pixelguns.registry.ItemRegistry;
import com.ultreon.mods.pixelguns.registry.SoundRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class SubmachineGunItem extends GunItem {

    public SubmachineGunItem() {
        super(
                true,
                9.0f,
                128,
                2,
                30,
                ItemRegistry.LIGHT_BULLETS,
                44,
                0.0f,
                7.5f,
                1,
                LoadingType.CLIP,
                SoundRegistry.SUBMACHINE_GUN_RELOAD,
                SoundRegistry.SUBMACHINE_GUN_FIRE,
                1,
                false,
                new int[]{5, 17, 30}
        );
    }
}
