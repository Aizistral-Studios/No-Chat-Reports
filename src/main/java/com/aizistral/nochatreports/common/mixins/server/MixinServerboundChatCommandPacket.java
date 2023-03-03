package com.aizistral.nochatreports.common.mixins.server;

import net.minecraft.network.message.ArgumentSignatureDataMap;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CommandExecutionC2SPacket.class)
public class MixinServerboundChatCommandPacket {

	/**
	 * @reason Same as {@link MixinServerboundChatPacket#onGetSignature}.
	 * @author Aizistral
	 */

	@Inject(method = "argumentSignatures", at = @At("RETURN"), cancellable = true)
	private void onGetSignatures(CallbackInfoReturnable<ArgumentSignatureDataMap> info) {
		info.setReturnValue(ArgumentSignatureDataMap.EMPTY);
	}

}
