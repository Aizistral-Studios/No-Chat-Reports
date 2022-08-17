package com.aizistral.nochatreports.core;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.util.Tuple;

/**
 * All this global state is questionable, but we have to...
 * @author Aizistral
 */

@Environment(EnvType.CLIENT)
public class ServerSafetyState {
	private static ServerSafetyLevel current = ServerSafetyLevel.UNKNOWN;
	private static ServerAddress lastServerAddress = null;
	private static ServerData lastServerData = null;
	private static boolean allowUnsafeServer = false, sessionRequestedKey = false;
	private static int reconnectCount = 0;

	public static void updateCurrent(ServerSafetyLevel level) {
		current = level;
	}

	public static ServerSafetyLevel getCurrent() {
		return current;
	}

	public static boolean allowsUnsafeServer() {
		return current != ServerSafetyLevel.SECURE ? allowUnsafeServer : false;
	}

	public static void setAllowsUnsafeServer(boolean allows) {
		allowUnsafeServer = allows;
	}

	public static void setSessionRequestedKey(boolean requested) {
		sessionRequestedKey = requested;
	}

	public static boolean sessionRequestedKey() {
		return current != ServerSafetyLevel.SECURE ? sessionRequestedKey : false;
	}

	public static boolean forceSignedMessages() {
		return allowsUnsafeServer() && sessionRequestedKey();
	}

	@Nullable
	public static ServerAddress getLastServerAddress() {
		return lastServerAddress;
	}

	@Nullable
	public static ServerData getLastServerData() {
		return lastServerData;
	}

	public static void setLastConnectedServer(@Nullable ServerAddress address, @Nullable ServerData data) {
		lastServerAddress = address;
		lastServerData = data;
	}

	public static int getReconnectCount() {
		return reconnectCount;
	}

	public static void setReconnectCount(int count) {
		reconnectCount = count;
	}

	public static void reset() {
		current = ServerSafetyLevel.UNKNOWN;
		allowUnsafeServer = sessionRequestedKey = false;
	}

}
