package com.aizistral.nochatreports.mixins.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.config.NoReportsConfig;
import com.aizistral.nochatreports.core.NoChatReportingOption;

import net.minecraft.network.protocol.game.ClientboundServerDataPacket;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Mixin(ServerStatus.class)
public class MixinServerStatus implements NoChatReportingOption {
	private boolean hasNoChatReporting;

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
