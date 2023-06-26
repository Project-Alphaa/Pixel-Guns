package com.ultreon.mods.pixelguns.network.packet.c2s.play;

import com.ultreon.mods.pixelguns.client.screen.handler.WorkshopScreenHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class WorkshopUpdateRecipeC2SPacket implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int i = buf.readInt();
        server.execute(() -> {
            if (player.currentScreenHandler instanceof WorkshopScreenHandler screenHandler) {
                screenHandler.updateRecipe(i);
            }
        });
    }
}
