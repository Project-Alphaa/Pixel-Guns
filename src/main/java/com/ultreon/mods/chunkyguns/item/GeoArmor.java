package com.ultreon.mods.chunkyguns.item;

import com.ultreon.mods.chunkyguns.client.GeoRendererGenerator;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GeoArmor extends ArmorItem implements GeoItem {

    public GeoArmor(ArmorMaterial material, EquipmentSlot equipmentSlot) {
        super(material, equipmentSlot, new FabricItemSettings());
    }

    /*
     * Animation Side
     */

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new ArmorRenderProvider());
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    protected class ArmorRenderProvider implements RenderProvider {

        private GeoArmorRenderer<?> renderer;

        @SuppressWarnings("unchecked")
        @Override
        public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {
            if (renderer == null) {
                renderer = GeoRendererGenerator.armor(GeoArmor.this);
            }

            renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
            return renderer;
        }
    }
}