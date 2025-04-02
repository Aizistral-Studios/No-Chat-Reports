package com.aizistral.nochatreports.common.core;

import com.aizistral.nochatreports.common.gui.UnsafeServerScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.LocalChatSession;
import net.minecraft.network.protocol.game.ServerboundChatSessionUpdatePacket;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * All this global state is questionable, but we have to...
 * @author Aizistral
 */

@Environment(EnvType.CLIENT)
public final class ServerSafetyState {
	private static final List<Runnable> RESET_ACTIONS = new ArrayList<>();
	private static final List<Runnable> SIGNING_ACTIONS = new ArrayList<>();
	private static final AtomicBoolean ALLOW_CHAT_SIGNING = new AtomicBoolean(false);
	private volatile static ServerSafetyLevel current = ServerSafetyLevel.UNDEFINED;
	private volatile static ServerAddress lastServer = null;
	private volatile static String lastMessage = "???";

	public static void updateCurrent(ServerSafetyLevel level) {
		current = level;
	}

	public static ServerSafetyLevel getCurrent() {
		return current;
	}

	public static boolean allowChatSigning() {
		return ALLOW_CHAT_SIGNING.get();
	}

	public static CompletableFuture<Void> setAllowChatSigning(boolean allow) {
		if (ALLOW_CHAT_SIGNING.compareAndSet(!allow, allow)) {
			Minecraft mc = Minecraft.getInstance();

			if (mc.player != null) {
				var connection = mc.player.connection;

				if (allow && connection.chatSession == null)
					return mc.getProfileKeyPairManager().prepareKeyPair()
							.thenAcceptAsync(optional -> optional.ifPresent(profileKeyPair -> {
								connection.setKeyPair(profileKeyPair);

								SIGNING_ACTIONS.forEach(Runnable::run);
								SIGNING_ACTIONS.clear();
							}), mc);
			}
		}

		return CompletableFuture.completedFuture(null);
	}

	public static void toggleChatSigning() {
		setAllowChatSigning(!ALLOW_CHAT_SIGNING.get());
	}

	public static boolean isOnRealms() {
		return current == ServerSafetyLevel.REALMS;
	}

	public static boolean isInSingleplayer() {
		return current == ServerSafetyLevel.SINGLEPLAYER;
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

	public static String getLastMessage() {
		return lastMessage;
	}

	public static void setLastMessage(String message) {
		lastMessage = message;
	}

	public static void scheduleResetAction(Runnable action) {
		RESET_ACTIONS.add(action);
	}

	public static void scheduleSigningAction(Runnable action) {
		SIGNING_ACTIONS.add(action);
	}

	public static void reset() {
		lastServer = null;
		current = ServerSafetyLevel.UNDEFINED;
		ALLOW_CHAT_SIGNING.set(false);
		UnsafeServerScreen.setHideThisSession(false);

		RESET_ACTIONS.forEach(Runnable::run);
		RESET_ACTIONS.clear();
		SIGNING_ACTIONS.clear();
	}

}
