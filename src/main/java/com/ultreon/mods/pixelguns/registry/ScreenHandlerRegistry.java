package com.ultreon.mods.pixelguns.registry;

import com.ultreon.mods.pixelguns.PixelGuns;
import com.ultreon.mods.pixelguns.client.screen.handler.WorkshopScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlerRegistry {

    public static final ScreenHandlerType<WorkshopScreenHandler> WORKSHOP_SCREEN_HANDLER = ScreenHandlerRegistry.register("workshop", WorkshopScreenHandler::new);

    public static void init() {}

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, PixelGuns.id(name), new ScreenHandlerType<>(factory));
    }
}