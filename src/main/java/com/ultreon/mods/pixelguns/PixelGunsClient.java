package com.ultreon.mods.pixelguns;

import com.ultreon.mods.pixelguns.client.GeoRendererGenerator;
import com.ultreon.mods.pixelguns.client.screen.WorkshopScreen;
import com.ultreon.mods.pixelguns.registry.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.network.GeckoLibNetwork;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Environment(value = EnvType.CLIENT)
public class PixelGunsClient implements ClientModInitializer {

    public static final Map<UUID, Float> TRACKED_GUN_COOLDOWNS = new HashMap<>();

    public void onInitializeClient() {
        // Block renderers
        registerBlockRenderer(BlockRegistry.WORKSHOP, RenderLayer.getCutout());
        registerBlockRenderer(BlockRegistry.LIME_BOTTLE, RenderLayer.getCutoutMipped());
        registerBlockRenderer(BlockRegistry.LEMON_BOTTLE, RenderLayer.getCutoutMipped());
        registerBlockRenderer(BlockRegistry.ORANGE_BOTTLE, RenderLayer.getCutoutMipped());

        // Entity renderers
        registerEntityRenderer(EntityRegistry.GRENADE);
        registerEntityRenderer(EntityRegistry.ROCKET);

        // Screen renderers
        registerScreenRenderer(ScreenHandlerRegistry.WORKSHOP_SCREEN_HANDLER, WorkshopScreen::new);

        KeyBindRegistry.registerKeyBinds();
        PacketRegistry.CLIENT.registerPackets();
        ModelPredicateRegistry.registerModelPredicates();
        GeckoLibNetwork.registerClientReceiverPackets();
    }

    private static void registerBlockRenderer(Block block, RenderLayer renderLayer) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, renderLayer);
    }

    private static <T extends Entity & GeoEntity> void registerEntityRenderer(EntityType<T> entityType) {
        EntityRendererRegistry.register(entityType, (ctx) -> GeoRendererGenerator.entity(entityType, ctx));
    }

    private static <M extends ScreenHandler, U extends Screen & ScreenHandlerProvider<M>> void registerScreenRenderer(ScreenHandlerType<? extends M> screenHandlerType, HandledScreens.Provider<M, U> provider) {
        HandledScreens.register(screenHandlerType, provider);
    }

    public static void addOrUpdateTrackedGuns(UUID uuid, float cooldown) {
        if (TRACKED_GUN_COOLDOWNS.replace(uuid, cooldown) == null) {
            TRACKED_GUN_COOLDOWNS.put(uuid, cooldown);
        }
    }
}