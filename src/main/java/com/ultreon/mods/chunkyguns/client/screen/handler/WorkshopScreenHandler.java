package com.ultreon.mods.chunkyguns.client.screen.handler;

import com.google.common.collect.ImmutableList;
import com.ultreon.mods.chunkyguns.item.recipe.WorkshopRecipe;
import com.ultreon.mods.chunkyguns.registry.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkshopScreenHandler extends AbstractRecipeScreenHandler<CraftingInventory> {

    private final ScreenHandlerContext context;
    private final Slot ingredientSlot1;
    private final Slot ingredientSlot2;
    private final Slot ingredientSlot3;
    private final Slot ingredientSlot4;
    private final Slot resultSlot;
    private final Property selectedRecipeIndex = Property.create();
    private final CraftingInventory inputInventory = new CraftingInventory(this, 2, 2) {

        @Override
        public void markDirty() {
            super.markDirty();
            onContentChanged(this);
        }
    };
    private final CraftingResultInventory resultInventory = new CraftingResultInventory();
    private final PlayerEntity player;
    private final Map<WorkshopTabsRegistry.WorkshopTab, List<WorkshopRecipe>> recipesByTab = new HashMap<>();
    private WorkshopTabsRegistry.WorkshopTab currentTab;
    private WorkshopRecipe currentRecipe;

    public WorkshopScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public WorkshopScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(ScreenHandlerRegistry.WORKSHOP_SCREEN_HANDLER, syncId);
        this.context = context;
        player = playerInventory.player;

        ingredientSlot1 = addSlot(new Slot(inputInventory, 0, 109, 22));
        ingredientSlot2 = addSlot(new Slot(inputInventory, 1, 127, 22));
        ingredientSlot3 = addSlot(new Slot(inputInventory, 2, 109, 40));
        ingredientSlot4 = addSlot(new Slot(inputInventory, 3, 127, 40));

        resultSlot = addSlot(new Slot(resultInventory, 4, 118, 66) {

            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                if (!player.world.isClient && currentRecipe != null) {
                    boolean hasChanged = false;
                    for (int i = 0; i < inputInventory.size(); i++) {
                        ItemStack ingredientStack = inputInventory.getStack(i);
                        for (Pair<Ingredient, Integer> pair : currentRecipe.getIngredientPairs()) {
                            if (pair.getLeft().test(ingredientStack)) {
                                ingredientStack.decrement(pair.getRight());
                                hasChanged = true;
                            }
                        }
                    }

                    if (hasChanged) {
                        inputInventory.markDirty();
                    }
                }
                super.onTakeItem(player, stack);
            }
        });

        // Player's Inventory
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 102 + y * 18));
            }
        }

        // Player's Hotbar
        for (int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 160));
        }

        addProperty(selectedRecipeIndex);

        if (!player.world.isClient) {
            WorkshopTabsRegistry.TABS.forEach((identifier, tab) -> {
                List<WorkshopRecipe> recipes = player.world.getRecipeManager()
                        .listAllOfType(RecipeRegistry.WORKSHOP_RECIPE_TYPE).stream()
                        .filter(recipe -> recipe.getTab() == tab).toList();
                if (!recipes.isEmpty()) {
                    recipesByTab.put(tab, recipes);
                }
            });
        }
        else {
            setCurrentTab(WorkshopTabsRegistry.GUNS);
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(context, player, BlockRegistry.WORKSHOP);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot.hasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            int containerSize = 5;
            int inventoryEnd = containerSize + 27;
            int hotbarEnd = inventoryEnd + 9;

            if (index == 4) {
                if (!insertItem(stack1, containerSize, hotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(stack1, stack);
            }
            else if (index >= containerSize) {
                if (!insertItem(stack1, 0, 4, false)) {
                    return ItemStack.EMPTY;
                }
                else if (index >= containerSize && index < inventoryEnd) {
                    if (!insertItem(stack1, inventoryEnd, hotbarEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= inventoryEnd && index < hotbarEnd && !insertItem(stack1, containerSize, inventoryEnd, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!insertItem(stack1, containerSize, hotbarEnd, false)) {
                return ItemStack.EMPTY;
            }

            if (stack1.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            }
            else {
                slot.markDirty();
            }

            if (stack1.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, stack1);
        }
        return stack;
    }

    public void updateRecipe(int i) {
        if (player.world.isClient) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(i);
            ClientPlayNetworking.send(PacketRegistry.WORKSHOP_UPDATE_RECIPE, buf);
        }
        else {
            int index = selectedRecipeIndex.get();
            List<WorkshopRecipe> recipes = getTabRecipes();
            if (recipes != null && !recipes.isEmpty()) {
                if (i == -1 && index < recipes.size()) {
                    index++;

                    if (index == recipes.size()) {
                        index = 0;
                    }
                }
                else if (i == 1 && index >= 0) {
                    index--;

                    if (index == -1) {
                        index = recipes.size() - 1;
                    }
                }
                else {
                    index = 0;
                }
                currentRecipe = recipes.get(index);
            }
            else {
                index = -1;
                currentRecipe = null;
            }

            selectedRecipeIndex.set(index);
            inputInventory.markDirty();
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeIdentifier(currentRecipe.getId());
            RecipeRegistry.WORKSHOP_SERIALIZER.write(buf, currentRecipe);
            ServerPlayNetworking.send((ServerPlayerEntity) player, PacketRegistry.WORKSHOP_UPDATE_RECIPE, buf);
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        context.run((world, pos) -> {
            if (!world.isClient && currentRecipe != null) {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                ItemStack result = ItemStack.EMPTY;
                if (currentRecipe.matches(inputInventory, world)) {
                    if (resultInventory.shouldCraftRecipe(world, serverPlayer, currentRecipe)) {
                        ItemStack stack = currentRecipe.craft(inputInventory);
                        if (stack.isItemEnabled(world.getEnabledFeatures())) {
                            result = stack;
                        }
                    }
                }

                resultInventory.setStack(4, result);
                setPreviousTrackedSlot(4, result);
                serverPlayer.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(syncId, nextRevision(), 4, result));
            }
        });
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        context.run((world, pos) -> dropInventory(player, inputInventory));
    }

    public void setCurrentTab(WorkshopTabsRegistry.WorkshopTab currentTab) {
        if (player.world.isClient) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeIdentifier(currentTab.id());
            ClientPlayNetworking.send(PacketRegistry.WORKSHOP_CHANGE_TAB, buf);
        }
        this.currentTab = currentTab;
        inputInventory.markDirty();
        updateRecipe(0);
    }

    public WorkshopTabsRegistry.WorkshopTab getCurrentTab() {
        return currentTab;
    }

    private List<WorkshopRecipe> getTabRecipes() {
        return recipesByTab.get(currentTab);
    }

    public List<Slot> getIngredientSlots() {
        return ImmutableList.of(ingredientSlot1, ingredientSlot2, ingredientSlot3, ingredientSlot4);//ingredientSlots;
    }

    public Slot getResultSlot() {
        return resultSlot;
    }

    @Override
    public void populateRecipeFinder(RecipeMatcher finder) {
        inputInventory.provideRecipeInputs(finder);
    }

    @Override
    public void clearCraftingSlots() {
        inputInventory.clear();
        resultInventory.clear();
    }

    @Override
    public boolean matches(Recipe<? super CraftingInventory> recipe) {
        return recipe.matches(inputInventory, player.world);
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 4;
    }

    @Override
    public int getCraftingWidth() {
        return inputInventory.getWidth();
    }

    @Override
    public int getCraftingHeight() {
        return inputInventory.getHeight();
    }

    @Override
    public int getCraftingSlotCount() {
        return 5;
    }

    @Override
    public RecipeBookCategory getCategory() {
        return null;
    }

    @Override
    public boolean canInsertIntoSlot(int index) {
        return index != getCraftingResultSlotIndex();
    }
}