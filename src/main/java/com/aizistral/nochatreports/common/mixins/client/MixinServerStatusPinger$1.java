package com.aizistral.nochatreports.common.mixins.client;

import java.net.InetSocketAddress;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerDataExtension;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerStatusPinger;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;

@Mixin(targets = "net/minecraft/client/multiplayer/ServerStatusPinger$1")
public abstract class MixinServerStatusPinger$1 {

	@Unique
	private ServerDataExtension nochatreports$serverData;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void captureServerData(
			ServerStatusPinger pinger,
			Connection connection,
			ServerData data,
			Runnable runnableA,
			Runnable runnableB,
			InetSocketAddress socketAddress,
			ServerAddress serverAddress,
			CallbackInfo info) {
		this.nochatreports$serverData = (ServerDataExtension) data;
	}

	/**
	 * @reason Ensure "preventsChatReports" property is transferred from {@link ServerStatus} to
	 * {@link ServerData} when handling status response.
	 * @author fxmorin (original implementation)
	 * @author Aizistral (current version)
	 */

	@Inject(method = "handleStatusResponse(Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/status/ServerStatus;"
					+ "description()Lnet/minecraft/network/chat/Component;",
					ordinal = 0, shift = At.Shift.BEFORE))
	private void getNoChatReports(ClientboundStatusResponsePacket packet, CallbackInfo info) {
		if (this.nochatreports$serverData == null) {
			NCRCore.LOGGER.error("Failed to capture ServerData instance in MixinServerStatusPinger$1!");
			return;
		}

		boolean preventsReports = ((ServerDataExtension) (Object) packet.status()).preventsChatReports();
		this.nochatreports$serverData.setPreventsChatReports(preventsReports);

		if (NCRConfig.getCommon().enableDebugLog()) {
			NCRCore.LOGGER.info("Received status response packet from server, preventsChatReports: {}",
					preventsReports);
		}
	}

	static {
		if (NCRConfig.getCommon().enableDebugLog()) {
			NCRCore.LOGGER.info("Common mixin into ServerStatusPinger$1 succeeded.");
		}
	}

}
