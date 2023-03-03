package com.aizistral.nochatreports.common;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerDataExtension;
import com.aizistral.nochatreports.common.core.ServerSafetyLevel;
import com.aizistral.nochatreports.common.core.ServerSafetyState;
import com.aizistral.nochatreports.common.core.SigningMode;
import com.aizistral.nochatreports.common.platform.PlatformProvider;
import com.aizistral.nochatreports.common.platform.events.ClientEvents;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class NCRClient {
	private static boolean signingKeysPresent = false;

	private NCRClient() {
		throw new IllegalStateException("Can't touch this");
	}

	static void setup() {
		NCRCore.LOGGER.info("Client initialization...");

		ClientEvents.DISCONNECT.register(NCRClient::onDisconnect);
		ClientEvents.PLAY_READY.register(NCRClient::onPlayReady);
	}

	private static void onDisconnect(MinecraftClient client) {
		if (!NCRConfig.getClient().enableMod())
			return;

		if (NCRConfig.getCommon().enableDebugLog()) {
			NCRCore.LOGGER.info("Disconnected from server, resetting safety state!");
		}

		ServerSafetyState.reset();
	}

	private static void onPlayReady(ClientPlayNetworkHandler handler, MinecraftClient client) {
		if (!NCRConfig.getClient().enableMod())
			return;

		client.execute(() -> {
			if (!client.isInSingleplayer()) {
				if (ServerSafetyState.isOnRealms()) {
					// NO-OP
				} else if (!handler.getConnection().isEncrypted()) {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.SECURE);
				} else if (client.getCurrentServerEntry() instanceof ServerDataExtension ext &&
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
				NCRCore.LOGGER.info("Sucessfully connected to server, safety state: {}", ServerSafetyState.getCurrent());
			}

			if (NCRConfig.getClient().demandOnServer() && !ServerSafetyState.getCurrent().isSecure()) {
				handler.getConnection().disconnect(Text.translatable("disconnect.nochatreports.client"));
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
		var mc = MinecraftClient.getInstance();
		var chatScr = mc.currentScreen instanceof ChatScreen chat ? chat : null;

		if (chatScr == null) {
			chatScr = new ChatScreen("");
			chatScr.init(mc, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
		}

		chatScr.sendMessage(NCRConfig.getEncryption().getLastMessage(), false);
	}

}
