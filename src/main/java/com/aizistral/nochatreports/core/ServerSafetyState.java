package com.aizistral.nochatreports.core;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nullable;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.config.NCRConfig;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

/**
 * All this global state is questionable, but we have to...
 * @author Aizistral
 */

@Environment(EnvType.CLIENT)
public final class ServerSafetyState {
	private static ServerSafetyLevel current = ServerSafetyLevel.UNKNOWN;
	private static ServerAddress lastServerAddress = null;
	private static ServerData lastServerData = null;
	private static AtomicBoolean allowUnsafeServer = new AtomicBoolean(false),
			sessionRequestedKey =  new AtomicBoolean(false), isOnServer = new AtomicBoolean(false);
	private static AtomicInteger reconnectCount = new AtomicInteger(0);
	private static AtomicLong disconnectMillis = new AtomicLong(0);

	public static void updateCurrent(ServerSafetyLevel level) {
		current = level;
	}

	public static ServerSafetyLevel getCurrent() {
		return current;
	}

	public static boolean allowsUnsafeServer() {
		return current != ServerSafetyLevel.SECURE ? allowUnsafeServer.get() : false;
	}

	public static void setAllowsUnsafeServer(boolean allows) {
		if (NCRConfig.getCommon().enableDebugLog()) {
			NoChatReports.LOGGER.info("Set allowUnsafeServer to: " + allows + ", value set in stacktrace:");
			NoChatReports.LOGGER.catching(new RuntimeException().fillInStackTrace());
		}

		allowUnsafeServer.set(allows);
	}

	public static void setSessionRequestedKey(boolean requested) {
		sessionRequestedKey.set(requested);
	}

	public static boolean sessionRequestedKey() {
		return current != ServerSafetyLevel.SECURE ? sessionRequestedKey.get() : false;
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
		return reconnectCount.get();
	}

	public static void setReconnectCount(int count) {
		reconnectCount.set(count);
	}

	public static long getDisconnectMillis() {
		return disconnectMillis.get();
	}

	public static void setDisconnectMillis(long millis) {
		disconnectMillis.set(millis);
	}

	public static boolean isOnServer() {
		return isOnServer.get();
	}

	public static void setOnServer(boolean on) {
		isOnServer.set(on);
	}

	public static void reset() {
		current = ServerSafetyLevel.UNKNOWN;
		allowUnsafeServer.set(false);
		sessionRequestedKey.set(false);
		isOnServer.set(false);

		if (NCRConfig.getCommon().enableDebugLog()) {
			NoChatReports.LOGGER.info("allowUnsafeServer: {}", allowUnsafeServer.get());
		}
	}

}
