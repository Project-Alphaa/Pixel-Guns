package com.ultreon.mods.pixelguns.network.packet.s2c.play;

import com.ultreon.mods.pixelguns.PixelGunsClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public class GunCooldownS2CPacket implements ClientPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        UUID uuid = buf.readUuid();
        float cooldown = buf.readFloat();
        client.execute(() -> {
            PixelGunsClient.addOrUpdateTrackedGuns(uuid, cooldown);
        });
    }
}
