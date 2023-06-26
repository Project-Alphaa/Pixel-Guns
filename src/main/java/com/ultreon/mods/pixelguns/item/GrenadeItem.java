package com.ultreon.mods.pixelguns.item;

import com.ultreon.mods.pixelguns.client.GeoRendererGenerator;
import com.ultreon.mods.pixelguns.entity.projectile.thrown.GrenadeEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GrenadeItem extends Item implements GeoItem {

    public GrenadeItem() {
        super(new FabricItemSettings().maxCount(16));
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 120;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        player.setCurrentHand(hand);
        if (world instanceof ServerWorld serverWorld) {
            this.triggerAnim(player, GeoItem.getOrAssignId(stack, serverWorld), "controller", "pull_pin");
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity playerEntity)) {
            return;
        }

        float throwProgress = ((float) this.getMaxUseTime(stack) - remainingUseTicks) / this.getMaxUseTime(stack);

        if (!world.isClient) {
            if (remainingUseTicks == 0) {
                world.createExplosion(null, null, null, user.getPos(), 4.5f, false, World.ExplosionSourceType.MOB);
            }
            else {
                GrenadeEntity grenade = new GrenadeEntity(world, playerEntity);
                grenade.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, throwProgress * 2.5f, 1.0f);
                world.spawnEntity(grenade);
            }
        }
        if (!playerEntity.isCreative()) {
            stack.decrement(1);
        }
        user.swingHand(Hand.MAIN_HAND);
        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        stack.removeSubNbt("GeckoLibID");
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity) {
            ((PlayerEntity) user).getItemCooldownManager().set(this, 20);
        }
        this.onStoppedUsing(stack, world, user, 0);
        return stack;
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }


    /*
     * Animation Side
     */

    public static final RawAnimation PULL_PIN = RawAnimation.begin().thenPlayAndHold("pull_pin");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private GeoItemRenderer<GrenadeItem> renderer;

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = GeoRendererGenerator.item(GrenadeItem.this);
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
        controllerRegistrar.add(new AnimationController<>(this, "controller", state ->
                PlayState.CONTINUE).triggerableAnim("pull_pin", PULL_PIN));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}