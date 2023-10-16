package com.ultreon.mods.chunkyguns.mixin.common;

import com.ultreon.mods.chunkyguns.registry.ItemRegistry;
import com.ultreon.mods.chunkyguns.util.LivingEntityAccessor;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    private PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "updateTurtleHelmet", at = @At("RETURN"))
    private void armorTick(CallbackInfo ci) {
        ItemStack stack = getEquippedStack(EquipmentSlot.HEAD);
        LivingEntityAccessor accessor = ((LivingEntityAccessor) this);

        if (stack.isOf(ItemRegistry.GAS_MASK) && !world.isClient()) {
            List<Entity> entities = new ArrayList<>();

            entities.addAll(world.getEntitiesByClass(AreaEffectCloudEntity.class, getBoundingBox(), cloud -> true));
            entities.addAll(world.getEntitiesByClass(PotionEntity.class, getBoundingBox(), potion -> true));

            if (!entities.isEmpty() && stack.getDamage() < stack.getMaxDamage()) {
                stack.damage(1, random, (ServerPlayerEntity) (Object) this);
                accessor.isAffectedBySplashPotions(false);
            }
        }
        else {
            accessor.isAffectedBySplashPotions(true);
        }
    }
}
