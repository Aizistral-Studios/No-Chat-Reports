package com.aizistral.nochatreports.common.config;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.aizistral.nochatreports.common.NCRCore;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public final class NCRConfig {
	private static NCRConfigCommon common = null;
	private static NCRConfigClient client = null;
	private static NCRServerPreferences serverPreferences = null;
	private static boolean modRunning = false;

	private NCRConfig() {
		throw new IllegalStateException("Can't touch this");
	}

	public static synchronized NCRConfigCommon getCommon() {
		if (common == null) {
			common = JSONConfig.loadConfig(NCRConfigCommon.class, NCRConfigCommon::new, NCRConfigCommon.FILE_NAME);
			modRunning = common.getSavedEnableMod();
			NCRCore.LOGGER.info("Mod Enabled: {}", modRunning);
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
	public static synchronized NCRServerPreferences getServerPreferences() {
		if (serverPreferences == null) {
			serverPreferences = JSONConfig.loadConfig(NCRServerPreferences.class, NCRServerPreferences::new, NCRServerPreferences.FILE_NAME);
		}

		return serverPreferences;
	}

	public static void save() {
		List<JSONConfig> configs = NCRCore.getProvider().getEnvironment() == EnvType.CLIENT
				? List.of(getCommon(), getClient(), getServerPreferences())
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
			NCRCore.LOGGER.fatal("Could not write README file: {}", readme);
			throw new RuntimeException(ex);
		}
	}

}
