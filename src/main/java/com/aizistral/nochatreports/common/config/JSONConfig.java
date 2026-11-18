package com.aizistral.nochatreports.common.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Supplier;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.loader.LoaderPaths;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class JSONConfig {
	protected static final Path CONFIG_DIR = LoaderPaths.getConfigDir();
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

	protected Gson getGson() {
		return GSON;
	}

	protected void uponLoad() {
		// NO-OP
	}

	public abstract JSONConfig getDefault();

	public static <T extends JSONConfig> T loadConfig(Class<T> configClass, Supplier<T> freshInstance, String fileName) {
		NCRCore.LOGGER.debug("Reading config file {}...", fileName);
		T defaults = freshInstance.get();
		T config = readFile(fileName, configClass, defaults.getGson()).orElse(defaults);
		config.uponLoad();
		return config;
	}

	private static <T extends JSONConfig> Optional<T> readFile(String fileName, Class<T> configClass, Gson gson) {
		Path file = CONFIG_DIR.resolve(fileName);

		if (!Files.isRegularFile(file))
			return Optional.empty();

		try (BufferedReader reader = Files.newBufferedReader(file)) {
			return Optional.of(gson.fromJson(reader, configClass));
		} catch (Exception ex) {
			NCRCore.LOGGER.fatal("Could not read config file: {}", file);
			NCRCore.LOGGER.fatal("This likely indicates the file is corrupted. "
					+ "You can try deleting it to fix this problem. Full stacktrace below:");
			NCRCore.LOGGER.catching(ex);
			throw new RuntimeException("Could not read config file: " + file, ex);
		}
	}

	private static void writeFile(String fileName, JSONConfig config) {
		Path file = CONFIG_DIR.resolve(fileName);

		try {
			Files.createDirectories(file.getParent());
			try (BufferedWriter writer = Files.newBufferedWriter(file)) {
				config.getGson().toJson(config, writer);
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

		return builder.create();
	}

}
