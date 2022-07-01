package com.aizistral.nochatreports.core;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

public class ServerSafetyState {
	private static ServerSafetyLevel current = ServerSafetyLevel.UNKNOWN;
	private static ServerAddress lastConnectedServer = null;
	private static boolean allowUnsafeServer = false;

	public static void updateCurrent(ServerSafetyLevel level) {
		current = level;
	}

	public static ServerSafetyLevel getCurrent() {
		return current;
	}

	public static boolean allowsUnsafeServer() {
		return allowUnsafeServer;
	}

	public static void setAllowsUnsafeServer(boolean allows) {
		allowUnsafeServer = allows;
	}

	@Nullable
	public static ServerAddress getLastConnectedServer() {
		return lastConnectedServer;
	}

	public static void setLastConnectedServer(@Nullable ServerAddress address) {
		lastConnectedServer = address;
	}

}
