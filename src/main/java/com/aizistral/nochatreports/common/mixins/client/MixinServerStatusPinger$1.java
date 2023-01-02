package com.aizistral.nochatreports.common.mixins.client;

import java.lang.reflect.Field;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerDataExtension;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;

@Mixin(targets = "net/minecraft/client/multiplayer/ServerStatusPinger$1")
public class MixinServerStatusPinger$1 {
	private static final Field SERVER_DATA_FIELD;

	static {
		// We use reflection here because Mixin's AP dies when trying to process @Shadow of this lol
		try {
			Class<?> pinger = Class.forName("net.minecraft.client.multiplayer.ServerStatusPinger$1");
			SERVER_DATA_FIELD = pinger.getDeclaredField("val$p_105460_");
			SERVER_DATA_FIELD.setAccessible(true);
		} catch (Exception ex) {
			throw new RuntimeException("Reflection failed in MixinServerStatusPinger$1", ex);
		}
	}

	/**
	 * @reason Ensure "preventsChatReports" property is transferred from {@link ServerStatus} to
	 * {@link ServerData} when handling status response.
	 * @author fxmorin (original implementation)
	 * @author Aizistral (current version)
	 */

	@Inject(method = "handleStatusResponse(Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/status/ServerStatus;getPlayers()Lnet/minecraft/network/protocol/status/ServerStatus$Players;", ordinal = 0, shift = At.Shift.BEFORE))
	private void getNoChatReports(ClientboundStatusResponsePacket packet, CallbackInfo info) {
		try {
			boolean preventsReports = ((ServerDataExtension) packet.getStatus()).preventsChatReports();
			((ServerDataExtension) SERVER_DATA_FIELD.get(this)).setPreventsChatReports(preventsReports);

			if (NCRConfig.getCommon().enableDebugLog()) {
				NCRCore.LOGGER.info("Received status response packet from server, preventsChatReports: {}",
						preventsReports);
			}
		} catch (IllegalArgumentException | IllegalAccessException ex) {
			throw new RuntimeException("Failed to get ServerData field via reflection", ex);
		}
	}

}
