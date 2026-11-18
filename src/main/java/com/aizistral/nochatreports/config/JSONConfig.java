package com.aizistral.nochatreports.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraftforge.fml.loading.FMLPaths;

public abstract class JSONConfig {
	protected static final Logger LOGGER = LogManager.getLogger("NoChatReports");
	protected static final Path CONFIG_DIR = FMLPaths.CONFIGDIR.get();
	protected static final Gson GSON = new GsonBuilder().setPrettyPrinting()
			.setExclusionStrategies(new ExclusionStrategy() {
				@Override
				public boolean shouldSkipField(FieldAttributes field) {
					return field.getDeclaringClass() == JSONConfig.class;
				}

				@Override
				public boolean shouldSkipClass(Class<?> theClass) {
					return false;
				}
			}).create();

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
		LOGGER.info("Writing config file {}...", this.fileName);
		writeFile(this.fileName, this);
	}

	protected void uponLoad() {
		// NO-OP
	}

	public abstract JSONConfig getDefault();

	public static <T extends JSONConfig> T loadConfig(Class<T> configClass, Supplier<T> freshInstance, String fileName) {
		LOGGER.info("Reading config file {}...", fileName);
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
		} catch (IOException ex) {
			LOGGER.fatal("Could not read config file: {}", file);
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
			LOGGER.fatal("Could not write config file: {}", file);
			throw new RuntimeException(ex);
		}
	}

}
