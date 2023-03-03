package com.aizistral.nochatreports.common.mixins.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.dedicated.DedicatedServer;

@Mixin(DedicatedServer.class)
public class MixinDedicatedServer {

	/**
	 * @reason If mod is installed on server - it does the exact opposite of what this option is
	 * designed to enforce, so there's no reason to have it enabled.
	 * @author Aizistral
	 */

	@Inject(method = "enforceSecureProfile", at = @At("RETURN"), cancellable = true)
	private void onEnforceSecureProfile(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(false);
	}

}