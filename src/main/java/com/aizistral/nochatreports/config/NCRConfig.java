package com.aizistral.nochatreports.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.List;

import javax.annotation.Nullable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

public final class NCRConfig {
	private static NCRConfigCommon common = null;
	private static NCRConfigClient client = null;
	private static NCRServerWhitelist whitelist = null;
	private static boolean modRunning = false;

	private NCRConfig() {
		throw new IllegalStateException("Can't touch this");
	}

	public static synchronized NCRConfigCommon getCommon() {
		if (common == null) {
			common = JSONConfig.loadConfig(NCRConfigCommon.class, NCRConfigCommon::new, NCRConfigCommon.FILE_NAME);
			modRunning = common.getSavedEnableMod();
			JSONConfig.LOGGER.info("Mod Enabled: {}", modRunning);
		}

		return common;
	}

	public static boolean isModRunning() {
		getCommon();
		return modRunning;
	}

	@Environment(EnvType.CLIENT)
	public static synchronized NCRConfigClient getClient() {
		if (client == null) {
			client = JSONConfig.loadConfig(NCRConfigClient.class, NCRConfigClient::new, NCRConfigClient.FILE_NAME);
		}

		return client;
	}

	@Environment(EnvType.CLIENT)
	public static synchronized NCRServerWhitelist getServerWhitelist() {
		if (whitelist == null) {
			whitelist = JSONConfig.loadConfig(NCRServerWhitelist.class, NCRServerWhitelist::new, NCRServerWhitelist.FILE_NAME);
		}

		return whitelist;
	}

	public static void save() {
		List<JSONConfig> configs = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT
				? List.of(getCommon(), getClient(), getServerWhitelist())
				: List.of(getCommon());

		configs.forEach(JSONConfig::saveFile);

		Path readme = JSONConfig.CONFIG_DIR.resolve("NoChatReports/README.md");
		try {
			Files.writeString(readme, """
					# No Chat Reports
					You can find updated documentation of configuration files on the wiki:
					https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/Configuration-Files
					""", StandardCharsets.UTF_8);
		} catch (Exception ex) {
			JSONConfig.LOGGER.fatal("Could not write README file: {}", readme);
			throw new RuntimeException(ex);
		}
	}

}
