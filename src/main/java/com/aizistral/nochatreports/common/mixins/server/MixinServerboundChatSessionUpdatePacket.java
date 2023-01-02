package com.aizistral.nochatreports.common.mixins.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.config.NCRConfig;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundChatSessionUpdatePacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Mixin(ServerboundChatSessionUpdatePacket.class)
public class MixinServerboundChatSessionUpdatePacket {

	/**
	 * @reason Ignore chat sessions from client, since we throw away signatures anyways.
	 * @author Aizistral
	 */

	@Inject(method = "handle", at = @At("HEAD"), cancellable = true)
	private void onHandle(ServerGamePacketListener listener, CallbackInfo info) {
		var impl = (ServerGamePacketListenerImpl) listener;

		if (!impl.getPlayer().getServer().isSingleplayerOwner(impl.getPlayer().getGameProfile())) {
			if (NCRConfig.getCommon().demandOnClient()) {
				impl.disconnect(Component.literal(NCRConfig.getCommon().demandOnClientMessage()));
			}
		}

		info.cancel();
	}

}
