package com.aizistral.nochatreports.common.mixins.client;

import java.net.InetSocketAddress;

import net.minecraft.client.multiplayer.ServerStatusPinger;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.Connection;
import net.minecraft.server.network.EventLoopGroupHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerDataExtension;
import com.aizistral.nochatreports.common.core.ServerStatusCache;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;


/**
 * This one ensures "preventsChatReports" property is transferred from {@link ServerStatus} to
 * {@link ServerData} when handling status response.
 *
 * @author fxmorin (original implementation)
 * @author Aizistral (current version)
 * @author pietro-lopes (fixed https://github.com/Aizistral-Studios/No-Chat-Reports/issues/481)
 */

@Mixin(targets = "net/minecraft/client/multiplayer/ServerStatusPinger$1")
public abstract class MixinServerStatusPinger$1 {

	@Unique
	private ServerDataExtension nochatreports$serverData;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void captureServerData(ServerStatusPinger pinger, Connection connection, ServerData data, CallbackInfo info) {
		try {
			this.nochatreports$serverData = (ServerDataExtension) data;
		} catch (Throwable ex) {
			NCRCore.LOGGER.error("Failed to capture server data in MixinServerStatusPinger$1!", ex);
		}
	}

	@Inject(method = "handleStatusResponse(Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/status/ServerStatus;"
					+ "description()Lnet/minecraft/network/chat/Component;"))
	private void getNoChatReports(ClientboundStatusResponsePacket packet, CallbackInfo info) {
		try {
			ServerStatus status = packet.status();
			boolean preventsReports = ServerStatusCache.doesPreventReports();

			if (status.version().isPresent()) {
				ServerStatus.Version version = status.version().get();

				if (version.protocol() < 759 && version.protocol() > 0)  {
					preventsReports = true;
				}
			}

			if (this.nochatreports$serverData == null) {
				NCRCore.LOGGER.error("Failed to capture ServerData instance in MixinServerStatusPinger$1!");
				NCRCore.LOGGER.catching(new IllegalStateException());
				return;
			}

			this.nochatreports$serverData.setPreventsChatReports(preventsReports);

			if (NCRConfig.getCommon().enableDebugLog()) {
				NCRCore.LOGGER.info("Received status response packet from server, preventsChatReports: {}",
						preventsReports);
			}
		} catch (Throwable ex) {
			NCRCore.LOGGER.error("Failed to handle status response in MixinServerStatusPinger$1!", ex);
		}
	}

	static {
		if (NCRConfig.getCommon().enableDebugLog()) {
			NCRCore.LOGGER.info("Common mixin into ServerStatusPinger$1 succeeded.");
		}
	}

}
