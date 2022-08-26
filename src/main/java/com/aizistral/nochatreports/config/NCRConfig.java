package com.aizistral.nochatreports.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import com.aizistral.nochatreports.NoChatReports;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

public final class NCRConfig {
	private static NCRCommonConfig common = null;
	private static NCRClientConfig client = null;

	private NCRConfig() {
		throw new IllegalStateException("Can't touch this");
	}

	public static NCRCommonConfig getCommon() {
		if (common == null) {
			load();
		}

		return common;
	}

	@Environment(EnvType.CLIENT)
	public static NCRClientConfig getClient() {
		if (client == null) {
			load();
		}

		return client;
	}

	public static void load() {
		common = JSONConfig.loadConfig(NCRCommonConfig.class, NCRCommonConfig::new, NCRCommonConfig.FILE_NAME);

		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			client = JSONConfig.loadConfig(NCRClientConfig.class, NCRClientConfig::new, NCRClientConfig.FILE_NAME);
		}

		save();
	}

	public static void save() {
		Objects.requireNonNull(common, "Cannot save config because it was not loaded!").saveFile();

		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			Objects.requireNonNull(client, "Cannot save config because it was not loaded!").saveFile();
		}

		Path readme = JSONConfig.CONFIG_DIR.resolve("NoChatReports/README.md");
		try {
			Files.writeString(readme, """
					# No Chat Reports
					You can find updated documentation of configuration files on the wiki:
					https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/Configuration-Files
					""", StandardCharsets.UTF_8);
		} catch (Exception ex) {
			NoChatReports.LOGGER.fatal("Could not write README file: {}", readme);
			throw new RuntimeException(ex);
		}
	}

}
