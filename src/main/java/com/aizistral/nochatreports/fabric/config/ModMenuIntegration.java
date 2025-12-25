package com.aizistral.nochatreports.fabric.config;

import java.util.function.Function;

import net.minecraft.client.gui.screens.Screen;

import com.aizistral.nochatreports.common.NCRClient;

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
        Function<Screen, Screen> configScreenFactory = NCRClient.getConfigScreen();
        if (configScreenFactory != null) return configScreenFactory::apply;
        return ModMenuApi.super.getModConfigScreenFactory();
    }
}
