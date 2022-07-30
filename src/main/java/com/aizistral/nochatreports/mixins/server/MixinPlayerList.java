package com.aizistral.nochatreports.mixins.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.network.chat.ChatSender;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.players.PlayerList;

@Mixin(PlayerList.class)
public class MixinPlayerList {

	/**
	 * @reason Remove "Not Secure" mark in server chat logs.
	 * @author Aizistral
	 */

	@Inject(method = "verifyChatTrusted", at = @At("HEAD"), cancellable = true)
	private void onVerifyChatTrusted(PlayerChatMessage playerChatMessage, ChatSender chatSender, CallbackInfoReturnable<Boolean> info) {
		info.setReturnValue(true);
	}

}
