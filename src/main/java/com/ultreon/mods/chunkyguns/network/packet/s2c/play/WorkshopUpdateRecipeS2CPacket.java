package com.ultreon.mods.chunkyguns.network.packet.s2c.play;

import com.ultreon.mods.chunkyguns.client.screen.WorkshopScreen;
import com.ultreon.mods.chunkyguns.item.recipe.WorkshopRecipe;
import com.ultreon.mods.chunkyguns.registry.RecipeRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class WorkshopUpdateRecipeS2CPacket implements ClientPlayNetworking.PlayChannelHandler {

    @Override
    public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Identifier recipeId = buf.readIdentifier();
        WorkshopRecipe recipe = RecipeRegistry.WORKSHOP_SERIALIZER.read(recipeId, buf);
        client.execute(() -> {
            if (client.currentScreen instanceof WorkshopScreen workshopScreen) {
                workshopScreen.changeRecipe(recipe);
            }
        });
    }
}
