package com.aizistral.nochatreports.common.mixins.client;

import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.mojang.authlib.minecraft.TelemetrySession;
import com.mojang.authlib.yggdrasil.YggdrasilUserApiService;

@Mixin(value = YggdrasilUserApiService.class, remap = false)
public class MixinYggdrasilUserApiService {

	/**
	 * @reason Privacy.
	 * @author Aizistral
	 */

	@Inject(method = "newTelemetrySession", at = @At("HEAD"), cancellable = true)
	private void onCreateTelemetrySession(Executor executor, CallbackInfoReturnable<TelemetrySession> info) {
		if (NCRConfig.getClient().disableTelemetry()) {
			info.setReturnValue(TelemetrySession.DISABLED);
		}
	}

}
