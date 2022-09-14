package com.aizistral.nochatreports.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.core.ServerSafetyState;

import net.minecraft.network.protocol.login.ClientLoginPacketListener;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;

@Mixin(ClientboundHelloPacket.class)
public class MixinClientboundHelloPacket {

	/**
	 * @reason If server is operated in the offline mode - we will never receive this type of packet
	 * from it, which allows us to conclude we have no necessity to expose account's public key.
	 * By acknowledging this we can update current {@link ServerSafetyLevel} accordingly.
	 * @author Aizistral
	 */

	@Inject(method = "handle", at = @At("HEAD"))
	private void onHandle(ClientLoginPacketListener listener, CallbackInfo info) {
		if (!NCRConfig.getClient().enableMod())
			return;

		ServerSafetyState.setSessionRequestedKey(true);
	}

}
