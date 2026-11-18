package com.aizistral.nochatreports.common.config;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import com.aizistral.nochatreports.common.NCRCore;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import net.fabricmc.api.EnvType;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

public abstract class JSONConfig {
	protected static final Path CONFIG_DIR = NCRCore.getProvider().getConfigDir();
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
		NCRCore.LOGGER.debug("Writing config file {}...", this.fileName);
		writeFile(this.fileName, this);
	}

	protected void uponLoad() {
		// NO-OP
	}

	public abstract JSONConfig getDefault();

	public static <T extends JSONConfig> T loadConfig(Class<T> configClass, Supplier<T> freshInstance, String fileName) {
		NCRCore.LOGGER.debug("Reading config file {}...", fileName);
		T config = readFile(fileName, configClass).orElseGet(freshInstance);
		config.uponLoad();
		return config;
	}

	private static <T extends JSONConfig> Optional<T> readFile(String fileName, Class<T> configClass) {
		Path file = CONFIG_DIR.resolve(fileName);

		if (!Files.isRegularFile(file))
			return Optional.empty();

		String contents;
		try {
			contents = Files.readString(file, StandardCharsets.UTF_8);
		} catch (IOException ex) {
			NCRCore.LOGGER.fatal("Could not read config file: {}", file);
			NCRCore.LOGGER.catching(ex);
			throw new RuntimeException("Could not read config file: " + file, ex);
		}

		T config;
		try {
			config = GSON.fromJson(contents, configClass);
			if (config == null) {
				throw new JsonParseException("Config file contains no JSON value");
			}
		} catch (JsonParseException ex) {
			Path backup = file.resolveSibling(file.getFileName() + ".corrupt-" + UUID.randomUUID());
			try {
				Files.move(file, backup);
			} catch (IOException backupException) {
				NCRCore.LOGGER.fatal("Could not preserve corrupted config file: {}", file);
				NCRCore.LOGGER.catching(backupException);
				throw new RuntimeException("Could not preserve corrupted config file: " + file, backupException);
			}

			NCRCore.LOGGER.error("Could not parse config file: {}. The original was preserved as {}; loading defaults.",
					file, backup);
			NCRCore.LOGGER.catching(ex);
			return Optional.empty();
		}

		return Optional.of(config);
	}

	private static <T> void writeFile(String fileName, T config) {
		Path file = CONFIG_DIR.resolve(fileName);

		try {
			Files.createDirectories(file.getParent());
			try (BufferedWriter writer = Files.newBufferedWriter(file)) {
				GSON.toJson(config, writer);
			}
		} catch (Exception ex) {
			NCRCore.LOGGER.fatal("Could not write config file: {}", file);
			NCRCore.LOGGER.fatal("Full stacktrace below:");
			NCRCore.LOGGER.catching(ex);
			throw new RuntimeException("Could not write config file: " + file,ex);
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

		if (NCRCore.getProvider().isOnClient()) {
			builder.registerTypeAdapter(ServerAddress.class, ServerAddressAdapter.INSTANCE);
		}

		return builder.create();
	}

}
