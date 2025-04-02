package com.aizistral.nochatreports.common.mixins.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.commands.arguments.ArgumentSignatures;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.network.protocol.game.ServerboundChatCommandSignedPacket;

@Mixin(ServerboundChatCommandSignedPacket.class)
public class MixinServerboundChatCommandSignedPacket {

	/**
	 * @reason Same as {@link MixinServerboundChatPacket#onGetSignature}.
	 * @author Aizistral
	 */

	@Inject(method = "argumentSignatures", at = @At("RETURN"), cancellable = true)
	private void onGetSignatures(CallbackInfoReturnable<ArgumentSignatures> info) {
		info.setReturnValue(ArgumentSignatures.EMPTY);
	}

}
