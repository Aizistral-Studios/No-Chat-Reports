package com.aizistral.nochatreports.common.mixins.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerDataExtension;

import net.minecraft.network.protocol.status.ServerStatus;

@Mixin(ServerStatus.class)
public class MixinServerStatus implements ServerDataExtension {

	/**
	 * Special additional variable that allows the client to know whether server prevents chat reports
	 * upon pinging it.
	 */

	private boolean preventsChatReports;

	/**
	 * @reason Spoof the value of "enforcesSecureChat" in case conversion to system messages is enabled.
	 * There is no way for client to verify the value of the option in such case, so that's one less
	 * annoying warning.
	 * @author Aizistral
	 */

	@Inject(method = "enforcesSecureChat", at = @At("HEAD"), cancellable = true)
	public void onSecureChatCheck(CallbackInfoReturnable<Boolean> info) {
		if (NCRConfig.getCommon().convertToGameMessage()) {
			info.setReturnValue(true);
		}
	}

	@Override
	public boolean preventsChatReports() {
		var self = (ServerStatus) (Object) this;

		if (self.version().isPresent() && self.version().get().protocol() < 759
				&& self.version().get().protocol() > 0)
			return true;

		return this.preventsChatReports;
	}

	@Override
	public void setPreventsChatReports(boolean prevents) {
		this.preventsChatReports = prevents;
	}

}
