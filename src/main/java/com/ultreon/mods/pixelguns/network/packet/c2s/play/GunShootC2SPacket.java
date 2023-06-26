package com.ultreon.mods.pixelguns.network.packet.c2s.play;

import com.ultreon.mods.pixelguns.item.gun.GunItem;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class GunShootC2SPacket implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        ItemStack itemStack = player.getMainHandStack();
        if (itemStack.getItem() instanceof GunItem gunItem) {
            if (!player.isSprinting() && GunItem.isLoaded(itemStack)) {
                NbtCompound nbt = itemStack.getOrCreateNbt();

                gunItem.shoot(player, itemStack);
                nbt.putInt(GunItem.TAG_RELOAD_TICK, 0);
                nbt.putBoolean(GunItem.TAG_IS_RELOADING, false);
            }
        }
    }
}
