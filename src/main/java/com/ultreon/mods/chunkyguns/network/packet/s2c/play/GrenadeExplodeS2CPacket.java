package com.ultreon.mods.chunkyguns.network.packet.s2c.play;

import com.ultreon.mods.chunkyguns.registry.SoundRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GrenadeExplodeS2CPacket implements ClientPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        BlockPos pos = buf.readBlockPos();
        client.execute(() -> {
            World world = client.player.getWorld();
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundRegistry.GRENADE_EXPLODE, SoundCategory.MASTER, 0.8f, 0.8f, false);
            world.addParticle(ParticleTypes.EXPLOSION_EMITTER, pos.getX(), pos.getY(), pos.getX(), 1.0, 0.0, 0.0);
        });
    }
}
