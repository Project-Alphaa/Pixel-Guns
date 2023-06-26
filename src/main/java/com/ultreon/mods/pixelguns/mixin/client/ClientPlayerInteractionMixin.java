package com.ultreon.mods.pixelguns.mixin.client;

import com.ultreon.mods.pixelguns.registry.ItemRegistry;
import com.ultreon.mods.pixelguns.registry.SoundRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionMixin {

    @Inject(method = "attackEntity", at = @At(value = "HEAD"))
    private void onAttackMiss(PlayerEntity player, Entity entity, CallbackInfo ci) {
        World world = MinecraftClient.getInstance().world;
        assert player != null;
        if (player.getMainHandStack().getItem() == ItemRegistry.KATANA || player.getMainHandStack().getItem() == ItemRegistry.CROWBAR) {
            assert world != null;
            world.playSound(player.getX(), player.getY(), player.getZ(), SoundRegistry.KATANA_HIT, SoundCategory.PLAYERS, 1, 1, false);
        }
    }
}
