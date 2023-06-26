package com.ultreon.mods.pixelguns.mixin.client.gun;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ultreon.mods.pixelguns.PixelGuns;
import com.ultreon.mods.pixelguns.client.handler.CrosshairHandler;
import com.ultreon.mods.pixelguns.item.gun.GunItem;
import com.ultreon.mods.pixelguns.registry.ItemRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class CrosshairMixin {

    private static final Identifier HIT_CROSSHAIR = PixelGuns.id("textures/gui/hit_crosshair.png");

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    public void removeCrosshair(MatrixStack matrixStack, CallbackInfo info) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        ItemStack stack = player.getMainHandStack();
        if (stack.getItem() instanceof GunItem gunItem && !player.getStackInHand(Hand.OFF_HAND).isOf(ItemRegistry.POLICE_SHIELD)) {
            if (GunItem.isLoaded(stack) && MinecraftClient.getInstance().options.useKey.isPressed() && gunItem != ItemRegistry.SNIPER_RIFLE) {
                info.cancel();
            }
        }
    }

    @Inject(method = "renderCrosshair", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V"))
    private void changeCrosshair(MatrixStack matrices, CallbackInfo ci) {
        if (CrosshairHandler.renderCrosshair) {
            RenderSystem.setShaderTexture(0, HIT_CROSSHAIR);
        }
    }
}
