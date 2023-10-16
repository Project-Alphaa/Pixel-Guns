package com.ultreon.mods.chunkyguns.mixin.client.gun.ads;

import com.ultreon.mods.chunkyguns.item.gun.GunItem;
import com.ultreon.mods.chunkyguns.registry.ConfigRegistry;
import com.ultreon.mods.chunkyguns.registry.ItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Mouse.class)
public class AdsSensitivityModifier {

    @ModifyArgs(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"))
    private void updateMouse(Args args) {
        double deltaX = args.get(0);
        double deltaY = args.get(1);

        MinecraftClient client = MinecraftClient.getInstance();
        ItemStack gun = client.player.getStackInHand(Hand.MAIN_HAND);
        if (gun.getItem() instanceof GunItem && client.mouse.wasRightButtonClicked() && GunItem.isLoaded(gun) && !client.player.getStackInHand(Hand.OFF_HAND).isOf(ItemRegistry.POLICE_SHIELD)) {
            args.set(0, deltaX * 2 * ConfigRegistry.ads_sensitivity);
            args.set(1, deltaY * 2 * ConfigRegistry.ads_sensitivity);
        }
    }
}