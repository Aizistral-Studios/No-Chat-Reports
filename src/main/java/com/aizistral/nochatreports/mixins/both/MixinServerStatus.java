package com.aizistral.nochatreports.mixins.both;

import com.aizistral.nochatreports.core.NoChatReportingOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.core.NoReportsConfig;
import net.minecraft.network.protocol.status.ServerStatus;

@Mixin(ServerStatus.class)
public class MixinServerStatus implements NoChatReportingOption {

	private boolean hasNoChatReporting; // Only used by the client to store the info for the ServerData

	@Inject(method = "enforcesSecureChat", at = @At("HEAD"), cancellable = true)
	public void onSecureChatCheck(CallbackInfoReturnable<Boolean> info) {
		if (NoReportsConfig.convertToGameMessage()) {
			info.setReturnValue(true);
		}
	}

	@Override
	public boolean hasNoChatReporting() {
		return this.hasNoChatReporting;
	}

	@Override
	public void setNoChatReporting(boolean state) {
		this.hasNoChatReporting = state;
	}
}
