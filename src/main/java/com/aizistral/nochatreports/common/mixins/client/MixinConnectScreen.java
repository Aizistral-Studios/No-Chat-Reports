package com.aizistral.nochatreports.common.mixins.client;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerSafetyState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.TransferState;
import net.minecraft.client.multiplayer.resolver.ServerAddress;

@Mixin(ConnectScreen.class)
public class MixinConnectScreen {

	/**
	 * @reason Intercept initiation of any new server connection. This way we can keep track of which
	 * server client currently tries to connect to.
	 * @author Aizistral
	 */

	@Inject(method = "startConnecting", at = @At("HEAD"))
	private static void onStartConnecting(Screen screen, Minecraft minecraft, ServerAddress serverAddress,
			@Nullable ServerData serverData, boolean quickplay, @Nullable TransferState transferState, CallbackInfo info) {
		if (!NCRConfig.getClient().enableMod())
			return;

		ServerSafetyState.reset();
		ServerSafetyState.setLastServer(serverAddress);

		if (NCRConfig.getCommon().enableDebugLog()) {
			NCRCore.LOGGER.info("Connecting to: {}",
					serverAddress.getHost() + ":" + serverAddress.getPort());
			NCRCore.LOGGER.info("Server Data IP: {}", serverData != null ? serverData.ip : "null");
		}
	}

}
