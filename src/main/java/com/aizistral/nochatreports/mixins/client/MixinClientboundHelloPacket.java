package com.aizistral.nochatreports.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.core.ServerSafetyState;

import net.minecraft.network.protocol.login.ClientLoginPacketListener;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;

@Mixin(ClientboundHelloPacket.class)
public class MixinClientboundHelloPacket {

	@Inject(method = "handle", at = @At("HEAD"))
	private void onHandle(ClientLoginPacketListener listener, CallbackInfo info) {
		ServerSafetyState.setSessionRequestedKey(true);
	}

}
