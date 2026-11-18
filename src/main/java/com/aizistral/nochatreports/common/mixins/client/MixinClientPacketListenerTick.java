package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.core.ServerSafetyState;

import net.minecraft.client.multiplayer.ClientPacketListener;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListenerTick {

	@Inject(method = "tick()V", at = @At("HEAD"))
	private void onTick(CallbackInfo info) {
		ServerSafetyState.tickSigningActions((ClientPacketListener) (Object) this);
	}

}
