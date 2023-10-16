package com.ultreon.mods.chunkyguns.mixin.client.gun;

import com.ultreon.mods.chunkyguns.item.gun.GunItem;
import com.ultreon.mods.chunkyguns.item.gun.variant.InfinityGunItem;
import com.ultreon.mods.chunkyguns.registry.ItemRegistry;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntityRenderer.class)
public class PlayerGunPose {

    @Inject(method = "getArmPose", at = @At("TAIL"), cancellable = true)
    private static void gunPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> ci) {
        if (player.getStackInHand(hand).getItem() instanceof GunItem) {
            if (player.getStackInHand(hand).getOrCreateNbt().getInt(GunItem.TAG_RELOAD_TICK) > 0) {
                ci.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_CHARGE);
            }
            else if (GunItem.isLoaded(player.getStackInHand(hand)) && !player.getStackInHand(Hand.OFF_HAND).isOf(ItemRegistry.POLICE_SHIELD)) {
                ci.setReturnValue(BipedEntityModel.ArmPose.BOW_AND_ARROW);
            }
            return;
        }
        ci.setReturnValue(BipedEntityModel.ArmPose.ITEM);
    }

    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
    private void render(AbstractClientPlayerEntity player, float f, float g, MatrixStack poseStack, VertexConsumerProvider multiBufferSource, int i, CallbackInfo ci) {
        ItemStack itemInHand = player.getStackInHand(Hand.MAIN_HAND);
        if (itemInHand.getItem() instanceof InfinityGunItem) {
            boolean isShooting = InfinityGunItem.isShooting(itemInHand);
            if (isShooting) {
                itemInHand.getOrCreateSubNbt(InfinityGunItem.NbtNames.INFINITY_GUN).getInt(InfinityGunItem.NbtNames.SHOOT_TICKS);
                int tickCount = player.age;
                List<BeaconBlockEntity.BeamSegment> list = new ArrayList<>();
                list.add(new BeaconBlockEntity.BeamSegment(new float[]{1, 1, 1}));
                int distance = 0;
                poseStack.push();
                poseStack.translate(player.getX(), player.getY() + 1.0, player.getZ());
                for (int m = 0; m < list.size(); ++m) {
                    BeaconBlockEntity.BeamSegment beaconBeamSection = list.get(m);
                    BeaconBlockEntityRenderer.renderBeam(poseStack, multiBufferSource, f, tickCount, distance, m == list.size() - 1 ? 1024 : beaconBeamSection.getHeight(), beaconBeamSection.getColor());
                    distance += beaconBeamSection.getHeight();
                }
                poseStack.pop();
            }
        }
    }
}