package com.ultreon.mods.pixelguns.mixin.client;

import com.ultreon.mods.pixelguns.client.handler.CrosshairHandler;
import com.ultreon.mods.pixelguns.client.handler.RecoilHandler;
import com.ultreon.mods.pixelguns.item.gun.GunItem;
import com.ultreon.mods.pixelguns.util.ZoomablePlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    MinecraftClient client;

    @Inject(method = "render", at = @At("RETURN"))
    private void endRender(float tickDelta, long systemNanoTime, boolean shouldTick, CallbackInfo ci) {
        RecoilHandler.onRenderTick();
        CrosshairHandler.onRenderTick();
    }

    @Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
    private void bobView(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (client.getCameraEntity() instanceof PlayerEntity player) {
            player.getHandItems().forEach(stack -> {
                if (stack.getItem() instanceof GunItem && ((ZoomablePlayer) player).isPlayerZoomed()) {
                    ci.cancel();
                }
            });
        }
    }
}
