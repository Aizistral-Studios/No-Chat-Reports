package com.aizistral.nochatreports.mixins.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundChatSessionUpdatePacket;

@Mixin(ServerboundChatSessionUpdatePacket.class)
public class MixinServerboundChatSessionUpdatePacket {

	/**
	 * @reason Ignore chat sessions from client, since we throw away signatures anyways.
	 * @author Aizistral
	 */

	@Inject(method = "handle", at = @At("HEAD"), cancellable = true)
	private void onHandle(ServerGamePacketListener listener, CallbackInfo info) {
		info.cancel();
	}

}
