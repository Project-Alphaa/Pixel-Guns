package com.ultreon.mods.pixelguns.item.gun.variant;

import com.ultreon.mods.pixelguns.client.GeoRendererGenerator;
import com.ultreon.mods.pixelguns.entity.damagesource.EnergyOrbDamageSource;
import com.ultreon.mods.pixelguns.item.gun.GunHitscanHelper;
import com.ultreon.mods.pixelguns.item.gun.GunItem;
import com.ultreon.mods.pixelguns.registry.ItemRegistry;
import com.ultreon.mods.pixelguns.registry.SoundRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class InfinityGunItem extends GunItem implements GeoItem {

    public InfinityGunItem() {
        super(
                false,
                80f,
                128,
                30,
                5,
                ItemRegistry.ENERGY_BATTERY,
                40,
                0,
                25.0f,
                1,
                LoadingType.CLIP,
                SoundRegistry.INFINITY_GUN_RELOAD,
                SoundRegistry.INFINITY_GUN_FIRE,
                1,
                false,
                new int[]{5, -1, -1}
        );
    }

    public static boolean isShooting(ItemStack infinityGun) {
        return infinityGun.getOrCreateSubNbt(NbtNames.INFINITY_GUN).getBoolean(NbtNames.IS_SHOOTING);
    }

    @Override
    public void shoot(PlayerEntity player, ItemStack stack) {
        if (!player.world.isClient) {
            NbtCompound infinityGun = stack.getOrCreateSubNbt(NbtNames.INFINITY_GUN);
            infinityGun.putBoolean(NbtNames.IS_SHOOTING, true);
        }

        super.shoot(player, stack);
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        NbtCompound infinityGun = stack.getOrCreateSubNbt(NbtNames.INFINITY_GUN);
        if (infinityGun.getBoolean(NbtNames.IS_SHOOTING)) {
            infinityGun.putBoolean(NbtNames.IS_SHOOTING, false);
            return true;
        }

        return super.isUsedOnRelease(stack);
    }

    @Override
    protected void handleHit(HitResult result, ServerWorld world, ServerPlayerEntity player) {
        // Check for blocks if no entities found
        if (result == null) {
            result = GunHitscanHelper.getCollision(player, this.range);
        }

        Vec3d hitLocation = result.getPos();
        Vec3d userPos = player.getEyePos();
        Vec3d beamDelta = hitLocation.subtract(userPos);
        Vec3d normalizedBeamDelta = beamDelta.normalize();

        // Spawn particles
        for (int i = 1; i < MathHelper.floor(beamDelta.length()); i++) {
            Vec3d lerpedLocation = userPos.add(normalizedBeamDelta.multiply(i));
            world.spawnParticles(ParticleTypes.SONIC_BOOM, lerpedLocation.x, lerpedLocation.y, lerpedLocation.z, 1, 0, 0, 0, 0);
        }

        // Explode world
        world.createExplosion(null, new EnergyOrbDamageSource(), new ExplosionBehavior() {
            @Override
            public Optional<Float> getBlastResistance(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState) {
                return Optional.of(0.0f);
            }
        }, hitLocation, 4.0f, false, World.ExplosionSourceType.MOB);
    }

    public static class NbtNames {
        public static final String INFINITY_GUN = "PixelGunsInfinityGun";
        public static final String SHOOT_TICKS = "shootTicks";
        public static final String IS_SHOOTING = "isShooting";
    }

    /*
     * Animation Side
     */

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private GeoItemRenderer<InfinityGunItem> renderer;

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = GeoRendererGenerator.gun(InfinityGunItem.this);
                }

                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}