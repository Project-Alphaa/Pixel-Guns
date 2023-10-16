package com.ultreon.mods.chunkyguns.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ultreon.mods.chunkyguns.client.screen.handler.WorkshopScreenHandler;
import com.ultreon.mods.chunkyguns.item.recipe.WorkshopRecipe;
import com.ultreon.mods.chunkyguns.registry.WorkshopTabsRegistry;
import com.ultreon.mods.chunkyguns.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class WorkshopScreen extends HandledScreen<WorkshopScreenHandler> implements ScreenHandlerListener {

    private static final Identifier TEXTURE = com.ultreon.mods.chunkyguns.ChunkyGuns.id("textures/gui/container/workshop.png");

    private float tick;
    private final List<WorkshopTabsRegistry.WorkshopTab> tabs = WorkshopTabsRegistry.TABS.values().stream().toList();
    private final List<GhostSlot> ghostSlots = Util.make(new ArrayList<>(), list -> {
        for (Slot slot : handler.getIngredientSlots()) {
            list.add(new GhostSlot(slot));
        }
    });
    private ItemStack resultStack = ItemStack.EMPTY;
    private WorkshopRecipe currentRecipe = null;

    public WorkshopScreen(WorkshopScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundHeight += 18;
        playerInventoryTitleX = x + 8;
        playerInventoryTitleY = y + 91;
    }

    @Override
    protected void init() {
        super.init();
        handler.addListener(this);

        addDrawableChild(new ButtonWidget(x + 86, y + 29, 15, 20, Text.literal("<"), button -> {
            handler.updateRecipe(-1);
        }, textSupplier -> Text.translatable("gui.chunky_guns.previous_item")));

        addDrawableChild(new ButtonWidget(x + 151, y + 29, 15, 20, Text.literal(">"), button -> {
            handler.updateRecipe(1);
        }, textSupplier -> Text.translatable("gui.chunky_guns.next_item")));
    }

    @Override
    public void removed() {
        super.removed();
        handler.removeListener(this);
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        tick++;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);

        for (WorkshopTabsRegistry.WorkshopTab tab : tabs) {
            if (RenderUtil.isMouseWithin(mouseX, mouseY, x + 28 * tabs.indexOf(tab), y - 28, 28, 28)) {
                renderTooltip(matrices, tab.getDisplayName(), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        renderUnselectedTabs(matrices);

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        renderSelectedTab(matrices);
        render3DResultItem(matrices);
        renderMissingIngredients(matrices);
    }

    private void renderMissingIngredients(MatrixStack matrices) {
        if (currentRecipe != null) {
            List<Slot> ingredientSlots = handler.getIngredientSlots();

            for (Slot slot : ingredientSlots) {
                ItemStack stack = slot.getStack();
                for (GhostSlot ghostSlot : ghostSlots) {
                    if (ghostSlot.slot.equals(slot)) {
                        if (slot.hasStack()) {
//                            relocateMissingIngredient(ghostSlot.ingredient);
                            if (!recipeContains(stack)) {
                                ghostSlot.renderType = GhostSlot.RenderType.INVALID;
                                break;
                            }
                            else if (!countMatchesRecipe(stack.getCount())) {
                                ghostSlot.renderType = GhostSlot.RenderType.INVALID;
                            }
                            else {
                                ghostSlot.renderType = GhostSlot.RenderType.NONE;
                            }
                            removeFromOthers(ghostSlot, stack);
                            break;
                        }
                        else if (ghostSlot.renderType != GhostSlot.RenderType.MISSING) {
                            method(ghostSlot);
                        }
                    }
                }
            }

            ghostSlots.forEach(ghostSlot -> ghostSlot.render(matrices));
        }
    }

    private void method(GhostSlot ghostSlot) {
//        for (Pair<Ingredient, Integer> pair : currentRecipe.getIngredientPairs()) {
//            for (GhostSlot gs : ghostSlots) {
//                if (!gs.equals(ghostSlot)) {
//                    int i = 0;
//                    if (!pair.equals(gs.ingredient)) {
//                        i++;
//                    }
//
//                    if (i == currentRecipe.getIngredientPairs().size() - 1) {
//                        ghostSlot.ingredient = pair;
//                        ghostSlot.renderType = GhostSlot.RenderType.MISSING;
//                        return;
//                    }
//                }
//            }
        for (GhostSlot gs : ghostSlots) {
            if (!gs.equals(ghostSlot)) {
                for (Pair<Ingredient, Integer> pair : currentRecipe.getIngredientPairs()) {
                    if (!pair.equals(gs.ingredient)) {
                        ghostSlot.ingredient = pair;
                        ghostSlot.renderType = GhostSlot.RenderType.MISSING;
                        return;
                    }
                }
            }
        }

        ghostSlot.ingredient = null;
        ghostSlot.renderType = GhostSlot.RenderType.NONE;
    }

    private boolean recipeContains(ItemStack stack) {
        for (Pair<Ingredient, Integer> pair : currentRecipe.getIngredientPairs()) {
            if (pair.getLeft().test(stack)) {
                return true;
            }
        }
        return false;
    }

    private boolean countMatchesRecipe(int count) {
        for (Pair<Ingredient, Integer> pair : currentRecipe.getIngredientPairs()) {
            if (pair.getRight() <= count) {
                return true;
            }
        }
        return false;
    }

    private void removeFromOthers(GhostSlot ghostSlot, ItemStack stack) {
        for (GhostSlot ghostSlot1 : ghostSlots) {
            if (!ghostSlot1.equals(ghostSlot)) {
                if (ghostSlot1.ingredient != null && ghostSlot1.ingredient.getLeft().test(stack)) {
                    ghostSlot1.ingredient = null;
                    ghostSlot1.renderType = GhostSlot.RenderType.NONE;
                }
            }
        }
    }

    private void relocateMissingIngredient(Pair<Ingredient, Integer> pair) {
        for (GhostSlot ghostSlot : ghostSlots) {
            if (!ghostSlot.slot.hasStack() && (ghostSlot.ingredient == null || ghostSlot.renderType == GhostSlot.RenderType.NONE)) {
                ghostSlot.ingredient = pair;
                ghostSlot.renderType = GhostSlot.RenderType.MISSING;
                return;
            }
        }
    }

    private void renderRedSlot(MatrixStack matrices, Slot slot) {
        int x = slot.x + this.x;
        int y = slot.y + this.y;
        fill(matrices, x, y, x + 16, y + 16, 822018048);
        RenderSystem.depthFunc(516);
        fill(matrices, x, y, x + 16, y + 16, 822083583);
        RenderSystem.depthFunc(515);
    }

    private void renderMissingItem(MatrixStack matrices, Ingredient ingredient, int count, int x, int y) {
        x += this.x;
        y += this.y;
        ItemRenderer itemRenderer = client.getItemRenderer();
        ItemStack[] stacks = ingredient.getMatchingStacks();
        ItemStack stack = stacks.length == 0 ? ItemStack.EMPTY : stacks[MathHelper.floor(tick / 30) % stacks.length].copy();

        stack.setCount(count);
        fill(matrices, x, y, x + 16, y + 16, 822018048);
        itemRenderer.renderInGui(stack, x, y);
        RenderSystem.depthFunc(516);
        fill(matrices, x, y, x + 16, y + 16, 822083583);
        RenderSystem.depthFunc(515);
        itemRenderer.renderGuiItemOverlay(client.textRenderer, stack, x, y);
    }

    private void renderUnselectedTabs(MatrixStack matrices) {
        for (int i = 0; i < tabs.size(); i++) {
            WorkshopTabsRegistry.WorkshopTab tab = tabs.get(i);
            if (tab != handler.getCurrentTab()) {
                renderTab(matrices, tab, i, 184);
            }
        }
    }

    private void renderSelectedTab(MatrixStack matrices) {
        WorkshopTabsRegistry.WorkshopTab currentTab = handler.getCurrentTab();
        if (currentTab != null && tabs.contains(currentTab)) {
            int i = tabs.indexOf(currentTab);
            renderTab(matrices, currentTab, i, 214);
        }
    }

    private void renderTab(MatrixStack matrices, WorkshopTabsRegistry.WorkshopTab tab, int index, int v) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        drawTexture(matrices, x + 28 * index, y - 28, index == 0 ? 0 : 28, v, 28, 32);

        MinecraftClient.getInstance().getItemRenderer().renderInGuiWithOverrides(tab.iconStack(), x + 28 * index + 6, y - 28 + 8);
        MinecraftClient.getInstance().getItemRenderer().renderGuiItemOverlay(textRenderer, tab.iconStack(), x + 28 * index + 6, y - 28 + 8, null);
    }

    private void render3DResultItem(MatrixStack matrices) {
        float partialTicks = MinecraftClient.getInstance().getTickDelta();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtil.scissor(x + 8, y + 17, 70, 70);

        MatrixStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.push();

        modelViewStack.translate(x + 40, y + 55, 100);
        modelViewStack.scale(50, -50, 50);
        modelViewStack.multiply(new Quaternionf(new AxisAngle4f(MathHelper.RADIANS_PER_DEGREE * (tick + partialTicks), 0, 1, 0)));
        modelViewStack.multiply(new Quaternionf(new AxisAngle4f(MathHelper.RADIANS_PER_DEGREE * 30F, 0, 0, -1)));
        RenderSystem.applyModelViewMatrix();
        VertexConsumerProvider.Immediate buffer = client.getBufferBuilders().getEntityVertexConsumers();
        itemRenderer.renderItem(resultStack, ModelTransformation.Mode.FIXED, false, matrices, buffer, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, RenderUtil.getModel(resultStack));
        buffer.draw();

        modelViewStack.pop();
        RenderSystem.applyModelViewMatrix();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            for (WorkshopTabsRegistry.WorkshopTab tab : tabs) {
                if (RenderUtil.isMouseWithin((int) mouseX, (int) mouseY, x + 28 * tabs.indexOf(tab), y - 28, 28, 28)) {
                    if (handler.getCurrentTab() != tab) {
                        handler.setCurrentTab(tab);
                        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1));
                        return true;
                    }
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void changeRecipe(WorkshopRecipe recipe) {
        currentRecipe = recipe;
        resultStack = recipe.getOutput();

        for (int i = 0; i < ghostSlots.size(); i++) {
            GhostSlot ghostSlot = ghostSlots.get(i);
            ghostSlot.renderType = GhostSlot.RenderType.NONE;

            if (i < currentRecipe.getIngredientPairs().size()) {
                ghostSlot.ingredient = currentRecipe.getIngredientPairs().get(i);
                ghostSlot.renderType = GhostSlot.RenderType.MISSING;
            }
        }
    }

    @Override
    public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
//        chunkyguns.LOGGER.info("Slot updated");
//        Slot slot = handler.getSlot(slotId);
//        for (GhostSlot ghostSlot : ghostSlots) {
//            if (ghostSlot.slot.equals(slot)) {
//                if (!recipeContains(stack)) {
//                    ghostSlot.renderType = GhostSlot.RenderType.INVALID;
//                    findFreeGhostSlot(ghostSlot);
//                }
//                // TODO if the item matches test the count
//                break;
//            }
//        }
    }

    @Override
    public void onPropertyUpdate(ScreenHandler handler, int property, int value) {

    }

    private class GhostSlot {

        private final Slot slot;
        private Pair<Ingredient, Integer> ingredient;
        private RenderType renderType = RenderType.NONE;

        public GhostSlot(Slot slot) {
            this.slot = slot;
        }

        public void render(MatrixStack matrices) {
            switch (renderType) {
                case MISSING -> {
                    if (ingredient != null) {
                        renderMissingItem(matrices, ingredient.getLeft(), ingredient.getRight(), slot.x, slot.y);
                    }
                    else {
                        renderType = RenderType.NONE;
                    }
                }
                case INVALID -> renderRedSlot(matrices, slot);
            }
        }

        public enum RenderType {
            NONE,
            MISSING,
            INVALID;
        }
    }
}
