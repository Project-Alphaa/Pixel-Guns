package com.ultreon.mods.chunkyguns.registry;

import com.ultreon.mods.chunkyguns.item.recipe.ArmoredVestRecipe;
import com.ultreon.mods.chunkyguns.item.recipe.RepairArmoredVest;
import com.ultreon.mods.chunkyguns.item.recipe.WorkshopRecipe;
import com.ultreon.mods.chunkyguns.item.recipe.WorkshopRecipeSerializer;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class RecipeRegistry {

    public static final RecipeSerializer<WorkshopRecipe> WORKSHOP_SERIALIZER = registerSerializer("workshop", new WorkshopRecipeSerializer());
    public static final RecipeSerializer<?> ARMORED_VEST = registerSerializer("armored_vest", new SpecialRecipeSerializer<>(ArmoredVestRecipe::new));
    public static final RecipeSerializer<?> REPAIR_ARMORED_VEST = registerSerializer("repair_armored_vest", new SpecialRecipeSerializer<>(RepairArmoredVest::new));

    public static final RecipeType<WorkshopRecipe> WORKSHOP_RECIPE_TYPE = registerRecipeType("workshop", new RecipeType<WorkshopRecipe>() {
        @Override
        public String toString() {
            return "workshop";
        }
    });

    public static void init() {}

    private static <T extends Recipe<?>> RecipeSerializer<T> registerSerializer(String name, RecipeSerializer<T> serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, com.ultreon.mods.chunkyguns.ChunkyGuns.id(name), serializer);
    }

    private static <T extends Recipe<?>> RecipeType<T> registerRecipeType(String name, RecipeType<T> type) {
        return Registry.register(Registries.RECIPE_TYPE, com.ultreon.mods.chunkyguns.ChunkyGuns.id(name), type);
    }
}
