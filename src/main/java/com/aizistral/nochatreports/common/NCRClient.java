package com.aizistral.nochatreports.common;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerDataExtension;
import com.aizistral.nochatreports.common.core.ServerSafetyLevel;
import com.aizistral.nochatreports.common.core.ServerSafetyState;
import com.aizistral.nochatreports.common.core.SigningMode;
import com.aizistral.nochatreports.common.platform.PlatformProvider;
import com.aizistral.nochatreports.common.platform.events.ClientEvents;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.player.Input;
import net.minecraft.network.chat.MutableComponent;
import com.aizistral.nochatreports.common.config.ClothConfigIntegration;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class NCRClient {
	private static boolean signingKeysPresent = false;

	private NCRClient() {
		throw new IllegalStateException("Can't touch this");
	}

	static void setup() {
		NCRCore.LOGGER.debug("Client initialization...");

		KeyMapping cycleChatState = KeyBindingHelper.registerKeyBinding(new KeyMapping("gui.nochatreports.safety_status_hotkey", InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), "configuration.NoChatReports.config"));
		KeyMapping globalConfig = KeyBindingHelper.registerKeyBinding(new KeyMapping("configuration.NoChatReports.config.hotkey", InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), "configuration.NoChatReports.config"));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (cycleChatState.consumeClick()) {
				var address = ServerSafetyState.getLastServer();
				var preferences = NCRConfig.getServerPreferences();
				var nextMode = preferences.getModeUnresolved(address).next();
				preferences.setMode(address, nextMode);
				showState(client, nextMode.getName().toString());
			}
			while (globalConfig.isDown()) {
				ClothConfigIntegration.getConfigScreen(Minecraft.getInstance().screen);
			}
		});

		ClientEvents.DISCONNECT.register(NCRClient::onDisconnect);
		ClientEvents.PLAY_READY.register(NCRClient::onPlayReady);
	}

	private static void onDisconnect(Minecraft client) {
		if (!NCRConfig.getClient().enableMod())
			return;

		if (NCRConfig.getCommon().enableDebugLog()) {
			NCRCore.LOGGER.info("Disconnected from server, resetting safety state!");
		}

		ServerSafetyState.reset();
	}

	private static void onPlayReady(ClientPacketListener handler, Minecraft client) {
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
				NCRCore.LOGGER.info("Successfully connected to server, safety state: {}", ServerSafetyState.getCurrent());
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

	private static void showState(Minecraft client, String translationKey){
		assert client.player != null;
		client.player.displayClientMessage(Component.translatable(translationKey), true);
	}

	private static void showState(Minecraft client, boolean variable, String translationKey){
		assert client.player != null;
		client.player.displayClientMessage(Component.translatable(variable ? "options.on.composed" : "options.off.composed", Component.translatable(translationKey).getString()), true);
	}
}
