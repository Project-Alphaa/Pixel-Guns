package com.ultreon.mods.pixelguns.item.ammo.variant;

import com.ultreon.mods.pixelguns.item.ammo.BulletItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemStack;

public class EnergyBatteryItem extends BulletItem {

    public EnergyBatteryItem() {
        super(new FabricItemSettings().maxCount(12));
    }

    @Override
    public boolean hasGlint(ItemStack itemStack) {
        return true;
    }
}
