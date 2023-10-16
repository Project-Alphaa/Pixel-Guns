package com.ultreon.mods.chunkyguns.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;

public class GunEvents {

    public static final EventHandler<GunShotPre> GUN_SHOT_PRE = new EventHandler<>();
    public static final EventHandler<GunShotPost> GUN_SHOT_POST = new EventHandler<>();
    public static final EventHandler<GunHit> GUN_HIT = new EventHandler<>();

    @FunctionalInterface
    public interface GunShotPre {

        void onGunShotPre(PlayerEntity player, ItemStack stack);
    }

    @FunctionalInterface
    public interface GunShotPost {

        void onGunShotPost(PlayerEntity player, ItemStack stack);
    }

    @FunctionalInterface
    public interface GunHit {

        void onGunHit(HitResult result, ServerWorld world, ServerPlayerEntity player);
    }
}
