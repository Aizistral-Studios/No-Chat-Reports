package com.aizistral.nochatreports.mixins.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.core.ServerDataExtension;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;

@Mixin(targets = "net/minecraft/client/multiplayer/ServerStatusPinger$1")
public class MixinServerStatusPinger$1 {
	@Shadow @Final
	private ServerData val$data;

	/**
	 * @reason Ensure "preventsChatReports" property is transferred from {@link ServerStatus} to
	 * {@link ServerData} when handling status response.
	 * @author fxmorin (original implementation)
	 * @author Aizistral (current version)
	 */

	@Inject(method = "handleStatusResponse(Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/status/ServerStatus;getPlayers()Lnet/minecraft/network/protocol/status/ServerStatus$Players;", ordinal = 0, shift = At.Shift.BEFORE))
	private void getNoChatReports(ClientboundStatusResponsePacket packet, CallbackInfo info) {
		boolean preventsReports = ((ServerDataExtension) packet.getStatus()).preventsChatReports();
		((ServerDataExtension) this.val$data).setPreventsChatReports(preventsReports);

		if (NCRConfig.getCommon().enableDebugLog()) {
			NoChatReports.LOGGER.info("Received status response packet from server, preventsChatReports: {}",
					preventsReports);
		}

	}

}
