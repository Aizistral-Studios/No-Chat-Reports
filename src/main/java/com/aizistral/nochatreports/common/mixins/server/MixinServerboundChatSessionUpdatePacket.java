package com.aizistral.nochatreports.common.mixins.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.config.NCRConfig;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.PlayerSessionC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;

@Mixin(PlayerSessionC2SPacket.class)
public class MixinServerboundChatSessionUpdatePacket {

	/**
	 * @reason Ignore chat sessions from client, since we throw away signatures anyways.
	 * @author Aizistral
	 */

	@Inject(method = "handle", at = @At("HEAD"), cancellable = true)
	private void onHandle(ServerPlayPacketListener listener, CallbackInfo info) {
		var impl = (ServerPlayNetworkHandler) listener;

		if (!impl.getPlayer().getServer().isHost(impl.getPlayer().getGameProfile())) {
			if (NCRConfig.getCommon().demandOnClient()) {
				impl.disconnect(Text.literal(NCRConfig.getCommon().demandOnClientMessage()));
			}
		}

		info.cancel();
	}

}
