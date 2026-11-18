package com.aizistral.nochatreports.common.mixins.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.commands.arguments.ArgumentSignatures;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundChatCommandSignedPacket;

@Mixin(ServerboundChatCommandSignedPacket.class)
public class MixinServerboundChatCommandSignedPacket {

	/**
	 * @reason Same as {@link MixinServerboundChatPacket#onHandle}.
	 * @author Aizistral
	 */

	@Inject(method = "handle(Lnet/minecraft/network/protocol/game/ServerGamePacketListener;)V", at = @At("HEAD"), cancellable = true)
	private void onHandle(ServerGamePacketListener listener, CallbackInfo info) {
		ServerboundChatCommandSignedPacket packet = (ServerboundChatCommandSignedPacket) (Object) this;

		if (!packet.argumentSignatures().entries().isEmpty()) {
			info.cancel();
			listener.handleSignedChatCommand(new ServerboundChatCommandSignedPacket(packet.command(),
					packet.timeStamp(), packet.salt(), ArgumentSignatures.EMPTY, packet.lastSeenMessages()));
		}
	}

}
