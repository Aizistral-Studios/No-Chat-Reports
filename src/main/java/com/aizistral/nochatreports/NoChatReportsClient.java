package com.aizistral.nochatreports;

import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.core.ServerDataExtension;
import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.aizistral.nochatreports.network.ClientChannelHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;

/**
 * Client initializer for the mod. Some networking setup here, as well as few screen-related events.
 * @author Aizistral
 */

@Environment(EnvType.CLIENT)
public final class NoChatReportsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		NoChatReports.LOGGER.info("Client initialization...");

		if (NCRConfig.getClient().enableMod()) {
			ClientChannelHandler.INSTANCE.register();
		}

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
				boolean canSend = ClientPlayNetworking.canSend(NoChatReports.CHANNEL);

				if (ServerSafetyState.isOnRealms()) {
					// NO-OP
				} else if (canSend) {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.SECURE);
				} else if (client.getCurrentServer() instanceof ServerDataExtension ext &&
						ext.preventsChatReports()) {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.SECURE);
				} else if (ServerSafetyState.getLastServer() != null &&
						NCRConfig.getServerWhitelist().isWhitelisted(ServerSafetyState.getLastServer())) {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.INSECURE);
					ServerSafetyState.setAllowChatSigning(true);
				} else {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.UNKNOWN);
				}
			} else {
				ServerSafetyState.updateCurrent(ServerSafetyLevel.SECURE);
			}

			if (NCRConfig.getCommon().enableDebugLog()) {
				NoChatReports.LOGGER.info("Sucessfully connected to server, safety state: {}", ServerSafetyState.getCurrent());
			}

			if (NCRConfig.getClient().demandOnServer() && !ClientPlayNetworking.canSend(NoChatReports.CHANNEL)) {
				handler.getConnection().disconnect(Component.translatable("disconnect.nochatreports.client"));
			}
		});
	}

}
