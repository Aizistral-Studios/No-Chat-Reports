package com.aizistral.nochatreports.forge;

import java.util.List;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.core.ServerDataExtension;
import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.aizistral.nochatreports.core.SigningMode;
import com.aizistral.nochatreports.gui.UnsafeServerScreen;
import com.google.common.collect.ImmutableList;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
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

@EventBusSubscriber(modid = "nochatreports", bus = Bus.FORGE)
public class NoChatReportsClient {
	private static boolean signingKeysPresent = false;

	private NoChatReportsClient() {
		throw new IllegalStateException("Can't touch this");
	}

	@OnlyIn(Dist.CLIENT)
	public static void onDisconnect(Minecraft client) {
		if (!NCRConfig.getClient().enableMod())
			return;

		if (NCRConfig.getCommon().enableDebugLog()) {
			NoChatReports.LOGGER.info("Disconnected from server, resetting safety state!");
		}

		ServerSafetyState.reset();
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onPlayReady(ClientPlayerNetworkEvent.LoggingIn event) {
		Minecraft client = Minecraft.getInstance();
		ClientPacketListener handler = client.getConnection();

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
