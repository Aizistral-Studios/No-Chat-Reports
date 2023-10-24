package com.aizistral.nochatreports.common.config;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

import com.aizistral.nochatreports.common.NCRCore;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public final class NCRConfig {
	private static NCRConfigCommon common = null;
	private static NCRConfigClient client = null;
	private static NCRServerPreferences serverPreferences = null;
	private static NCRConfigEncryption encryption = null;

	private NCRConfig() {
		throw new IllegalStateException("Can't touch this");
	}

	public static NCRConfigCommon getCommon() {
		return checkLoaded(() -> common);
	}

	@Environment(EnvType.CLIENT)
	public static NCRConfigClient getClient() {
		return checkLoaded(() -> client);
	}

	@Environment(EnvType.CLIENT)
	public static NCRServerPreferences getServerPreferences() {
		return checkLoaded(() -> serverPreferences);
	}

	@Environment(EnvType.CLIENT)
	public static NCRConfigEncryption getEncryption() {
		return checkLoaded(() -> encryption);
	}

	private static <T extends JSONConfig> T checkLoaded(Supplier<T> config) {
		if (config.get() == null) {
			load();
		}

		return config.get();
	}

	public static void load() {
		common = JSONConfig.loadConfig(NCRConfigCommon.class, NCRConfigCommon::new, NCRConfigCommon.FILE_NAME);

		if (NCRCore.getProvider().isOnClient()) {
			client = JSONConfig.loadConfig(NCRConfigClient.class, NCRConfigClient::new, NCRConfigClient.FILE_NAME);
			serverPreferences = JSONConfig.loadConfig(NCRServerPreferences.class, NCRServerPreferences::new, NCRServerPreferences.FILE_NAME);
			encryption = JSONConfig.loadConfig(NCRConfigEncryption.class, NCRConfigEncryption::new, NCRConfigEncryption.FILE_NAME);
		}

		save();
	}

	public static void save() {
		checkLoaded(() -> common).saveFile();

		if (NCRCore.getProvider().isOnClient()) {
			checkLoaded(() -> client).saveFile();
			checkLoaded(() -> serverPreferences).saveFile();
			checkLoaded(() -> encryption).saveFile();
		}

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
