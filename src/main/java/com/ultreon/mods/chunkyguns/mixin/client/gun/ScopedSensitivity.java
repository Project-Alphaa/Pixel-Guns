package com.ultreon.mods.chunkyguns.mixin.client.gun;

import com.ultreon.mods.chunkyguns.item.gun.GunItem;
import com.ultreon.mods.chunkyguns.registry.ItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Mouse.class)
public class ScopedSensitivity {

    @ModifyArgs(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"))
    private void updateMouse(Args args) {
        double a0 = args.get(0);
        double a1 = args.get(1);

        MinecraftClient client = MinecraftClient.getInstance();
        ItemStack gun = client.player.getStackInHand(Hand.MAIN_HAND);
        if (gun.getItem() instanceof GunItem && client.mouse.wasRightButtonClicked() && GunItem.isLoaded(gun) && !client.player.getStackInHand(Hand.OFF_HAND).isOf(ItemRegistry.POLICE_SHIELD)) {
            NbtCompound nbtCompound = gun.getOrCreateNbt();
            if (nbtCompound.getBoolean(GunItem.TAG_IS_SCOPED)) {
                args.set(0, a0 * 0.2);
                args.set(1, a1 * 0.2);
            }
            else {
                args.set(0, a0 * 0.8);
                args.set(1, a1 * 0.8);
            }
        }
    }
}