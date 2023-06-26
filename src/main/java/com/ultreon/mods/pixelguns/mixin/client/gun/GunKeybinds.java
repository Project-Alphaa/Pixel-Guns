package com.ultreon.mods.pixelguns.mixin.client.gun;

import com.ultreon.mods.pixelguns.item.gun.GunItem;

import com.ultreon.mods.pixelguns.registry.ItemRegistry;
import com.ultreon.mods.pixelguns.registry.KeyBindRegistry;
import com.ultreon.mods.pixelguns.registry.PacketRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class GunKeybinds {
    
    @Shadow @Nullable public ClientPlayerEntity player;
    @Shadow private int itemUseCooldown;

    // Allows the GunItem.use() to be called when holding a GunItem and using the attack keybind instead of the use keybind
    @Inject(method = "handleInputEvents", at = @At("TAIL"))
    public void handleGunShoot(CallbackInfo info) {
        assert this.player != null;
        if (this.player.getMainHandStack().getItem() instanceof GunItem gunItem) {
            if (MinecraftClient.getInstance().options.attackKey.isPressed() && this.itemUseCooldown == 0) {
                if (!gunItem.isAutomatic) {
                    MinecraftClient.getInstance().options.attackKey.setPressed(false);
                }
                this.itemUseCooldown = gunItem.fireCooldown;
                if (!this.player.isSprinting() && GunItem.isLoaded(this.player.getMainHandStack())) {
                    gunItem.shoot(this.player, this.player.getMainHandStack());
                    ClientPlayNetworking.send(PacketRegistry.GUN_SHOOT, PacketByteBufs.create());
                }
            }
        }
    }

    @Inject(method = "handleInputEvents", at = @At("TAIL"))
    public void handleGunReload(CallbackInfo info) {
        assert this.player != null;
        if (this.player.getMainHandStack().getItem() instanceof GunItem) {
            if (KeyBindRegistry.RELOAD_KEY.isPressed()) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBoolean(true);
                ClientPlayNetworking.send(PacketRegistry.GUN_RELOAD, buf);
            }
        }
    }

    // Prevents default item use cooldown when firing gun
    @Inject(method = "doItemUse", at = @At("HEAD"), cancellable = true)
    public void preventGunUseCooldown(CallbackInfo ci) {
        if (this.player == null) {
            return;
        }
        ItemStack itemStack = this.player.getMainHandStack();
        if (itemStack.getItem() instanceof GunItem && !player.getStackInHand(Hand.OFF_HAND).isOf(ItemRegistry.POLICE_SHIELD)) {
            ci.cancel();
        }
    }
}