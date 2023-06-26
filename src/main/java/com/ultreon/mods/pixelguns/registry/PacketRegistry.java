package com.ultreon.mods.pixelguns.registry;

import com.ultreon.mods.pixelguns.PixelGuns;
import com.ultreon.mods.pixelguns.network.packet.c2s.play.GunReloadC2SPacket;
import com.ultreon.mods.pixelguns.network.packet.c2s.play.GunShootC2SPacket;
import com.ultreon.mods.pixelguns.network.packet.c2s.play.WorkshopChangeTabC2SPacket;
import com.ultreon.mods.pixelguns.network.packet.c2s.play.WorkshopUpdateRecipeC2SPacket;
import com.ultreon.mods.pixelguns.network.packet.s2c.play.GrenadeExplodeS2CPacket;
import com.ultreon.mods.pixelguns.network.packet.s2c.play.GunCooldownS2CPacket;
import com.ultreon.mods.pixelguns.network.packet.s2c.play.RenderCrosshairS2CPacket;
import com.ultreon.mods.pixelguns.network.packet.s2c.play.WorkshopUpdateRecipeS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public class PacketRegistry {

    public static final Identifier GUN_RELOAD = PixelGuns.id("reload");
    public static final Identifier GUN_SHOOT = PixelGuns.id("shoot");
    public static final Identifier WORKSHOP_UPDATE_RECIPE = PixelGuns.id("workshop_update_recipe");
    public static final Identifier WORKSHOP_CHANGE_TAB = PixelGuns.id("workshop_change_tab");
    public static final Identifier GUN_COOLDOWN = PixelGuns.id("cooldown");
    public static final Identifier GRENADE_EXPLODE = PixelGuns.id("grenade_explode");
    public static final Identifier RENDER_CROSSHAIR = PixelGuns.id("render_crosshair");

    public static class CLIENT {
        public static void registerPackets() {
            PacketRegistry.CLIENT.registerPacket(GUN_COOLDOWN, new GunCooldownS2CPacket());
            PacketRegistry.CLIENT.registerPacket(GRENADE_EXPLODE, new GrenadeExplodeS2CPacket());
            PacketRegistry.CLIENT.registerPacket(WORKSHOP_UPDATE_RECIPE, new WorkshopUpdateRecipeS2CPacket());
            PacketRegistry.CLIENT.registerPacket(RENDER_CROSSHAIR, new RenderCrosshairS2CPacket());
        }

        private static void registerPacket(Identifier id, ClientPlayNetworking.PlayChannelHandler packetHandler) {
            ClientPlayNetworking.registerGlobalReceiver(id, packetHandler);
        }
    }

    public static class SERVER {

        public static void registerPackets() {
            PacketRegistry.SERVER.registerPacket(GUN_RELOAD, new GunReloadC2SPacket());
            PacketRegistry.SERVER.registerPacket(GUN_SHOOT, new GunShootC2SPacket());
            PacketRegistry.SERVER.registerPacket(WORKSHOP_UPDATE_RECIPE, new WorkshopUpdateRecipeC2SPacket());
            PacketRegistry.SERVER.registerPacket(WORKSHOP_CHANGE_TAB, new WorkshopChangeTabC2SPacket());
        }

        private static void registerPacket(Identifier id, ServerPlayNetworking.PlayChannelHandler packetHandler) {
            ServerPlayNetworking.registerGlobalReceiver(id, packetHandler);
        }
    }
}
