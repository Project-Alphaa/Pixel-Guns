package com.ultreon.mods.chunkyguns.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class ItemGroupRegistry {

    public static final ItemGroup MISC = FabricItemGroup.builder(com.ultreon.mods.chunkyguns.ChunkyGuns.id("misc"))
        .displayName(Text.translatable("itemGroup.chunky_guns.misc"))
        .icon(() -> new ItemStack(ItemRegistry.ARMORED_VEST))
        .build();

    public static final ItemGroup WEAPONS = FabricItemGroup.builder(com.ultreon.mods.chunkyguns.ChunkyGuns.id("guns"))
        .displayName(Text.translatable("itemGroup.chunky_guns.guns"))
        .icon(() -> new ItemStack(ItemRegistry.REVOLVER))
        .build();

    public static void registerItemGroups() {
        ItemGroupEvents.modifyEntriesEvent(WEAPONS).register(entries -> {
            entries.add(ItemRegistry.PISTOL);
            entries.add(ItemRegistry.REVOLVER);
            entries.add(ItemRegistry.COMBAT_SHOTGUN);
            entries.add(ItemRegistry.SUBMACHINE_GUN);
            entries.add(ItemRegistry.LIGHT_ASSAULT_RIFLE);
            entries.add(ItemRegistry.ASSAULT_RIFLE);
            entries.add(ItemRegistry.SNIPER_RIFLE);
            entries.add(ItemRegistry.ROCKET_LAUNCHER);
            entries.add(ItemRegistry.INFINITY_GUN);

            entries.add(ItemRegistry.SHOTGUN_SHELL);
            entries.add(ItemRegistry.LIGHT_BULLETS);
            entries.add(ItemRegistry.MEDIUM_BULLETS);
            entries.add(ItemRegistry.HEAVY_BULLETS);
            entries.add(ItemRegistry.ROCKET);
            entries.add(ItemRegistry.ENERGY_BATTERY);

            entries.add(ItemRegistry.GRENADE);
            ItemStack katana = new ItemStack(ItemRegistry.KATANA);
            katana.addEnchantment(Enchantments.SWEEPING, 3);
            entries.add(katana);
            entries.add(ItemRegistry.CROWBAR);
        });

        ItemGroupEvents.modifyEntriesEvent(MISC).register(entries -> {
            entries.add(ItemRegistry.SPECIALISED_GRIP);
            entries.add(ItemRegistry.HEAVY_STOCK);
            entries.add(ItemRegistry.SHORT_SCOPE);
            entries.add(ItemRegistry.MEDIUM_SCOPE);
            entries.add(ItemRegistry.LONG_SCOPE);

            entries.add(ItemRegistry.WORKSHOP);
            entries.add(ItemRegistry.LIME_BOTTLE);
            entries.add(ItemRegistry.LEMON_BOTTLE);
            entries.add(ItemRegistry.ORANGE_BOTTLE);
            entries.add(ItemRegistry.DUMMY);

            entries.add(ItemRegistry.GAS_MASK);
            entries.add(ItemRegistry.ARMORED_VEST);
            entries.add(ItemRegistry.GREEN_ARMORED_VEST);
            entries.add(ItemRegistry.POLICE_SHIELD);
        });
    }
}
