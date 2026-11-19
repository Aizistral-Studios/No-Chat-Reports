package com.aizistral.nochatreports.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

public class NoReportsConfig {
	private static final Logger LOGGER = LogManager.getLogger("NoChatReports");
	private static final Path CONFIG_FILE = FMLPaths.CONFIGDIR.get().resolve("NoChatReports.json");
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static NoReportsConfig INSTANCE;
	private boolean enableMod = true, demandOnClient, demandOnServer, convertToGameMessage;

	private static synchronized NoReportsConfig getInstance() {
		if (INSTANCE == null) {
			INSTANCE = readFile();

			if (INSTANCE == null) {
				INSTANCE = new NoReportsConfig();
				//INSTANCE.demandOnServer = true;
			}

			LOGGER.info("Mod Enabled: {}", INSTANCE.enableMod);
		}

		return INSTANCE;
	}

	public static void saveConfig() {
		writeFile(getInstance());
	}

	@Nullable
	private static NoReportsConfig readFile() {
		if (!Files.isRegularFile(CONFIG_FILE))
			return null;

		try (BufferedReader reader = Files.newBufferedReader(CONFIG_FILE)) {
			return GSON.fromJson(reader, NoReportsConfig.class);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private static void writeFile(NoReportsConfig instance) {
		try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_FILE)) {
			GSON.toJson(instance, writer);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static boolean isModRunning() {
		return getInstance().enableMod;
	}

	public static boolean demandsOnClient() {
		return getInstance().demandOnClient;
	}

	public static boolean demandsOnServer() {
		return getInstance().demandOnServer;
	}

	public static boolean convertsToGameMessage() {
		return getInstance().convertToGameMessage;
	}

}
