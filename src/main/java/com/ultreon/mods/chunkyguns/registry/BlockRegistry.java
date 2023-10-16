package com.ultreon.mods.chunkyguns.registry;

import com.ultreon.mods.chunkyguns.block.BottleBlock;
import com.ultreon.mods.chunkyguns.block.DummyBlock;
import com.ultreon.mods.chunkyguns.block.WorkshopBlock;

import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

public class BlockRegistry {

    public static final Block WORKSHOP = BlockRegistry.register("workshop", new WorkshopBlock());
    public static final Block LIME_BOTTLE = BlockRegistry.register("lime_bottle", new BottleBlock(AbstractBlock.Settings.copy(Blocks.GLASS)));
    public static final Block LEMON_BOTTLE = BlockRegistry.register("lemon_bottle", new BottleBlock(AbstractBlock.Settings.copy(Blocks.GLASS)));
    public static final Block ORANGE_BOTTLE = BlockRegistry.register("orange_bottle", new BottleBlock(AbstractBlock.Settings.copy(Blocks.GLASS)));
    public static final Block DUMMY = BlockRegistry.register("dummy", new DummyBlock(AbstractBlock.Settings.of(Material.WOOD, MapColor.TERRACOTTA_WHITE).strength(0.5F).sounds(BlockSoundGroup.WOOD)));

    public static void init() {}

    private static Block register(String name, Block block) {
        return Registry.register(Registries.BLOCK, com.ultreon.mods.chunkyguns.ChunkyGuns.id(name), block);
    }
}