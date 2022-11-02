package com.aizistral.nochatreports.core;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nullable;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.gui.UnsafeServerScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.LocalChatSession;
import net.minecraft.network.chat.SignedMessageChain.Encoder;

/**
 * All this global state is questionable, but we have to...
 * @author Aizistral
 */

@Environment(EnvType.CLIENT)
public final class ServerSafetyState {
	private volatile static ServerSafetyLevel current = ServerSafetyLevel.UNDEFINED;
	private volatile static ServerAddress lastServer = null;
	private static AtomicBoolean allowChatSigning = new AtomicBoolean(false);

	public static void updateCurrent(ServerSafetyLevel level) {
		current = level;
	}

	public static ServerSafetyLevel getCurrent() {
		return current;
	}

	public static boolean allowChatSigning() {
		return allowChatSigning.get();
	}

	public static void setAllowChatSigning(boolean allow) {
		if (allowChatSigning.compareAndSet(!allow, allow)) {
			if (Minecraft.getInstance().player != null) {
				var connection = Minecraft.getInstance().player.connection;

				if (allow && connection.chatSession == null) {
					Minecraft.getInstance().getProfileKeyPairManager().prepareKeyPair()
					.thenAcceptAsync(optional -> optional.ifPresent(profileKeyPair ->
					connection.setChatSession(LocalChatSession.create(profileKeyPair))),
							Minecraft.getInstance());
				}
			}
		}
	}

	public static void toggleChatSigning() {
		setAllowChatSigning(!allowChatSigning.get());
	}

	public static boolean isOnRealms() {
		return current == ServerSafetyLevel.REALMS;
	}

	public static boolean isDetermined() {
		return current != ServerSafetyLevel.UNINTRUSIVE && current != ServerSafetyLevel.UNDEFINED
				&& current != ServerSafetyLevel.UNKNOWN;
	}

	@Nullable
	public static ServerAddress getLastServer() {
		return lastServer;
	}

	public static void setLastServer(@Nullable ServerAddress address) {
		lastServer = address;
	}

	public static void reset() {
		lastServer = null;
		current = ServerSafetyLevel.UNDEFINED;
		allowChatSigning.set(false);
		UnsafeServerScreen.setHideThisSession(false);
	}

}
