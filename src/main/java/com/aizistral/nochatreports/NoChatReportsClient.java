package com.aizistral.nochatreports;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.core.ServerDataExtension;
import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.aizistral.nochatreports.core.SigningMode;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.telemetry.TelemetryInfoScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ProfileKeyPairManager;
import net.minecraft.network.chat.Component;

/**
 * Client initializer for the mod. Some networking setup here, as well as few screen-related events.
 * @author Aizistral
 */

@Environment(EnvType.CLIENT)
public final class NoChatReportsClient implements ClientModInitializer {
	private static boolean signingKeysPresent = false;

	@Override
	public void onInitializeClient() {
		NoChatReports.LOGGER.info("Client initialization...");

		ClientPlayConnectionEvents.JOIN.register(this::onPlayReady);
		ClientPlayConnectionEvents.DISCONNECT.register(this::onDisconnect);
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
				if (ServerSafetyState.isOnRealms()) {
					// NO-OP
				} else if (!handler.getConnection().isEncrypted()) {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.SECURE);
				} else if (client.getCurrentServer() instanceof ServerDataExtension ext &&
						ext.preventsChatReports()) {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.SECURE);
				} else if (NCRConfig.getServerPreferences().hasMode(ServerSafetyState.getLastServer(),
						SigningMode.ALWAYS)) {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.INSECURE);
					ServerSafetyState.setAllowChatSigning(true);
				} else {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.UNKNOWN);
				}
			} else {
				ServerSafetyState.updateCurrent(ServerSafetyLevel.SINGLEPLAYER);
			}

			if (NCRConfig.getCommon().enableDebugLog()) {
				NoChatReports.LOGGER.info("Sucessfully connected to server, safety state: {}", ServerSafetyState.getCurrent());
			}

			if (NCRConfig.getClient().demandOnServer() && !ServerSafetyState.getCurrent().isSecure()) {
				handler.getConnection().disconnect(Component.translatable("disconnect.nochatreports.client"));
			}
		});
	}

	public static boolean areSigningKeysPresent() {
		return signingKeysPresent;
	}

	public static void setSigningKeysPresent(boolean present) {
		signingKeysPresent = present;
	}

	public static void resendLastChatMessage() {
		var mc = Minecraft.getInstance();
		var chatScr = mc.screen instanceof ChatScreen chat ? chat : null;

		if (chatScr == null) {
			chatScr = new ChatScreen("");
			chatScr.init(mc, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
		}

		chatScr.handleChatInput(NCRConfig.getEncryption().getLastMessage(), false);
	}

}
