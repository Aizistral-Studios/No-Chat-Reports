package com.aizistral.nochatreports.common.mixins.server;

import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatMessageC2SPacket.class)
public class MixinServerboundChatPacket {

	/**
	 * @reason Strip signatures before relaying messages to other clients, if they arrive signed
	 * This is important for mod to be able to operate in server-only mode without
	 * {@link NCRConfigLegacy#convertToGameMessage()} enabled.
	 * @author Aizistral
	 */

	@Inject(method = "signature", at = @At("RETURN"), cancellable = true)
	private void onGetSignature(CallbackInfoReturnable<MessageSignatureData> info) {
		info.setReturnValue(null);
	}

}
