package com.ultreon.mods.pixelguns.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import com.ultreon.mods.pixelguns.PixelGuns;

import eu.midnightdust.lib.config.MidnightConfig;

public class ModMenuEntry implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> MidnightConfig.getScreen(parent, PixelGuns.MOD_ID);
	}
}
