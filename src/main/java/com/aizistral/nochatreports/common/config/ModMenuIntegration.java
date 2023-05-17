package com.aizistral.nochatreports.common.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.core.ServerSafetyState;
import com.aizistral.nochatreports.common.core.SigningMode;
import com.aizistral.nochatreports.common.gui.FontHelper;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;

/**
 * Implementation of ModMenu and ClothConfig support for the mod.
 * @author MODKILLER1001
 */

@Environment(EnvType.CLIENT)
public final class ModMenuIntegration implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		if (!ClothConfigIntegration.ACTIVE) {
			NCRCore.LOGGER.warn("ClothConfig API not found, cannot provide config screen factory.");
			return null;
		} else
			return ClothConfigIntegration::getConfigScreen;
	}

}