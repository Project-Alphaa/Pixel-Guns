package com.ultreon.mods.chunkyguns.network.packet.c2s.play;

import com.ultreon.mods.chunkyguns.client.screen.handler.WorkshopScreenHandler;
import com.ultreon.mods.chunkyguns.registry.WorkshopTabsRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class WorkshopChangeTabC2SPacket implements ServerPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Identifier tabId = buf.readIdentifier();
        server.execute(() -> {
            if (player.currentScreenHandler instanceof WorkshopScreenHandler screenHandler) {
                if (WorkshopTabsRegistry.TABS.containsKey(tabId)) {
                    screenHandler.setCurrentTab(WorkshopTabsRegistry.TABS.get(tabId));
                }
                else {
                    throw new IllegalStateException("Unknown workshop tab: " + tabId);
                }
            }
        });
    }
}
