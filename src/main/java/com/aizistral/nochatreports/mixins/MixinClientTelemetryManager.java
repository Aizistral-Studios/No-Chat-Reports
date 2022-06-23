package com.aizistral.nochatreports.mixins;

import com.mojang.authlib.minecraft.TelemetrySession;
import com.mojang.authlib.minecraft.UserApiService;
import net.minecraft.client.ClientTelemetryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.Executor;

@Mixin(ClientTelemetryManager.class)
public class MixinClientTelemetryManager {

	/**
	 * @reason Privacy.
	 * @author Aizistral
	 */

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/authlib/minecraft/UserApiService;"
			+ "newTelemetrySession(Ljava/util/concurrent/Executor;)Lcom/mojang/authlib/minecraft/TelemetrySession;",
			remap = false))
	private TelemetrySession redirectNewTelemetrySession(UserApiService service, Executor executor) {
		return TelemetrySession.DISABLED;
	}

}