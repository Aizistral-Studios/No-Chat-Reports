package com.aizistral.nochatreports.handlers;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

public class NoReportsConfig {
	private static final File CONFIG_FOLDER = FabricLoader.getInstance().getConfigDir().toFile();
	private static final File CONFIG_FILE = new File(CONFIG_FOLDER, "NoChatReports.json");
	private static NoReportsConfig INSTANCE;
	private boolean demandOnClient, demandOnServer;

	public static void loadConfig() {
		INSTANCE = readFile();

		if (INSTANCE == null) {
			INSTANCE = new NoReportsConfig();
			INSTANCE.demandOnServer = true;
		}

		writeFile(INSTANCE);
	}

	@Nullable
	private static NoReportsConfig readFile() {
		if (!CONFIG_FILE.exists() || !CONFIG_FILE.isFile())
			return null;

		try (FileReader reader = new FileReader(CONFIG_FILE)) {
			NoReportsConfig transience = new Gson().fromJson(reader, NoReportsConfig.class);
			return transience;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private static void writeFile(NoReportsConfig instance) {
		try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(instance, writer);
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

}
