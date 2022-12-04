package com.aizistral.nochatreports.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Supplier;

import com.aizistral.nochatreports.NoChatReports;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

public abstract class JSONConfig {
	protected static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();
	protected static final Gson GSON = createGson();

	protected final String fileName;
	protected final Path filePath;

	protected JSONConfig(String file) {
		this.fileName = file;
		this.filePath = CONFIG_DIR.resolve(this.fileName);
	}

	public Path getFile() {
		return this.filePath;
	}

	public void saveFile() {
		NoChatReports.LOGGER.info("Writing config file {}...", this.fileName);
		writeFile(this.fileName, this);
	}

	protected void uponLoad() {
		// NO-OP
	}

	public abstract JSONConfig getDefault();

	public static <T extends JSONConfig> T loadConfig(Class<T> configClass, Supplier<T> freshInstance, String fileName) {
		NoChatReports.LOGGER.info("Reading config file {}...", fileName);
		T config = readFile(fileName, configClass).orElseGet(freshInstance);
		config.uponLoad();
		return config;
	}

	private static <T extends JSONConfig> Optional<T> readFile(String fileName, Class<T> configClass) {
		Path file = CONFIG_DIR.resolve(fileName);

		if (!Files.isRegularFile(file))
			return Optional.empty();

		try (BufferedReader reader = Files.newBufferedReader(file)) {
			return Optional.of(GSON.fromJson(reader, configClass));
		} catch (Exception ex) {
			NoChatReports.LOGGER.fatal("Could not read config file: {}", file);
			NoChatReports.LOGGER.fatal("This likely indicates the file is corrupted. "
					+ "You can try deleting it to fix this problem. Full stacktrace below:");
			ex.printStackTrace();
			return null;
		}
	}

	private static <T> void writeFile(String fileName, T config) {
		Path file = CONFIG_DIR.resolve(fileName);

		try {
			Files.createDirectories(file.getParent());
			try (BufferedWriter writer = Files.newBufferedWriter(file)) {
				GSON.toJson(config, writer);
			}
		} catch (Exception ex) {
			NoChatReports.LOGGER.fatal("Could not write config file: {}", file);
			throw new RuntimeException(ex);
		}
	}

	private static Gson createGson() {
		GsonBuilder builder = new GsonBuilder();
		builder.setPrettyPrinting();
		builder.setExclusionStrategies(new ExclusionStrategy() {
			@Override
			public boolean shouldSkipField(FieldAttributes field) {
				return field.getDeclaringClass() == JSONConfig.class;
			}

			@Override
			public boolean shouldSkipClass(Class<?> theClass) {
				return false;
			}
		});

		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			builder.registerTypeAdapter(ServerAddress.class, ServerAddressAdapter.INSTANCE);
		}

		return builder.create();
	}

}
