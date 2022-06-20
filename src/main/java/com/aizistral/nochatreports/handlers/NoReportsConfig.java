package com.aizistral.nochatreports.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NoReportsConfig {
	private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("NoChatReports.json");
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static NoReportsConfig INSTANCE;
	private boolean demandOnClient, demandOnServer, convertToGameMessage;

	public static void loadConfig() {
		INSTANCE = readFile();

		if (INSTANCE == null) {
			INSTANCE = new NoReportsConfig();
			//INSTANCE.demandOnServer = true;
		}

		writeFile(INSTANCE);
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

	public static boolean demandsOnClient() {
		return INSTANCE.demandOnClient;
	}

	public static boolean demandsOnServer() {
		return INSTANCE.demandOnServer;
	}

	public static boolean convertsToGameMessage() {
		return INSTANCE.convertToGameMessage;
	}

}
