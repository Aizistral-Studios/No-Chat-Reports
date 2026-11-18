package com.aizistral.nochatreports.common.mixins.server;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundChatPacket;

@Mixin(ServerboundChatPacket.class)
public class MixinServerboundChatPacket {

	/**
	 * @reason Strip signatures before relaying messages to other clients, if they arrive signed
	 * This is important for mod to be able to operate in server-only mode without
	 * {@link NCRConfigLegacy#convertToGameMessage()} enabled.
	 * @author Aizistral
	 */

	@Inject(method = "handle(Lnet/minecraft/network/protocol/game/ServerGamePacketListener;)V", at = @At("HEAD"), cancellable = true)
	private void onHandle(ServerGamePacketListener listener, CallbackInfo info) {
		ServerboundChatPacket packet = (ServerboundChatPacket) (Object) this;

		if (packet.signature().isPresent()) {
			info.cancel();
			listener.handleChat(new ServerboundChatPacket(packet.message(), packet.timeStamp(), packet.salt(),
					Optional.empty(), packet.lastSeenMessages()));
		}
	}

}
