package com.ultreon.mods.pixelguns.data;

import com.ultreon.mods.pixelguns.PixelGuns;
import com.ultreon.mods.pixelguns.item.recipe.builders.WorkshopRecipeJsonBuilder;
import com.ultreon.mods.pixelguns.registry.ItemRegistry;
import com.ultreon.mods.pixelguns.registry.WorkshopTabsRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Pair;

import java.util.function.Consumer;

public class RecipeGenerator extends FabricRecipeProvider {

    public RecipeGenerator(FabricDataOutput output) {
        super(output);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        // Guns
        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.GUNS, ItemRegistry.ASSAULT_RIFLE, toIngredient(Items.IRON_INGOT, 45), toIngredient(Items.IRON_NUGGET, 20))
                .offerTo(exporter, PixelGuns.id("assault_rifle"));

        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.GUNS, ItemRegistry.COMBAT_SHOTGUN, toIngredient(Items.IRON_INGOT, 30), toIngredient(Items.IRON_NUGGET, 15))
                .offerTo(exporter, PixelGuns.id("combat_shotgun"));

        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.GUNS, ItemRegistry.INFINITY_GUN, toIngredient(Items.IRON_INGOT, 64), toIngredient(Items.NETHER_STAR, 1))
                .offerTo(exporter, PixelGuns.id("infinity_gun"));

        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.GUNS, ItemRegistry.LIGHT_ASSAULT_RIFLE, toIngredient(Items.IRON_INGOT, 38), toIngredient(Items.IRON_NUGGET, 12))
                .offerTo(exporter, PixelGuns.id("light_assault_rifle"));

        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.GUNS, ItemRegistry.PISTOL, toIngredient(Items.IRON_INGOT, 25), toIngredient(Items.IRON_NUGGET, 15))
                .offerTo(exporter, PixelGuns.id("pistol"));

        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.GUNS, ItemRegistry.REVOLVER, toIngredient(Items.IRON_INGOT, 35), toIngredient(Items.IRON_NUGGET, 20), new Pair<>(Ingredient.fromTag(ItemTags.PLANKS), 2))
                .offerTo(exporter, PixelGuns.id("revolver"));

        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.GUNS, ItemRegistry.ROCKET_LAUNCHER, toIngredient(Items.IRON_INGOT, 55), toIngredient(Items.IRON_NUGGET, 10))
                .offerTo(exporter, PixelGuns.id("rocket_launcher"));

        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.GUNS, ItemRegistry.SNIPER_RIFLE, toIngredient(Items.IRON_INGOT, 45), toIngredient(Items.IRON_NUGGET, 5), new Pair<>(Ingredient.fromTag(ItemTags.PLANKS), 5))
                .offerTo(exporter, PixelGuns.id("sniper_rifle"));

        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.GUNS, ItemRegistry.SUBMACHINE_GUN, toIngredient(Items.IRON_INGOT, 40), toIngredient(Items.IRON_NUGGET, 10))
                .offerTo(exporter, PixelGuns.id("submachine_gun"));

        // Ammo
        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.AMMUNITION, ItemRegistry.ENERGY_BATTERY, toIngredient(Items.IRON_INGOT, 8), toIngredient(Items.LIGHTNING_ROD, 1))
                .offerTo(exporter, PixelGuns.id("energy_battery"));

        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.AMMUNITION, ItemRegistry.HEAVY_BULLETS, toIngredient(Items.COPPER_INGOT, 4), toIngredient(Items.GUNPOWDER, 1))
                .offerTo(exporter, PixelGuns.id("heavy_bullets"));

        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.AMMUNITION, ItemRegistry.LIGHT_BULLETS, toIngredient(Items.COPPER_INGOT, 4), toIngredient(Items.GUNPOWDER, 1))
                .offerTo(exporter, PixelGuns.id("light_bullets"));

        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.AMMUNITION, ItemRegistry.MEDIUM_BULLETS, toIngredient(Items.COPPER_INGOT, 4), toIngredient(Items.GUNPOWDER, 1))
                .offerTo(exporter, PixelGuns.id("medium_bullets"));

        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.AMMUNITION, ItemRegistry.ROCKET, toIngredient(Items.IRON_INGOT, 2), toIngredient(Items.GUNPOWDER, 4))
                .offerTo(exporter, PixelGuns.id("rocket"));

        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.AMMUNITION, ItemRegistry.SHOTGUN_SHELL, toIngredient(Items.COPPER_INGOT, 4), toIngredient(Items.GOLD_NUGGET, 1), toIngredient(Items.GUNPOWDER, 1))
                .offerTo(exporter, PixelGuns.id("shotgun_shell"));

        // Attachments
        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.ATTACHMENTS, ItemRegistry.HEAVY_STOCK, toIngredient(Items.IRON_INGOT, 4), toIngredient(Items.IRON_NUGGET, 2))
                .offerTo(exporter, PixelGuns.id("heavy_stock"));

        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.ATTACHMENTS, ItemRegistry.LONG_SCOPE, toIngredient(Items.IRON_INGOT, 8), toIngredient(Items.GLOWSTONE_DUST, 1))
                .offerTo(exporter, PixelGuns.id("long_scope"));

        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.ATTACHMENTS, ItemRegistry.MEDIUM_SCOPE, toIngredient(Items.IRON_INGOT, 4), toIngredient(Items.GLOWSTONE_DUST, 1))
                .offerTo(exporter, PixelGuns.id("medium_scope"));

        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.ATTACHMENTS, ItemRegistry.SHORT_SCOPE, toIngredient(Items.IRON_INGOT, 2), toIngredient(Items.GLOWSTONE_DUST, 1))
                .offerTo(exporter, PixelGuns.id("short_scope"));

        WorkshopRecipeJsonBuilder.workshopRecipe(WorkshopTabsRegistry.ATTACHMENTS, ItemRegistry.SPECIALISED_GRIP, toIngredient(Items.IRON_INGOT, 2), toIngredient(Items.IRON_NUGGET, 8))
                .offerTo(exporter, PixelGuns.id("specialised_grip"));
    }

    private static Pair<Ingredient, Integer> toIngredient(ItemConvertible item, int count) {
        return new Pair<>(Ingredient.ofItems(item), count);
    }
}
