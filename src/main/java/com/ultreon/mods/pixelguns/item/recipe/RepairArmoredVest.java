package com.ultreon.mods.pixelguns.item.recipe;

import com.ultreon.mods.pixelguns.registry.ItemRegistry;
import com.ultreon.mods.pixelguns.registry.RecipeRegistry;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class RepairArmoredVest extends SpecialCraftingRecipe {

    public RepairArmoredVest(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingInventory inventory, World world) {
        int ironCount = 0;
        ItemStack armoredVest = ItemStack.EMPTY;

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                if (stack.isOf(ItemRegistry.ARMORED_VEST)) {
                    armoredVest = stack;
                }
                else if (stack.isOf(Items.IRON_INGOT)) {
                    ironCount++;
                }
                else {
                    return false;
                }
            }
        }

        return (ironCount > 0 && ironCount < 7) && !armoredVest.isEmpty() && armoredVest.isDamaged();
    }

    @Override
    public ItemStack craft(CraftingInventory inventory) {
        int ironCount = 0;
        ItemStack armoredVest = ItemStack.EMPTY;

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                if (stack.isOf(ItemRegistry.ARMORED_VEST)) {
                    armoredVest = stack;
                }
                else if (stack.isOf(Items.IRON_INGOT)) {
                    ironCount++;
                }
            }
        }

        ItemStack result = new ItemStack(ItemRegistry.ARMORED_VEST);

        result.setDamage(armoredVest.getDamage() - (ironCount * 25));
        return result;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.REPAIR_ARMORED_VEST;
    }
}
