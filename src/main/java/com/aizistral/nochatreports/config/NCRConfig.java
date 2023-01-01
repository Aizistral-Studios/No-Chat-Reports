package com.aizistral.nochatreports.config;

import com.aizistral.nochatreports.NoChatReports;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

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

	@OnlyIn(Dist.CLIENT)
	public static NCRConfigClient getClient() {
		return checkLoaded(() -> client);
	}

	@OnlyIn(Dist.CLIENT)
	public static NCRServerPreferences getServerPreferences() {
		return checkLoaded(() -> serverPreferences);
	}

	@OnlyIn(Dist.CLIENT)
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

		if (FMLEnvironment.dist == Dist.CLIENT) {
			client = JSONConfig.loadConfig(NCRConfigClient.class, NCRConfigClient::new, NCRConfigClient.FILE_NAME);
			serverPreferences = JSONConfig.loadConfig(NCRServerPreferences.class, NCRServerPreferences::new, NCRServerPreferences.FILE_NAME);
			encryption = JSONConfig.loadConfig(NCRConfigEncryption.class, NCRConfigEncryption::new, NCRConfigEncryption.FILE_NAME);
		}

		save();
	}

	public static void save() {
		checkLoaded(() -> common).saveFile();

		if (FMLEnvironment.dist == Dist.CLIENT) {
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
			NoChatReports.LOGGER.fatal("Could not write README file: {}", readme);
			throw new RuntimeException(ex);
		}
	}

}
