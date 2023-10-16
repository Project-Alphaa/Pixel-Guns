package com.ultreon.mods.chunkyguns.client;

import com.ultreon.mods.chunkyguns.item.gun.GunItem;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class GeoRendererGenerator {

    public static <T extends GunItem & GeoAnimatable> GeoItemRenderer<T> gun(T item) {
        return new GeoItemRenderer<>(new DefaultedItemGeoModel<>(Registries.ITEM.getId(item).withPrefixedPath("gun/")));
    }

    public static <T extends Item & GeoAnimatable> GeoItemRenderer<T> item(T item) {
        return new GeoItemRenderer<>(new DefaultedItemGeoModel<>(Registries.ITEM.getId(item)));
    }

    public static <T extends Entity & GeoEntity> GeoEntityRenderer<T> entity(EntityType<T> entityType, EntityRendererFactory.Context renderManager) {
        return new GeoEntityRenderer<>(renderManager, new DefaultedEntityGeoModel<>(Registries.ENTITY_TYPE.getId(entityType))) {
            @Override
            public void preRender(MatrixStack poseStack, T animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
                RenderUtils.faceRotation(poseStack, animatable, partialTick);
                super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            }
        };
    }

    public static <T extends ArmorItem & GeoItem> GeoArmorRenderer<T> armor(T armor) {
        return new GeoArmorRenderer<>(new DefaultedItemGeoModel<>(com.ultreon.mods.chunkyguns.ChunkyGuns.id("armor/" + armor.getMaterial().getName() + "_armor")));
    }
}
