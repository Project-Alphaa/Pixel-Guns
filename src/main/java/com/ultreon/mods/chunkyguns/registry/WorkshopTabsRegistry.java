package com.ultreon.mods.chunkyguns.registry;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class WorkshopTabsRegistry {

    public static final Map<Identifier, WorkshopTab> TABS = new HashMap<>();

    public static final WorkshopTab GUNS = registerTab("guns", new ItemStack(ItemRegistry.ASSAULT_RIFLE));
    public static final WorkshopTab AMMUNITION = registerTab("ammunition", new ItemStack(ItemRegistry.MEDIUM_BULLETS));
    public static final WorkshopTab ATTACHMENTS = registerTab("attachments", new ItemStack(ItemRegistry.LONG_SCOPE));

    public static void init() {
    }

    private static WorkshopTab registerTab(String name, ItemStack iconStack) {
        Identifier id = com.ultreon.mods.chunkyguns.ChunkyGuns.id(name);
        WorkshopTab tab = new WorkshopTab(id, iconStack);
        TABS.put(id, tab);
        return tab;
    }

    public record WorkshopTab(Identifier id, ItemStack iconStack) {

        public Text getDisplayName() {
            return Text.translatable("container.chunky_guns.workshop." + id.toTranslationKey());
        }
    }
}
