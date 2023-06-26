package com.ultreon.mods.pixelguns.mixin.client.gun;

import com.ultreon.mods.pixelguns.item.gun.GunItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class AnimationPrevention {

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    // Prevent attack animation when holding gun
    @Inject(method = "doAttack", at = @At("HEAD"), cancellable = true)
    private void cancelGunAttack(CallbackInfoReturnable<Boolean> info) {
        if (player.getMainHandStack().getItem() instanceof GunItem) {
            info.setReturnValue(false);
        }
    }

    // Prevent mining animation when holding gun
    @Inject(method = "handleBlockBreaking", at = @At("HEAD"), cancellable = true)
    private void handleBlockBreaking(boolean b, CallbackInfo info) {
        if (player.getMainHandStack().getItem() instanceof GunItem) {
            info.cancel();
        }
    }
}