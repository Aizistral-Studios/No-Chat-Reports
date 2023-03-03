package com.aizistral.nochatreports.common.mixins.client;

import java.lang.reflect.Field;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.network.packet.s2c.query.QueryResponseS2CPacket;
import net.minecraft.server.ServerMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerDataExtension;
import com.aizistral.nochatreports.common.platform.extensions.ServerPingerExtension;

@Mixin(targets = "net/minecraft/client/multiplayer/ServerStatusPinger$1")
public abstract class MixinServerStatusPinger$1 implements ServerPingerExtension {

	/**
	 * @reason Ensure "preventsChatReports" property is transferred from {@link ServerMetadata} to
	 * {@link ServerInfo} when handling status response.
	 * @author fxmorin (original implementation)
	 * @author Aizistral (current version)
	 */

	@Inject(method = "handleStatusResponse(Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/status/ServerStatus;"
					+ "description()Lnet/minecraft/network/chat/Component;",
					ordinal = 0, shift = At.Shift.BEFORE))
	private void getNoChatReports(QueryResponseS2CPacket packet, CallbackInfo info) {
		boolean preventsReports = ((ServerDataExtension) (Object) packet.metadata()).preventsChatReports();
		((ServerDataExtension) this.getServerData()).setPreventsChatReports(preventsReports);

		if (NCRConfig.getCommon().enableDebugLog()) {
			NCRCore.LOGGER.info("Received status response packet from server, preventsChatReports: {}",
					preventsReports);
		}
	}

	static {
		NCRCore.LOGGER.info("Common mixin into ServerStatusPinger$1 succeeded.");
	}

}
