package com.ultreon.mods.chunkyguns.item.gun;

import com.ultreon.mods.chunkyguns.event.GunEvents;
import com.ultreon.mods.chunkyguns.registry.KeyBindRegistry;
import com.ultreon.mods.chunkyguns.registry.PacketRegistry;
import com.ultreon.mods.chunkyguns.registry.TagRegistry;
import com.ultreon.mods.chunkyguns.util.InventoryUtil;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.TargetBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public abstract class GunItem extends Item {

    public static final String TAG_RELOAD_TICK = "reloadTick";
    public static final String TAG_CLIP = "Clip";
    public static final String TAG_CURRENT_CYCLE = "currentCycle";
    public static final String TAG_IS_SCOPED = "isScoped";
    public static final String TAG_IS_RELOADING = "isReloading";
    public static final String TAG_UUID = "uuid";
    public static final String TAG_IS_COOLING_DOWN = "isCoolingDown";
    
    public final boolean isAutomatic;
    protected final float damage;
    protected final int range;
    public final int fireCooldown;
    private final int magazineSize;
    public final Item ammunition;
    private final int reloadCooldown;
    protected final float bulletSpread;
    protected final float recoil;
    protected final int pelletCount;
    private final LoadingType loadingType;
    private final SoundEvent[] reloadSounds;
    private final int[] reloadSoundStages;
    protected final SoundEvent fireAudio;
    private final int reloadCycles;
    public final boolean isScoped;

    public GunItem(boolean isAutomatic, float damage, int range, int fireCooldown, int magazineSize, Item ammunition, int reloadCooldown, float bulletSpread, float recoil, int pelletCount, LoadingType loadingType, SoundEvent[] reloadSounds, SoundEvent fireAudio, int reloadCycles, boolean isScoped, int[] reloadStages) {
        super(new FabricItemSettings().maxCount(1));
        this.isAutomatic = isAutomatic;
        this.damage = damage;
        this.range = range;
        this.fireCooldown = fireCooldown;
        this.magazineSize = magazineSize;
        this.ammunition = ammunition;
        this.reloadCooldown = reloadCooldown;
        this.bulletSpread = bulletSpread;
        this.recoil = recoil;
        this.pelletCount = pelletCount;
        this.loadingType = loadingType;
        this.reloadSounds = reloadSounds;
        this.fireAudio = fireAudio;
        this.reloadCycles = reloadCycles;
        this.isScoped = isScoped;
        this.reloadSoundStages = reloadStages;
    }

    public static boolean isLoaded(ItemStack stack) {
        return GunItem.remainingAmmo(stack) > 0;
    }

    public static int remainingAmmo(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        return nbtCompound.getInt(TAG_CLIP);
    }

    public static int reserveAmmoCount(PlayerEntity player, Item item) {
        if (player.isCreative()) {
            return item.getMaxCount();
        }
        else {
            return InventoryUtil.itemCountInInventory(player, item);
        }
    }

    public void setDefaultNBT(NbtCompound nbtCompound) {
        nbtCompound.putInt(TAG_RELOAD_TICK, 0);
        nbtCompound.putInt(TAG_CURRENT_CYCLE, 1);
        nbtCompound.putInt(TAG_CLIP, 0);
        nbtCompound.putBoolean(TAG_IS_SCOPED, this.isScoped);
        nbtCompound.putBoolean(TAG_IS_RELOADING, false);
        nbtCompound.putBoolean(TAG_IS_COOLING_DOWN, false);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("item.chunky_guns.gun.ammo_type").formatted(Formatting.GRAY).append(" ").append(ammunition.getName().copy().formatted(Formatting.WHITE)));
        tooltip.add(Text.translatable("item.chunky_guns.gun.damage").formatted(Formatting.GRAY).append(Text.literal(" " + damage).formatted(Formatting.WHITE)));
        tooltip.add(Text.translatable("item.chunky_guns.gun.ammo").formatted(Formatting.GRAY).append(Text.literal(" " + GunItem.remainingAmmo(stack) + "/" + magazineSize).formatted(Formatting.WHITE)));
    }

    @Override
    public void inventoryTick(ItemStack stack, @NotNull World world, @NotNull Entity entity, int slot, boolean selected) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        ItemCooldownManager cooldownManager = ((PlayerEntity) entity).getItemCooldownManager();

        if (!nbtCompound.contains(TAG_UUID)) {
            nbtCompound.putUuid(TAG_UUID, UUID.randomUUID());
        }

        if (!(nbtCompound.contains(TAG_RELOAD_TICK) && nbtCompound.contains(TAG_CLIP) && nbtCompound.contains(TAG_IS_SCOPED) && nbtCompound.contains(TAG_IS_RELOADING))) {
            this.setDefaultNBT(nbtCompound);
        }

        if (world.isClient() && ((PlayerEntity) entity).getStackInHand(Hand.MAIN_HAND) == stack && KeyBindRegistry.RELOAD_KEY.isPressed() && GunItem.remainingAmmo(stack) < this.magazineSize && GunItem.reserveAmmoCount((PlayerEntity) entity, this.ammunition) > 0 && !nbtCompound.getBoolean(TAG_IS_RELOADING)) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            buf.writeBoolean(true);
            ClientPlayNetworking.send(PacketRegistry.GUN_RELOAD, buf);
        }

        if (nbtCompound.getBoolean(TAG_IS_RELOADING) && (((PlayerEntity) entity).getStackInHand(Hand.MAIN_HAND) != stack || GunItem.reserveAmmoCount((PlayerEntity) entity, this.ammunition) <= 0 && this.reloadCycles <= 1 || nbtCompound.getInt(TAG_RELOAD_TICK) >= this.reloadCooldown || GunItem.remainingAmmo(stack) >= this.magazineSize && this.reloadCycles <= 1)) {
            nbtCompound.putBoolean(TAG_IS_RELOADING, false);
        }

        if (nbtCompound.getBoolean(TAG_IS_RELOADING)) {
            this.doReloadTick(world, nbtCompound, (PlayerEntity) entity, stack);
        }
        else {
            if (nbtCompound.getInt(TAG_RELOAD_TICK) > this.reloadSoundStages[2] && nbtCompound.getInt(TAG_RELOAD_TICK) <= this.reloadCooldown) {
                this.finishReload((PlayerEntity) entity, stack);
            }
            nbtCompound.putInt(TAG_RELOAD_TICK, 0);
        }

        if (!world.isClient) {
            if (cooldownManager.isCoolingDown(stack.getItem()) && nbtCompound.getBoolean(TAG_IS_COOLING_DOWN)) {
                float cooldown = cooldownManager.getCooldownProgress(stack.getItem(), 0);

                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeUuid(nbtCompound.getUuid(TAG_UUID));
                buf.writeFloat(cooldown);

                for (ServerPlayerEntity trackingPlayer : PlayerLookup.tracking(entity)) {
                    if (!trackingPlayer.getUuid().equals(entity.getUuid())) {
                        ServerPlayNetworking.send(trackingPlayer, PacketRegistry.GUN_COOLDOWN, buf);
                    }
                }

                ServerPlayNetworking.send((ServerPlayerEntity) entity, PacketRegistry.GUN_COOLDOWN, buf);

                if (cooldown == 0) {
                    nbtCompound.putBoolean(TAG_IS_COOLING_DOWN, false);
                }
            }
        }
    }

    protected void doReloadTick(World world, NbtCompound nbtCompound, PlayerEntity player, ItemStack stack) {
        int reloadTick = nbtCompound.getInt(TAG_RELOAD_TICK);
        nbtCompound.putInt(TAG_RELOAD_TICK, nbtCompound.getInt(TAG_RELOAD_TICK) + 1);
        if (!world.isClient()) {
            if (reloadTick == this.reloadSoundStages[0]) {
                world.playSound(null, player.getX(), player.getY(), player.getZ(), this.reloadSounds[0], SoundCategory.MASTER, 1.0f, 1.0f);
            }
            else if (reloadTick == this.reloadSoundStages[1]) {
                world.playSound(null, player.getX(), player.getY(), player.getZ(), this.reloadSounds[1], SoundCategory.MASTER, 1.0f, 1.0f);
            }
            else if (reloadTick == this.reloadSoundStages[2]) {
                world.playSound(null, player.getX(), player.getY(), player.getZ(), this.reloadSounds[2], SoundCategory.MASTER, 1.0f, 1.0f);
            }
        }
        switch (this.loadingType) {
            case CLIP -> {
                if (reloadTick < this.reloadCooldown) {
                    break;
                }
                if (GunItem.reserveAmmoCount(player, this.ammunition) <= 0) {
                    break;
                }

                nbtCompound.putInt(TAG_CURRENT_CYCLE, 1);
                this.finishReload(player, stack);
                nbtCompound.putInt(TAG_RELOAD_TICK, 0);
            }
            case INDIVIDUAL -> {
                if (reloadTick < this.reloadSoundStages[2]) {
                    break;
                }
                if (nbtCompound.getInt(TAG_CURRENT_CYCLE) >= this.reloadCycles) {
                    break;
                }
                if (GunItem.reserveAmmoCount(player, this.ammunition) <= 0) {
                    break;
                }

                nbtCompound.putInt(TAG_CLIP, nbtCompound.getInt(TAG_CLIP) + 1);
                InventoryUtil.removeItemFromInventory(player, this.ammunition, 1);
                if (GunItem.remainingAmmo(stack) < this.magazineSize && GunItem.reserveAmmoCount(player, this.ammunition) > 0) {
                    nbtCompound.putInt(TAG_RELOAD_TICK, this.reloadSoundStages[1]);
                }
                nbtCompound.putInt(TAG_CURRENT_CYCLE, nbtCompound.getInt(TAG_CLIP));
            }
        }
    }

    protected void handleHit(HitResult result, ServerWorld world, ServerPlayerEntity damageSource) {
        GunEvents.GUN_HIT.invokeEvent(event -> event.onGunHit(result, world, damageSource));

        if (result instanceof EntityHitResult entityHitResult) {
            entityHitResult.getEntity().damage(DamageSource.player(damageSource), this.damage);
        }
        else if (result instanceof BlockHitResult blockHitResult) {
            BlockPos pos = blockHitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            if (blockHitResult.getType() == HitResult.Type.MISS) {
                return;
            }

            if (state.isIn(TagRegistry.BULLET_DESTROYS)) {
                world.breakBlock(pos, false);
            }

            if (block instanceof ButtonBlock buttonBlock) {
                buttonBlock.onUse(state, world, pos, damageSource, Hand.MAIN_HAND, blockHitResult);
            }

            if (block instanceof TargetBlock targetBlock) {
                TargetBlock.trigger(world, state, blockHitResult, null);
                damageSource.incrementStat(Stats.TARGET_HIT);
            }

            ParticleEffect particleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, world.getBlockState(pos));
            world.spawnParticles(particleEffect, blockHitResult.getPos().x, blockHitResult.getPos().y, blockHitResult.getPos().z, 1, 0, 0, 0, 1);
        }
    }

    public void shoot(PlayerEntity player, ItemStack stack) {
        GunEvents.GUN_SHOT_PRE.invokeEvent(event -> event.onGunShotPre(player, stack));

        if (player.world.isClient) {
            GunEvents.GUN_SHOT_POST.invokeEvent(event -> event.onGunShotPost(player, stack));
            return;
        }

        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        ServerWorld world = serverPlayer.getWorld();

        playFireAudio(world, player);
        serverPlayer.getItemCooldownManager().set(this, fireCooldown);
        stack.getOrCreateNbt().putBoolean(TAG_IS_COOLING_DOWN, true);

        for (int i = 0; i < pelletCount; ++i) {
            // TODO bullet spread
            handleHit(GunHitscanHelper.getCollision(player, range), world, serverPlayer);
        }

        if (!player.getAbilities().creativeMode) {
            useAmmo(stack);
        }

        GunEvents.GUN_SHOT_POST.invokeEvent(event -> event.onGunShotPost(player, stack));
    }

    public void playFireAudio(World world, PlayerEntity user) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(), this.fireAudio, SoundCategory.MASTER, 1.0f, 1.0f);
    }

    public float getRecoil() {
        return this.recoil;
    }

    protected void useAmmo(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        nbtCompound.putInt(TAG_CLIP, nbtCompound.getInt(TAG_CLIP) - 1);
    }

    public void finishReload(PlayerEntity player, ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        if (nbtCompound.getInt(TAG_CLIP) <= 0) {
            if (GunItem.reserveAmmoCount(player, this.ammunition) > this.magazineSize) {
                nbtCompound.putInt(TAG_CLIP, this.magazineSize);
                InventoryUtil.removeItemFromInventory(player, this.ammunition, this.magazineSize);
            }
            else {
                nbtCompound.putInt(TAG_CLIP, GunItem.reserveAmmoCount(player, this.ammunition));
                InventoryUtil.removeItemFromInventory(player, this.ammunition, GunItem.reserveAmmoCount(player, this.ammunition));
            }
        }
        else {
            int ammoToLoad = this.magazineSize - nbtCompound.getInt(TAG_CLIP);
            if (GunItem.reserveAmmoCount(player, this.ammunition) >= ammoToLoad) {
                nbtCompound.putInt(TAG_CLIP, nbtCompound.getInt(TAG_CLIP) + ammoToLoad);
                InventoryUtil.removeItemFromInventory(player, this.ammunition, ammoToLoad);
            }
            else {
                nbtCompound.putInt(TAG_CLIP, nbtCompound.getInt(TAG_CLIP) + GunItem.reserveAmmoCount(player, this.ammunition));
                InventoryUtil.removeItemFromInventory(player, this.ammunition, GunItem.reserveAmmoCount(player, this.ammunition));
            }
        }
    }

    @Override
    public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    public enum LoadingType {
        INDIVIDUAL,
        CLIP
    }
}

