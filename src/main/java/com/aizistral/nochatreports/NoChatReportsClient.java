package com.aizistral.nochatreports;

import java.util.List;

import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.core.ServerDataExtension;
import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.aizistral.nochatreports.gui.UnsafeServerScreen;
import com.aizistral.nochatreports.mixins.client.AccessorDisconnectedScreen;
import com.aizistral.nochatreports.network.ClientChannelHandler;
import com.google.common.collect.ImmutableList;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;

/**
 * Client initializer for the mod. Some networking setup here, as well as few screen-related events.
 * @author Aizistral
 */

@Environment(EnvType.CLIENT)
public final class NoChatReportsClient implements ClientModInitializer {
	private static final List<String> KEY_DISCONNECT_REASONS = ImmutableList.of(
			"multiplayer.disconnect.missing_public_key",
			"multiplayer.disconnect.invalid_public_key_signature",
			"multiplayer.disconnect.invalid_public_key"
			);
	private static final List<String> STRING_DISCONNECT_REASONS = ImmutableList.of(
			"A secure profile is required to join this server.",
			"Secure profile expired.",
			"Secure profile invalid."
			);
	private static boolean screenOverride = false;

	@Override
	public void onInitializeClient() {
		NoChatReports.LOGGER.info("Client initialization...");
		ClientPlayNetworking.registerGlobalReceiver(NoChatReports.CHANNEL, ClientChannelHandler.INSTANCE);
		ClientPlayConnectionEvents.JOIN.register(this::onPlayReady);
		ClientPlayConnectionEvents.DISCONNECT.register(this::onDisconnect);
		ScreenEvents.AFTER_INIT.register(this::onScreenInit);
	}

	private void onScreenInit(Minecraft client, Screen screen, int scaledWidth, int scaledHeight) {
		if (screen instanceof AccessorDisconnectedScreen dsc) {
			if (screenOverride)
				return;

			screenOverride = true;
			var disconnectReason = dsc.getReason();

			if (disconnectReason != null) {
				if (ServerSafetyState.allowsUnsafeServer()) {
					screen = new DisconnectedScreen(new JoinMultiplayerScreen(new TitleScreen()),
							CommonComponents.CONNECT_FAILED, dsc.getReason());
					client.setScreen(screen);
					screenOverride = false;
					return;
				} else if (STRING_DISCONNECT_REASONS.contains(disconnectReason.getString())
						|| (disconnectReason.getContents() instanceof TranslatableContents translatable &&
								KEY_DISCONNECT_REASONS.contains(translatable.getKey()))) {
					if (ServerSafetyState.getLastServerAddress() != null) {
						if (!NCRConfig.getServerWhitelist().isWhitelisted(ServerSafetyState.getLastServerAddress()) && !NCRConfig.getClient().whitelistAllServers()) {
							client.setScreen(new UnsafeServerScreen());
						} else {
							if (ServerSafetyState.getReconnectCount() <= 0) {
								ServerSafetyState.setAllowsUnsafeServer(true);
								reconnectLastServer();
							} else {
								ServerSafetyState.setReconnectCount(0);
							}
						}
					}
				}
			}
			screenOverride = false;
		}
	}

	private void onDisconnect(ClientPacketListener handler, Minecraft client) {
		client.execute(ServerSafetyState::reset);
	}

	private void onPlayReady(ClientPacketListener handler, PacketSender sender, Minecraft client) {
		client.execute(() -> {
			ServerSafetyState.setReconnectCount(0);

			if (!client.isLocalServer()) {
				boolean canSend = ClientPlayNetworking.canSend(NoChatReports.CHANNEL);

				if (canSend) {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.SECURE);
				} else if (ServerSafetyState.forceSignedMessages()) {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.INSECURE);
				} else {
					if (ServerSafetyState.getLastServerData() instanceof ServerDataExtension ext
							&& ext.preventsChatReports()) {
						ServerSafetyState.updateCurrent(ServerSafetyLevel.SECURE);
					} else {
						ServerSafetyState.updateCurrent(ServerSafetyLevel.UNINTRUSIVE);
					}
				}
			}

			if (NCRConfig.getClient().demandOnServer() && !ClientPlayNetworking.canSend(NoChatReports.CHANNEL)) {
				handler.getConnection().disconnect(Component.translatable("disconnect.nochatreports.client"));
			}
		});
	}

	public static void reconnectLastServer() {
		ServerSafetyState.setReconnectCount(ServerSafetyState.getReconnectCount() + 1);
		ConnectScreen.startConnecting(new JoinMultiplayerScreen(new TitleScreen()), Minecraft.getInstance(),
				ServerSafetyState.getLastServerAddress(), ServerSafetyState.getLastServerData());
	}

}
