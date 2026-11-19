package com.aizistral.nochatreports.common.core;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.gui.UnsafeServerScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.LocalChatSession;
import net.minecraft.network.chat.RemoteChatSession;
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
	private static final List<Runnable> SIGNING_ACTIONS = new ArrayList<>();
	private static final AtomicBoolean ALLOW_CHAT_SIGNING = new AtomicBoolean(false);
	private static final int SESSION_ACK_TIMEOUT_TICKS = 60;
	private static ClientPacketListener signingActionsOwner = null;
	private static int sessionAckTicksLeft = 0;
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
							.thenAcceptAsync(optional -> optional.ifPresent(connection::setKeyPair), mc);
			}
		}

		return CompletableFuture.completedFuture(null);
	}

	public static void tickSigningActions(ClientPacketListener connection) {
		if (SIGNING_ACTIONS.isEmpty())
			return;

		if (connection != signingActionsOwner) {
			SIGNING_ACTIONS.clear();
			signingActionsOwner = null;
			return;
		}

		if (connection.chatSession == null)
			return;

		boolean acknowledged = isChatSessionAcknowledged(connection);
		boolean ncrServer = !acknowledged && isNoChatReportsServer();

		if (!acknowledged && !ncrServer && --sessionAckTicksLeft > 0)
			return;

		if (NCRConfig.getCommon().enableDebugLog()) {
			NCRCore.LOGGER.info(acknowledged ? "Server acknowledged chat session, running signing actions."
					: ncrServer ? "Server runs No Chat Reports, running signing actions without acknowledgement."
					: "Server did not acknowledge chat session in time, running signing actions anyway.");
		}

		List<Runnable> actions = new ArrayList<>(SIGNING_ACTIONS);
		SIGNING_ACTIONS.clear();
		signingActionsOwner = null;
		actions.forEach(Runnable::run);
	}

	private static boolean isChatSessionAcknowledged(ClientPacketListener connection) {
		PlayerInfo info = connection.getPlayerInfo(connection.getLocalGameProfile().id());
		RemoteChatSession remote = info != null ? info.getChatSession() : null;
		return remote != null && remote.sessionId().equals(connection.chatSession.sessionId());
	}

	private static boolean isNoChatReportsServer() {
		ServerData server = Minecraft.getInstance().getCurrentServer();
		return server instanceof ServerDataExtension data && data.preventsChatReports()
				&& ServerAddress.parseString(server.ip).equals(lastServer);
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

	public static void scheduleSigningAction(Runnable action) {
		ClientPacketListener connection = Minecraft.getInstance().getConnection();

		if (connection != signingActionsOwner) {
			SIGNING_ACTIONS.clear();
			signingActionsOwner = connection;
		}

		SIGNING_ACTIONS.add(action);
		sessionAckTicksLeft = SESSION_ACK_TIMEOUT_TICKS;
	}

	public static void reset() {
		lastServer = null;
		current = ServerSafetyLevel.UNDEFINED;
		ALLOW_CHAT_SIGNING.set(false);
		UnsafeServerScreen.setHideThisSession(false);

		if (Minecraft.getInstance().isSameThread()) {
			SIGNING_ACTIONS.clear();
			signingActionsOwner = null;
		}
	}

}
