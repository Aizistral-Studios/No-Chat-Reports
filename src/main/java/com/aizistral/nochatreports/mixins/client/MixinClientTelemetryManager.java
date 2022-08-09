package com.aizistral.nochatreports.mixins.client;

import com.aizistral.nochatreports.core.NoReportsConfig;
import com.mojang.authlib.minecraft.TelemetrySession;
import com.mojang.authlib.minecraft.UserApiService;

import net.minecraft.client.ClientTelemetryManager;

import java.util.concurrent.Executor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientTelemetryManager.class)
public class MixinClientTelemetryManager {

	/**
	 * @reason Privacy.
	 * @author Aizistral
	 */

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/authlib/minecraft/UserApiService;"
			+ "newTelemetrySession(Ljava/util/concurrent/Executor;)Lcom/mojang/authlib/minecraft/TelemetrySession;",
			remap = false))
	private TelemetrySession onCreateTelemetrySession(UserApiService service, Executor executor) {
		if (NoReportsConfig.disableTelemetry())
			return TelemetrySession.DISABLED;
		else
			return service.newTelemetrySession(executor);
	}

}
