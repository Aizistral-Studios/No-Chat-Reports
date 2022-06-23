package com.aizistral.nochatreports.mixins;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerboundChatPacket.class)
public class MixinServerboundChatPacket {

	/**
	 * @reason Strip signatures before relaying messages to other clients,
	 * if they somehow arrive signed. This is important for mod to be able
	 * to operate in server-only mode.
	 * @author Aizistral (Overwrite)
	 * @author Aven (Inject)
	 */

	@Inject(method = "getSignature", at = @At("HEAD"), cancellable = true)
	private void stripSignatures(UUID uUID, CallbackInfoReturnable<MessageSignature> cir) {
		cir.setReturnValue(MessageSignature.unsigned());
	}

}
