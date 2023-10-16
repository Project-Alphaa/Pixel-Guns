package com.ultreon.mods.chunkyguns.mixin.client;

import com.ultreon.mods.chunkyguns.registry.ItemRegistry;
import com.ultreon.mods.chunkyguns.registry.SoundRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class CustomSwordSounds {

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @Shadow
    @Nullable
    public ClientWorld world;

    @Inject(method = "doAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasLimitedAttackSpeed()Z", shift = Shift.BEFORE))
    private void onAttackMiss(CallbackInfoReturnable<Boolean> info) {
        assert player != null;
        assert world != null;

        Item heldItem = player.getMainHandStack().getItem();

        if (heldItem == ItemRegistry.KATANA) {
            world.playSound(this.player.getX(), this.player.getY(), this.player.getZ(), SoundRegistry.KATANA_SWING, SoundCategory.PLAYERS, 1, 1, false);
        }
        else if (heldItem == ItemRegistry.CROWBAR) {
            world.playSound(this.player.getX(), this.player.getY(), this.player.getZ(), SoundRegistry.CROWBAR_HIT, SoundCategory.PLAYERS, 1, 1, false);
        }
    }
}