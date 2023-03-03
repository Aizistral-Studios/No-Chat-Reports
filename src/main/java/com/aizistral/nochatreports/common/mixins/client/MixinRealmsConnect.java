package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerSafetyLevel;
import com.aizistral.nochatreports.common.core.ServerSafetyState;
import com.mojang.realmsclient.dto.RealmsServer;

import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.realms.RealmsConnect;

@Mixin(RealmsConnect.class)
public class MixinRealmsConnect {

	/**
	 * @reason Always treat Realms servers as insecure, there is nothing we can do about them.
	 * @author Aizistral
	 */

	@Inject(method = "connect", at = @At("HEAD"))
	private void onConnect(RealmsServer realmsServer, ServerAddress serverAddress, CallbackInfo info) {
		if (!NCRConfig.getClient().enableMod())
			return;

		ServerSafetyState.reset();
		ServerSafetyState.updateCurrent(ServerSafetyLevel.REALMS);
		ServerSafetyState.setAllowChatSigning(true);
	}

}
