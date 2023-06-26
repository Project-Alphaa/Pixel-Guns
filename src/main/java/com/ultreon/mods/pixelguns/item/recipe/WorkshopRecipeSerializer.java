package com.ultreon.mods.pixelguns.item.recipe;

import com.google.gson.*;
import com.ultreon.mods.pixelguns.registry.WorkshopTabsRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;

public class WorkshopRecipeSerializer implements RecipeSerializer<WorkshopRecipe> {

    @Override
    public WorkshopRecipe read(Identifier id, JsonObject json) {
        Identifier tabId = Identifier.tryParse(json.get("tab").getAsString());
        if (tabId == null || !WorkshopTabsRegistry.TABS.containsKey(tabId)) {
            throw new JsonParseException("Invalid tab for workshop recipe: " + id);
        }

        WorkshopTabsRegistry.WorkshopTab tab = WorkshopTabsRegistry.TABS.get(tabId);

        JsonArray jsonIngredients = json.get("ingredients").getAsJsonArray();
        if (jsonIngredients.isEmpty()) {
            throw new JsonParseException("No ingredients for workshop recipe: " + id);
        }
        else if (jsonIngredients.size() > 4) {
            throw new JsonParseException("Too many ingredients for workshop recipe: " + id);
        }

        DefaultedList<Pair<Ingredient, Integer>> ingredients = DefaultedList.of();
        for (JsonElement element : jsonIngredients) {
            JsonObject object = element.getAsJsonObject();

            Ingredient ingredient = Ingredient.fromJson(element.getAsJsonObject().get("items"));
            int count = object.has("count") ? object.get("count").getAsInt() : 1;
            if (!ingredient.isEmpty() && count > 0) {
                ingredients.add(new Pair<>(ingredient, count));
            }
        }

        if (!json.has("result")) {
            throw new JsonSyntaxException("Missing result, expected to find a string or object");
        }


        ItemStack result;
        if (json.get("result").isJsonObject()) {
            result = ShapedRecipe.outputFromJson(json.get("result").getAsJsonObject());
        }
        else {
            String resultString = json.get("result").getAsString();
            Identifier identifier = new Identifier(resultString);
            result = new ItemStack(Registries.ITEM.getOrEmpty(identifier).orElseThrow(() ->
                    new IllegalStateException("Item: " + resultString + " does not exist")));
        }

        return new WorkshopRecipe(id, tab, ingredients, result);
    }

    @Override
    public WorkshopRecipe read(Identifier id, PacketByteBuf buf) {
        ItemStack result = buf.readItemStack();
        WorkshopTabsRegistry.WorkshopTab tab = WorkshopTabsRegistry.TABS.get(buf.readIdentifier());
        int ingredientCount = buf.readByte();
        DefaultedList<Pair<Ingredient, Integer>> ingredients = DefaultedList.of();

        for (int i = 0; i < ingredientCount; i++) {
            ingredients.add(new Pair<>(Ingredient.fromPacket(buf), buf.readInt()));
        }

        return new WorkshopRecipe(id, tab, ingredients, result);
    }

    @Override
    public void write(PacketByteBuf buf, WorkshopRecipe recipe) {
        buf.writeItemStack(recipe.getOutput());
        buf.writeIdentifier(recipe.getTab().id());
        buf.writeByte(recipe.getIngredientPairs().size());
        recipe.getIngredientPairs().forEach(pair -> {
            pair.getLeft().write(buf);
            buf.writeInt(pair.getRight());
        });
    }
}
