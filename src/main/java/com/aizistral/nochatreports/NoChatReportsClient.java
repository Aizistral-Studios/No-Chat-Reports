package com.aizistral.nochatreports;

import java.util.List;

import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.core.ServerDataExtension;
import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.aizistral.nochatreports.gui.AwaitConnectionScreen;
import com.aizistral.nochatreports.gui.UnsafeServerScreen;
import com.aizistral.nochatreports.mixins.client.AccessorDisconnectedScreen;
import com.google.common.collect.ImmutableList;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = NoChatReports.MODID, bus = Bus.FORGE)
public class NoChatReportsClient {
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

	private NoChatReportsClient() {
		throw new IllegalStateException("Can't touch this");
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onScreenInit(ScreenEvent.Init.Post event) {
		Minecraft client = Minecraft.getInstance();
		Screen screen = event.getScreen();

		if (!NCRConfig.getClient().enableMod())
			return;

		if (screen instanceof AccessorDisconnectedScreen dsc) {
			if (screenOverride || ServerSafetyState.isOnServer() || screen.getTitle() == CONNECT_FAILED_NCR)
				return;

			screenOverride = true;
			var disconnectReason = dsc.getReason();

			if (disconnectReason != null) {
				if (NCRConfig.getCommon().enableDebugLog()) {
					NoChatReports.LOGGER.info("Disconnected with reason {}, reconnect count: {}",
							Component.Serializer.toStableJson(disconnectReason), ServerSafetyState.getReconnectCount());
				}

				if (ServerSafetyState.allowsUnsafeServer()) {
					screen = new DisconnectedScreen(new JoinMultiplayerScreen(new TitleScreen()),
							CONNECT_FAILED_NCR, dsc.getReason());
					client.setScreen(screen);
					screenOverride = false;
					return;
				} else if (STRING_DISCONNECT_REASONS.contains(disconnectReason.getString())
						|| (disconnectReason.getContents() instanceof TranslatableContents translatable &&
								KEY_DISCONNECT_REASONS.contains(translatable.getKey()))) {
					if (ServerSafetyState.getLastServerAddress() != null) {
						if (!NCRConfig.getServerWhitelist().isWhitelisted(ServerSafetyState.getLastServerAddress())) {
							if (NCRConfig.getCommon().enableDebugLog()) {
								NoChatReports.LOGGER.info("Server {} evaluated as unsafe and is not whitelisted, displaying warning screen.",
										ServerSafetyState.getLastServerAddress().getHost() + ":" + ServerSafetyState.getLastServerAddress().getPort());
							}

							client.setScreen(new UnsafeServerScreen());
						} else {
							NCRConfig.getClient().updateSigningCheck(ServerSafetyState.getLastServerAddress());

							if (ServerSafetyState.getReconnectCount() <= 0) {
								ServerSafetyState.setAllowsUnsafeServer(true);
								client.setScreen(new AwaitConnectionScreen(new JoinMultiplayerScreen(new TitleScreen())));
								screenOverride = false;
								return;
							} else {
								ServerSafetyState.setReconnectCount(0);
								screenOverride = false;
								return;
							}
						}
					}
				}
			}
			screenOverride = false;
		} else if (screen instanceof JoinMultiplayerScreen) {
			if (NCRConfig.getCommon().enableDebugLog()) {
				NoChatReports.LOGGER.info("Initialized JoinMultiplayerScreen screen, resetting safety state!");
			}

			ServerSafetyState.reset();
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static void onDisconnect(Minecraft client) {
		if (!NCRConfig.getClient().enableMod())
			return;

		if (NCRConfig.getCommon().enableDebugLog()) {
			NoChatReports.LOGGER.info("Disconnected from server, resetting safety state!");
		}

		ServerSafetyState.reset();
		ServerSafetyState.setDisconnectMillis(Util.getMillis());
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onPlayReady(ClientPlayerNetworkEvent.LoggingIn event) {
		Minecraft client = Minecraft.getInstance();

		if (!NCRConfig.getClient().enableMod())
			return;

		client.execute(() -> {
			ServerSafetyState.setReconnectCount(0);
			ServerSafetyState.setOnServer(true);

			if (!client.isLocalServer()) {
				boolean canSend = NoChatReports.isDetectedOnServer();

				if (ServerSafetyState.getCurrent() == ServerSafetyLevel.REALMS) {
					// NO-OP
				} else if (canSend) {
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

			if (NCRConfig.getCommon().enableDebugLog()) {
				NoChatReports.LOGGER.info("Sucessfully connected to server, safety state: {}, reconnect count reset.", ServerSafetyState.getCurrent());
			}
		});
	}

	@OnlyIn(Dist.CLIENT)
	public static void reconnectLastServer() {
		ServerSafetyState.setReconnectCount(ServerSafetyState.getReconnectCount() + 1);
		ConnectScreen.startConnecting(new JoinMultiplayerScreen(new TitleScreen()), Minecraft.getInstance(),
				ServerSafetyState.getLastServerAddress(), ServerSafetyState.getLastServerData());
	}


}
