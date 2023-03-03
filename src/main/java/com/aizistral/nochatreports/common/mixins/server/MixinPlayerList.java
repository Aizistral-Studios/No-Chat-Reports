package com.aizistral.nochatreports.common.mixins.server;

import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerManager.class)
public class MixinPlayerList {

	/**
	 * @reason Remove "Not Secure" mark in server chat logs.
	 * @author Aizistral
	 */

	@Inject(method = "verifyChatTrusted", at = @At("HEAD"), cancellable = true)
	private void onVerifyChatTrusted(SignedMessage playerChatMessage, CallbackInfoReturnable<Boolean> info) {
		info.setReturnValue(true);
	}

}
