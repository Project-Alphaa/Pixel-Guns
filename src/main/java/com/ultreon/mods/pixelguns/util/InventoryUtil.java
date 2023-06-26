package com.ultreon.mods.pixelguns.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InventoryUtil {

    public static int itemCountInInventory(PlayerEntity player, Item item) {
        int itemCount = 0;
        for (int i = 0; i < player.getInventory().size(); ++i) {
            ItemStack currentStack = player.getInventory().getStack(i);
            if (currentStack.isEmpty() || !currentStack.isItemEqual(new ItemStack(item))) {
                continue;
            }
            itemCount += currentStack.getCount();
        }
        return Math.max(itemCount, 0);
    }

    public static void removeStacks(PlayerEntity player, ItemStack[] stacks) {
        for (ItemStack stack : stacks) {
            InventoryUtil.removeItemFromInventory(player, stack.getItem(), stack.getCount());
        }
    }

    public static void removeItemFromInventory(PlayerEntity player, Item item, int count) {
        int itemsToRemove = count;
        for (int i = 0; i < player.getInventory().size(); ++i) {
            ItemStack currentStack = player.getInventory().getStack(i);
            if (currentStack.isEmpty() || !currentStack.isItemEqual(new ItemStack(item))) {
                continue;
            }
            if (currentStack.getCount() > itemsToRemove) {
                currentStack.decrement(itemsToRemove);
                break;
            }
            currentStack.setCount(0);
            if ((itemsToRemove -= currentStack.getCount()) == 0) {
                break;
            }
        }
    }
}

