package com.aizistral.nochatreports;

import java.util.List;

import com.aizistral.nochatreports.core.NoReportsConfig;
import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.aizistral.nochatreports.gui.UnsafeServerScreen;
import com.aizistral.nochatreports.mixins.client.AccessorDisconnectedScreen;
import com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = NoChatReports.MODID, bus = Bus.FORGE)
public class NoChatReportsClient {
	private static final List<String> KEY_DISCONNECT_REASONS = ImmutableList.of(
			"multiplayer.disconnect.missing_public_key",
			"multiplayer.disconnect.invalid_public_key_signature",
			"multiplayer.disconnect.invalid_public_key"
			);
	private static boolean screenOverride = false;

	private NoChatReportsClient() {
		throw new IllegalStateException("Can't touch this");
	}

	@SubscribeEvent
	public static void onScreenInit(ScreenEvent.Init.Post event) {
		Minecraft client = Minecraft.getInstance();
		Screen screen = event.getScreen();

		if (screen instanceof AccessorDisconnectedScreen dsc) {
			if (screenOverride)
				return;

			screenOverride = true;
			if (dsc.getReason().getContents() instanceof TranslatableContents contents) {
				if (ServerSafetyState.allowsUnsafeServer()) {
					screen = new DisconnectedScreen(new JoinMultiplayerScreen(new TitleScreen()),
							CommonComponents.CONNECT_FAILED, dsc.getReason());
					client.setScreen(screen);
					screenOverride = false;
					return;
				} else if (KEY_DISCONNECT_REASONS.contains(contents.getKey())) {
					if (ServerSafetyState.getLastConnectedServer() != null) {
						if (!NoReportsConfig.isWhitelistedServer(ServerSafetyState.getLastConnectedServer())) {
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

	@SubscribeEvent
	public static void onDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
		Minecraft client = Minecraft.getInstance();
		client.execute(ServerSafetyState::reset);
	}

	@SubscribeEvent
	public static void onJoin(ClientPlayerNetworkEvent.LoggingIn event) {
		Minecraft client = Minecraft.getInstance();

		client.execute(() -> {
			ServerSafetyState.setReconnectCount(0);

			if (!client.isLocalServer()) {
				boolean canSend = NoChatReports.isDetectedOnServer();

				if (canSend) {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.SECURE);
				} else {
					ServerSafetyState.updateCurrent(ServerSafetyState.forceSignedMessages() ?
							ServerSafetyLevel.INSECURE : ServerSafetyLevel.UNINTRUSIVE);
				}
			}
		});
	}

	public static void reconnectLastServer() {
		ServerSafetyState.setReconnectCount(ServerSafetyState.getReconnectCount() + 1);
		ConnectScreen.startConnecting(new JoinMultiplayerScreen(new TitleScreen()), Minecraft.getInstance(),
				ServerSafetyState.getLastConnectedServer(), null);
	}


}
