package com.ultreon.mods.chunkyguns.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import eu.midnightdust.lib.config.MidnightConfig;

public class ModMenuEntry implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> MidnightConfig.getScreen(parent, com.ultreon.mods.chunkyguns.ChunkyGuns.MOD_ID);
	}
}
