package com.aizistral.nochatreports.common.config;

import com.aizistral.nochatreports.common.NCRCore;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Implementation of ModMenu and ClothConfig support for the mod.
 */
@Environment(EnvType.CLIENT)
public final class ModMenuIntegration implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		if (ClothConfigIntegration.ACTIVE) {
			return ClothConfigIntegration::getConfigScreen;
		}

		NCRCore.LOGGER.warn("ClothConfig API not found, cannot provide config screen factory.");
		return ModMenuApi.super.getModConfigScreenFactory();
	}
}
