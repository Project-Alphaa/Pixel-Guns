package com.ultreon.mods.chunkyguns.mixin.client.gun;

import com.ultreon.mods.chunkyguns.item.gun.GunItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class AmmoHud {

    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    private int scaledWidth;

    @Shadow
    protected abstract PlayerEntity getCameraPlayer();

    @Shadow
    public abstract TextRenderer getTextRenderer();

    @Shadow private int scaledHeight;

    @Inject(method = "render", at = @At("TAIL"))
    public void renderAmmoHud(MatrixStack matrixStack, float f, CallbackInfo ci) {
        if (!client.options.hudHidden) {
            PlayerEntity player = getCameraPlayer();
            if (player == null) {
                return;
            }

            ItemStack heldItem = player.getMainHandStack();
            if (heldItem.getItem() instanceof GunItem) {
                TextRenderer textRenderer = getTextRenderer();
                String text = String.format("%s/%s", GunItem.remainingAmmo(heldItem), player.isCreative() ? "∞" : GunItem.reserveAmmoCount(player, ((GunItem) heldItem.getItem()).ammunition));
                textRenderer.drawWithShadow(matrixStack, text, ((float) scaledWidth / 2) + 95, scaledHeight - 30, 0xFFFFFF);
            }
        }
    }
}
