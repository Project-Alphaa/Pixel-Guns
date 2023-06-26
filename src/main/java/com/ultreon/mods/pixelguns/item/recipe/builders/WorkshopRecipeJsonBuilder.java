package com.ultreon.mods.pixelguns.item.recipe.builders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ultreon.mods.pixelguns.registry.RecipeRegistry;
import com.ultreon.mods.pixelguns.registry.WorkshopTabsRegistry;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.function.Consumer;

public class WorkshopRecipeJsonBuilder implements CraftingRecipeJsonBuilder {

    private final WorkshopTabsRegistry.WorkshopTab tab;
    private final DefaultedList<Pair<Ingredient, Integer>> ingredients;
    private final Item result;

    private WorkshopRecipeJsonBuilder(WorkshopTabsRegistry.WorkshopTab tab, DefaultedList<Pair<Ingredient, Integer>> ingredients, ItemConvertible result) {
        this.tab = tab;
        this.ingredients = ingredients;
        this.result = result.asItem();
    }

    public static WorkshopRecipeJsonBuilder workshopRecipe(WorkshopTabsRegistry.WorkshopTab tab, ItemConvertible result, Pair<Ingredient, Integer>... ingredients) {
        if (ingredients.length > 4) {
            throw new IllegalArgumentException("Too many ingredients for workshop recipe. The max is 4");
        }

        DefaultedList<Pair<Ingredient, Integer>> ingredientsList = DefaultedList.of();
        Collections.addAll(ingredientsList, ingredients);
        return new WorkshopRecipeJsonBuilder(tab, ingredientsList, result);
    }

    @Override
    public CraftingRecipeJsonBuilder criterion(String name, CriterionConditions conditions) {
        return this;
    }

    @Override
    public CraftingRecipeJsonBuilder group(@Nullable String group) {
        return this;
    }

    @Override
    public Item getOutputItem() {
        return result;
    }

    @Override
    public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
        exporter.accept(new WorkshopRecipeJsonProvider(recipeId, tab, ingredients, result));
    }

    public static class WorkshopRecipeJsonProvider implements RecipeJsonProvider {

        private final Identifier recipeId;
        private final WorkshopTabsRegistry.WorkshopTab tab;
        private final DefaultedList<Pair<Ingredient, Integer>> ingredients;
        private final Item result;

        public WorkshopRecipeJsonProvider(Identifier recipeId, WorkshopTabsRegistry.WorkshopTab tab, DefaultedList<Pair<Ingredient, Integer>> ingredients, Item result) {
            this.recipeId = recipeId;
            this.tab = tab;
            this.ingredients = ingredients;
            this.result = result;
        }

        @Override
        public void serialize(JsonObject json) {
            JsonArray jsonIngredients = new JsonArray(4);

            for (Pair<Ingredient, Integer> pair : ingredients) {
                JsonObject object = new JsonObject();
                object.add("items", pair.getLeft().toJson());
                object.addProperty("count", pair.getRight());
                jsonIngredients.add(object);
            }

            json.add("ingredients", jsonIngredients);
            json.addProperty("tab", tab.id().toString());
            json.addProperty("result", Registries.ITEM.getId(result).toString());
        }

        @Override
        public Identifier getRecipeId() {
            return recipeId;
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return RecipeRegistry.WORKSHOP_SERIALIZER;
        }

        @Nullable
        @Override
        public JsonObject toAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public Identifier getAdvancementId() {
            return null;
        }
    }
}
