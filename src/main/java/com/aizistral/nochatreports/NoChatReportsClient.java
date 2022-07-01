package com.aizistral.nochatreports;

import java.util.List;

import com.aizistral.nochatreports.core.NoReportsConfig;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.gui.UnsafeServerScreen;
import com.aizistral.nochatreports.mixins.AccessorDisconnectedScreen;
import com.aizistral.nochatreports.network.ClientChannelHandler;
import com.aizistral.nochatreports.network.ServerChannelHandler;
import com.google.common.collect.ImmutableList;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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

@Environment(EnvType.CLIENT)
public class NoChatReportsClient implements ClientModInitializer {
	private static final List<String> KEY_DISCONNECT_REASONS = ImmutableList.of(
			"multiplayer.disconnect.missing_public_key",
			"multiplayer.disconnect.invalid_public_key_signature",
			"multiplayer.disconnect.invalid_public_key"
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

			if (dsc.getReason().getContents() instanceof TranslatableContents contents) {
				if (ServerSafetyState.allowsUnsafeServer()) {
					screenOverride = true;
					screen = new DisconnectedScreen(new JoinMultiplayerScreen(new TitleScreen()),
							CommonComponents.CONNECT_FAILED, dsc.getReason());
					client.setScreen(screen);
					screenOverride = false;
					return;
				} else if (KEY_DISCONNECT_REASONS.contains(contents.getKey())) {
					screenOverride = true;
					client.setScreen(new UnsafeServerScreen());
					screenOverride = false;
				}
			}
		}
	}

	private void onDisconnect(ClientPacketListener handler, Minecraft client) {
		client.execute(ServerSafetyState::reset);
	}

	private void onPlayReady(ClientPacketListener handler, PacketSender sender, Minecraft client) {
		client.execute(() -> {
			if (!client.isLocalServer()) {
				String ip = ServerSafetyState.getLastConnectedServer().getHost() + ":"
						+ ServerSafetyState.getLastConnectedServer().getPort();
				boolean canSend = ClientPlayNetworking.canSend(NoChatReports.CHANNEL);

				if (canSend) {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.SECURE);
				} else {
					ServerSafetyState.updateCurrent(ServerSafetyState.allowsUnsafeServer() ?
							ServerSafetyLevel.INSECURE : ServerSafetyLevel.UNINTRUSIVE);

					if (ServerSafetyState.getCurrent() != ServerSafetyLevel.INSECURE) {
						ServerSafetyState.updateCurrent(ServerSafetyLevel.UNINTRUSIVE);
					} else {
						// NO-OP
					}
				}
			}

			if (NoReportsConfig.demandsOnServer() && !ClientPlayNetworking.canSend(NoChatReports.CHANNEL)) {
				handler.getConnection().disconnect(Component.translatable("disconnect.nochatreports.client"));
			}
		});
	}

}
