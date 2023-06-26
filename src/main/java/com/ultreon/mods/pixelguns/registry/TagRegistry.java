package com.ultreon.mods.pixelguns.registry;

import com.ultreon.mods.pixelguns.PixelGuns;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;

public class TagRegistry {

	public static final TagKey<Block> BULLET_DESTROYS = register(Registries.BLOCK, "bullet_destroys");

	private static <T> TagKey<T> register(Registry<T> registry, String name) {
		return TagKey.of(registry.getKey(), PixelGuns.id(name));
	}
}
