package com.ultreon.mods.chunkyguns.mixin.client.gun.ads;

import com.mojang.authlib.GameProfile;
import com.ultreon.mods.chunkyguns.item.gun.GunItem;
import com.ultreon.mods.chunkyguns.registry.ItemRegistry;
import com.ultreon.mods.chunkyguns.util.ZoomablePlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AdsZoom extends PlayerEntity implements ZoomablePlayer {

    private boolean isPlayerZoomed = false;

    public AdsZoom(World world, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(world, blockPos, f, gameProfile);
    }

    // Apply camera zoom on ADS
    @Inject(method = "getFovMultiplier", at = @At(value = "TAIL"), cancellable = true)
    private void getFovMultiplier(CallbackInfoReturnable<Float> cir) {
        if (MinecraftClient.getInstance().options.useKey.isPressed()) {
            ItemStack stack = getStackInHand(Hand.MAIN_HAND);
            if (stack.getItem() instanceof GunItem && GunItem.isLoaded(stack) && !getStackInHand(Hand.OFF_HAND).isOf(ItemRegistry.POLICE_SHIELD)) {
                NbtCompound nbt = stack.getOrCreateNbt();
                if (nbt.getBoolean(GunItem.TAG_IS_SCOPED)) {
                    cir.setReturnValue(0.2f);
                }
                else {
                    cir.setReturnValue(0.8f);
                }
                isPlayerZoomed = true;
            }
        }
        else {
            isPlayerZoomed = false;
        }
    }

    @Override
    public boolean isPlayerZoomed() {
        return isPlayerZoomed;
    }
}

