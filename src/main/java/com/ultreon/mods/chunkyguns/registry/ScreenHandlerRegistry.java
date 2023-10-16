package com.ultreon.mods.chunkyguns.registry;

import com.ultreon.mods.chunkyguns.client.screen.handler.WorkshopScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlerRegistry {

    public static final ScreenHandlerType<WorkshopScreenHandler> WORKSHOP_SCREEN_HANDLER = ScreenHandlerRegistry.register("workshop", WorkshopScreenHandler::new);

    public static void init() {}

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, com.ultreon.mods.chunkyguns.ChunkyGuns.id(name), new ScreenHandlerType<>(factory));
    }
}