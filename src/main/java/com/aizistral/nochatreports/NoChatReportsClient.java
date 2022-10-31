package com.aizistral.nochatreports;

import java.util.List;

import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.config.NCRConfigClient;
import com.aizistral.nochatreports.core.ServerDataExtension;
import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.aizistral.nochatreports.gui.RealmsWarningScreen;
import com.aizistral.nochatreports.gui.UnsafeServerScreen;
import com.aizistral.nochatreports.network.ClientChannelHandler;
import com.google.common.collect.ImmutableList;
import com.mojang.realmsclient.RealmsMainScreen;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.Util;
import net.minecraft.client.GuiMessageTag;
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
import net.minecraft.realms.RealmsScreen;

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
	private static final Component CONNECT_FAILED_NCR = Component.translatable("connect.failed");
	private static boolean screenOverride = false;

	@Override
	public void onInitializeClient() {
		NoChatReports.LOGGER.info("Client initialization...");

		if (NCRConfig.getClient().enableMod()) {
			ClientChannelHandler.INSTANCE.register();
		}

		ClientPlayConnectionEvents.JOIN.register(this::onPlayReady);
		ClientPlayConnectionEvents.DISCONNECT.register(this::onDisconnect);
		ScreenEvents.AFTER_INIT.register(this::onScreenInit);
	}

	private void onScreenInit(Minecraft client, Screen screen, int scaledWidth, int scaledHeight) {
		if (!NCRConfig.getClient().enableMod())
			return;

		if (screen instanceof JoinMultiplayerScreen) {
			if (NCRConfig.getCommon().enableDebugLog()) {
				NoChatReports.LOGGER.info("Initialized JoinMultiplayerScreen screen, resetting safety state!");
			}

			ServerSafetyState.reset();
		}
	}

	private void onDisconnect(ClientPacketListener handler, Minecraft client) {
		if (!NCRConfig.getClient().enableMod())
			return;

		if (NCRConfig.getCommon().enableDebugLog()) {
			NoChatReports.LOGGER.info("Disconnected from server, resetting safety state!");
		}

		ServerSafetyState.reset();
	}

	private void onPlayReady(ClientPacketListener handler, PacketSender sender, Minecraft client) {
		if (!NCRConfig.getClient().enableMod())
			return;

		client.execute(() -> {
			if (!client.isLocalServer()) {
				boolean canSend = ClientPlayNetworking.canSend(NoChatReports.CHANNEL);

				if (ServerSafetyState.getCurrent() == ServerSafetyLevel.REALMS) {
					// NO-OP
				} else if (canSend) {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.SECURE);
				} else {
					if (client.getCurrentServer() instanceof ServerDataExtension ext
							&& ext.preventsChatReports()) {
						ServerSafetyState.updateCurrent(ServerSafetyLevel.SECURE);
					} else {
						ServerSafetyState.updateCurrent(ServerSafetyLevel.UNINTRUSIVE);
					}
				}
			}

			if (NCRConfig.getCommon().enableDebugLog()) {
				NoChatReports.LOGGER.info("Sucessfully connected to server, safety state: {}, reconnect count reset.", ServerSafetyState.getCurrent());
			}

			if (NCRConfig.getClient().demandOnServer() && !ClientPlayNetworking.canSend(NoChatReports.CHANNEL)) {
				handler.getConnection().disconnect(Component.translatable("disconnect.nochatreports.client"));
			}
		});
	}

}
