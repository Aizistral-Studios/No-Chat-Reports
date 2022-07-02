package com.aizistral.nochatreports.core;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

/**
 * All this global state is questionable, but we have to...
 * @author Aizistral
 */

@Environment(EnvType.CLIENT)
public class ServerSafetyState {
	private static ServerSafetyLevel current = ServerSafetyLevel.UNKNOWN;
	private static ServerAddress lastConnectedServer = null;
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
	public static ServerAddress getLastConnectedServer() {
		return lastConnectedServer;
	}

	public static void setLastConnectedServer(@Nullable ServerAddress address) {
		lastConnectedServer = address;
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
