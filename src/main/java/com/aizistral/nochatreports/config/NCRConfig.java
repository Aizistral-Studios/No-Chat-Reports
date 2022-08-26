package com.aizistral.nochatreports.config;

import java.util.ArrayList;
import java.util.Objects;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

public final class NCRConfig {
	private static NCRCommonConfig common = null;
	private static NCRClientConfig client = null;

	private NCRConfig() {
		throw new IllegalStateException("Can't touch this");
	}

	public static AbstractJSONConfig getCommon() {
		if (common == null) {
			load();
		}

		return common;
	}

	@Environment(EnvType.CLIENT)
	public static AbstractJSONConfig getClient() {
		if (client == null) {
			load();
		}

		return client;
	}

	public static void load() {
		common = AbstractJSONConfig.loadConfig(NCRCommonConfig.class, NCRCommonConfig::new, NCRCommonConfig.FILE_NAME);
		common.save();

		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			client = AbstractJSONConfig.loadConfig(NCRClientConfig.class, NCRClientConfig::new, NCRClientConfig.FILE_NAME);
			client.save();
		}
	}

	public static void save() {
		Objects.requireNonNull(common, "Cannot save config because it was not loaded!").save();

		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			Objects.requireNonNull(client, "Cannot save config because it was not loaded!").save();
		}
	}

}
