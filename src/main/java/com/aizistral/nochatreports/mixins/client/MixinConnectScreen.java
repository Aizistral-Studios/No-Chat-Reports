package com.aizistral.nochatreports.mixins.client;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.config.NCRConfigClient;
import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.core.ServerSafetyState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

@Mixin(ConnectScreen.class)
public class MixinConnectScreen {

	/**
	 * @reason Intercept initiation of any new server connection. This way we can keep track of which
	 * server client currently tries to connect to
	 * @author Aizistral
	 */

	@Inject(method = "startConnecting", at = @At("HEAD"))
	private static void onStartConnecting(Screen screen, Minecraft minecraft, ServerAddress serverAddress,
			@Nullable ServerData serverData, CallbackInfo info) {
		if (!NCRConfig.getClient().enableMod())
			return;

		ServerSafetyState.updateCurrent(ServerSafetyLevel.UNDEFINED); // just to be 100% sure
		ServerSafetyState.setLastConnectedServer(serverAddress, serverData);

		if (NCRConfig.getServerWhitelist().isWhitelisted(serverAddress)) {
			if (!NCRConfig.getClient().doSigningCheck(serverAddress)) {
				ServerSafetyState.setAllowsUnsafeServer(true);
			}
		}

		if (NCRConfig.getCommon().enableDebugLog()) {
			NoChatReports.LOGGER.info("Connecting to: {}, will expose public key: {}",
					serverAddress.getHost() + ":" + serverAddress.getPort(),
					ServerSafetyState.allowsUnsafeServer());
			NoChatReports.LOGGER.info("Server Data IP: {}", serverData != null ? serverData.ip : "null");
		}
	}

}
