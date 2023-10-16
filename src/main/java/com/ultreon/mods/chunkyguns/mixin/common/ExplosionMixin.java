package com.ultreon.mods.chunkyguns.mixin.common;

import com.ultreon.mods.chunkyguns.util.GrenadeExplosion;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Explosion.class)
public class ExplosionMixin implements GrenadeExplosion {

    private boolean cancelSound = false;

    @Redirect(method = "affectWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V"))
    private void affectWorld(World instance, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance) {
        if (!cancelSound) {
            instance.playSound(x, y, z, sound, category, volume, pitch, useDistance);
        }
    }

    @Override
    public void cancelSound() {
        cancelSound = true;
    }
}
