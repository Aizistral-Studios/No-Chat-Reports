package com.aizistral.nochatreports.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.PlayerChatMessage;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

	/**
	 * @reason Prevents client from trying to validate chat messages relayed
	 * by the server. They will come unsigned as in good old days, so no point.
	 * @author Aizistral (Overwrite)
	 * @author Aven (Inject)
	 */

	@Inject(method = "hasValidSignature", at = @At("TAIL"), cancellable = true)
	private void preventServerValidataion(PlayerChatMessage playerChatMessage, PlayerInfo playerInfo, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
	}
}
