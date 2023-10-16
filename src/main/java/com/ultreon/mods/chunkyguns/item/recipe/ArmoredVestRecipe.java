package com.ultreon.mods.chunkyguns.item.recipe;

import com.ultreon.mods.chunkyguns.registry.ItemRegistry;
import com.ultreon.mods.chunkyguns.registry.RecipeRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class ArmoredVestRecipe extends SpecialCraftingRecipe {

    public ArmoredVestRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        List<Integer> slotIndexes = List.of(0, 2, 3, 5, 6, 7, 8);
        int ironCount = 0;
        int woolCount = 0;

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);

            if (!stack.isEmpty()) {
                if (i == 4 && stack.isOf(Blocks.YELLOW_WOOL.asItem())) {
                    woolCount++;
                }
                else if (slotIndexes.contains(i) && stack.isOf(Items.IRON_INGOT)) {
                    ironCount++;
                }
            }
        }

        return ironCount == 7 && woolCount == 1;
    }

    @Override
    public ItemStack craft(CraftingInventory inventory) {
        ItemStack result = new ItemStack(ItemRegistry.ARMORED_VEST);
        result.setDamage(result.getMaxDamage() - result.getMaxDamage() / 3);
        return result;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 9;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.ARMORED_VEST;
    }
}
