package com.ultreon.mods.pixelguns.mixin.common;

import com.ultreon.mods.pixelguns.util.LivingEntityAccessor;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements LivingEntityAccessor {

    private boolean isAffectedBySplashPotions;

    @Inject(method = "isAffectedBySplashPotions", at = @At("HEAD"), cancellable = true)
    private void isAffectedBySplashPotions(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(isAffectedBySplashPotions);
    }

    @Override
    public void isAffectedBySplashPotions(boolean isAffectedBySplashPotions) {
        this.isAffectedBySplashPotions = isAffectedBySplashPotions;
    }
}
