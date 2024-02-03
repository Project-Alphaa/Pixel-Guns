package com.ultreon.mods.chunkyguns.registry;

import com.ultreon.mods.chunkyguns.item.gun.GunItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ModelPredicateRegistry {

    private static final Identifier AIMING = com.ultreon.mods.chunkyguns.ChunkyGuns.id("aiming");

    public static void registerModelPredicates() {
        registerGunPredicate(ItemRegistry.PISTOL);
        registerGunPredicate(ItemRegistry.REVOLVER);
        registerGunPredicate(ItemRegistry.SUBMACHINE_GUN);
        registerGunPredicate(ItemRegistry.LIGHT_ASSAULT_RIFLE);
        registerGunPredicate(ItemRegistry.ASSAULT_RIFLE);
        registerGunPredicate(ItemRegistry.COMBAT_SHOTGUN);
        registerGunPredicate(ItemRegistry.SNIPER_RIFLE);

        ModelPredicateProviderRegistry.register(ItemRegistry.ROCKET_LAUNCHER, AIMING, (stack, world, entity, seed) ->
                entity != null && MinecraftClient.getInstance().options.useKey.isPressed() && GunItem.isLoaded(stack) ? 1.0f : 0.0f);
        ModelPredicateProviderRegistry.register(ItemRegistry.POLICE_SHIELD, new Identifier("blocking"), (stack, world, entity, seed) ->
                entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1 : 0);
    }

    private static void registerGunPredicate(Item gun) {
        ModelPredicateProviderRegistry.register(gun, com.ultreon.mods.chunkyguns.ChunkyGuns.id("cooldown_tick"), (stack, world, entity, seed) -> {
            if (entity != null) {
                if(entity.getStackInHand(Hand.MAIN_HAND) != stack)
                    return 0;
                NbtCompound nbt = stack.getOrCreateNbt();
                if (nbt.contains(GunItem.TAG_UUID)) {
                    UUID uuid = nbt.getUuid(GunItem.TAG_UUID);
                    if (com.ultreon.mods.chunkyguns.ChunkyGunsClient.TRACKED_GUN_COOLDOWNS.containsKey(uuid)) {
                        float cooldown = com.ultreon.mods.chunkyguns.ChunkyGunsClient.TRACKED_GUN_COOLDOWNS.get(uuid);
                        if (cooldown <= 0.25f) {
                            com.ultreon.mods.chunkyguns.ChunkyGunsClient.TRACKED_GUN_COOLDOWNS.remove(uuid);
                        }
                        return cooldown;
                    }
                }
            }
            return 0;
        });

        ModelPredicateProviderRegistry.register(gun, com.ultreon.mods.chunkyguns.ChunkyGuns.id("load_tick"), (stack, world, entity, seed) -> {
            if (entity == null || !stack.getOrCreateNbt().getBoolean(GunItem.TAG_IS_RELOADING)) {
                return 0.0f;
            }
            return stack.getOrCreateNbt().getInt(GunItem.TAG_RELOAD_TICK) / 200.0f;
        });
        ModelPredicateProviderRegistry.register(gun, com.ultreon.mods.chunkyguns.ChunkyGuns.id("loading"), (stack, world, entity, seed) -> {
            if (entity == null || !stack.getOrCreateNbt().getBoolean(GunItem.TAG_IS_RELOADING)) {
                return 0.0f;
            }
            return 1.0f;
        });
        ModelPredicateProviderRegistry.register(gun, AIMING, (stack, world, entity, seed) -> {
            if(entity != null && entity.getStackInHand(Hand.MAIN_HAND) == stack)
                if (MinecraftClient.getInstance().options.useKey.isPressed() && GunItem.isLoaded(stack) && !entity.getStackInHand(Hand.OFF_HAND).isOf(ItemRegistry.POLICE_SHIELD)) {
                    return 1.0f;
                }
            return 0.0f;
        });
        ModelPredicateProviderRegistry.register(gun, com.ultreon.mods.chunkyguns.ChunkyGuns.id("sprinting"), (stack, world, entity, seed) -> {
            if (entity != null && entity.getStackInHand(Hand.MAIN_HAND) == stack && entity.isSprinting()) {
                return 1.0f;
            }
            return 0.0f;
        });
    }
}
