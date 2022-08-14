package com.aizistral.nochatreports.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.gui.UnsafeServerScreen;
import com.aizistral.nochatreports.mixins.client.MixinChatListener;
import com.aizistral.nochatreports.mixins.client.MixinToastComponent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.multiplayer.chat.ChatTrustLevel;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.util.Tuple;

/**
 * Simple implementation of JSON-config for the mod. Can be loaded at any time and does not depend
 * on modloader-specific API. The only disadvantage is that we don't get to comment options.<br><br>
 *
 * For now, the config file is called <code>NoChatReports.json</code> and is located in default config
 * folder. In the future this should be separated into client and server configs.
 *
 * @author Aizistral
 */

public class NoReportsConfig {
	private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("NoChatReports.json");
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static NoReportsConfig INSTANCE;
	private boolean demandOnClient = true, demandOnServer = false, enableDebugLog = false,
			convertToGameMessage = false, showServerSafety = true, hideRedChatIndicators = true,
			hideYellowChatIndicators = true, hideGrayChatIndicators = true, hideWarningToast = true,
			alwaysHideReportButton = false, disableTelemetry = true, showReloadButton = true,
			whitelistAllServers = false;
	private List<String> whitelistedServers;

	private static NoReportsConfig getInstance() {
		if (INSTANCE == null) {
			loadConfig();
		}

		return INSTANCE;
	}

	/**
	 * Loads (or reloads) the config from local file, or creates default one if that fails.
	 * Re-searializes validated {@link #INSTANCE} object back into the file.
	 */

	public static void loadConfig() {
		NoChatReports.LOGGER.info(INSTANCE == null ? "Loading config..." : "Reloading config...");

		INSTANCE = readFile();

		if (INSTANCE == null) {
			INSTANCE = new NoReportsConfig();
		}

		if (INSTANCE.whitelistedServers == null) {
			INSTANCE.whitelistedServers = new ArrayList<>();
		}

		writeFile(INSTANCE);
	}

	/**
	 * Writes current config instance into local file.
	 */

	public static void saveConfig() {
		if (INSTANCE != null) {
			writeFile(INSTANCE);
		}
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

	/**
	 * @return True if server with No Chat Reports installed should demand that mod is present on
	 * every client that tries to join. Mod's installation on client is optional otherwise.<br><br>
	 *
	 * This is false by default.
	 */

	public static boolean demandsOnClient() {
		return getInstance().demandOnClient;
	}

	/**
	 * @return True if client with No Chat Reports installed should demand that mod is present on
	 * every server that it tries to join. In that case client will not be able to join servers that
	 * don't have the mod installed. Servers without the mod can still provide varying degrees of
	 * safety, which {@link ServerSafetyLevel} and respective icon in bottom-right corner of the chat
	 * screen is designed to indicate.<br><br>
	 *
	 * This is false by default.
	 */

	public static boolean demandsOnServer() {
		return getInstance().demandOnServer;
	}

	/**
	 * @return True if server should convert all player messages to system messages. System messages
	 * do not feature signatures and look completely identical in the chat, with exception of gray line
	 * next to them added in pre-release 3. They are also unselectable for chat reporting.<br><br>
	 *
	 * This is false by default.
	 */

	public static boolean convertToGameMessage() {
		return getInstance().convertToGameMessage;
	}

	/**
	 * @return True if current {@link ServerSafetyLevel} should be indicated on client by an icon in
	 * the bottom-right corner of chat screen.<br><br>
	 *
	 * This is true by default.
	 */

	public static boolean showServerSafety() {
		return getInstance().showServerSafety;
	}

	/**
	 * @return Current list of whitelisted unsafe servers. Used on client to remember which servers
	 * user disabled unsafety warnings for.<br><br>
	 *
	 * This is empty by default.
	 *
	 * @see UnsafeServerScreen
	 */

	public static List<String> getWhitelistedServers() {
		return getInstance().whitelistedServers;
	}

	/**
	 * @param address Address of the server to be check.
	 * @return Whether this server is in the whitelist of unsafe servers, as returned by
	 * {@link #getWhitelistedServers()}.
	 * @see UnsafeServerScreen
	 */

	public static boolean isWhitelistedServer(ServerAddress address) {
		return getWhitelistedServers().contains(address.getHost() + ":" + address.getPort());
	}

	/**
	 * @return True if vanilla's unsigned messages trust indicator should be removed.<br><br>
	 *
	 * This is true by default.
	 *
	 * @see MixinChatListener#onEvaluateTrustLevel
	 */

	public static boolean hideRedChatIndicators() {
		return getInstance().hideRedChatIndicators;
	}

	/**
	 * @return True if vanilla's modified/hidden messages trust indicator should be removed.<br><br>
	 *
	 * This is true by default.
	 *
	 * @see MixinChatListener#onEvaluateTrustLevel
	 */

	public static boolean hideYellowChatIndicators() {
		return getInstance().hideYellowChatIndicators;
	}

	/**
	 * @return True if vanilla's system messages trust indicator should be removed.<br><br>
	 *
	 * This is true by default.
	 *
	 * @see com.aizistral.nochatreports.mixins.client.MixinGuiMessageTag#onSystem
	 */

	public static boolean hideGrayChatIndicators() {
		return getInstance().hideGrayChatIndicators;
	}

	/**
	 * @return True if "Chat messages can't be verified" toast should be removed.<br><br>
	 *
	 * This is true by default.
	 *
	 * @see MixinToastComponent#onAddToast
	 */

	public static boolean hideWarningToast() {
		return getInstance().hideWarningToast;
	}

	/**
	 * @return True if mod should log some additional information about its activity and message
	 * signatures. Generally only useful for debugging.<br><br>
	 *
	 * This is false by default.
	 */

	public static boolean isDebugLogEnabled() {
		return getInstance().enableDebugLog;
	}

	/**
	 * @return True if reporting buttons should always be hidden from social interactions screen,
	 * instead of simply being disabled.<br><br>
	 *
	 * This is false by default.
	 */

	public static boolean alwaysHideReportButton() {
		return getInstance().alwaysHideReportButton;
	}

	/**
	 * @return True if sending telemetry to Mojang should be disabled.<br><br>
	 *
	 * This is true by default.
	 */

	public static boolean disableTelemetry() {
		return getInstance().disableTelemetry;
	}

	/**
	 * @return True if the configuration reload button should be displayed in multiplayer screen.
	 * <br><br>
	 *
	 * This is true by default.
	 */

	public static boolean showReloadButton() {
		return getInstance().showReloadButton;
	}

	/**
	 * @return True if the full-screen warning should be skipped for servers that require chat signing,
	 * letting users simply join the server. Because of that, servers will no longer be added to the whitelist.
	 * <br><br>
	 *
	 * This is false by default.
	 */

	public static boolean whitelistAllServers() {
		return getInstance().whitelistAllServers;
	}

}
