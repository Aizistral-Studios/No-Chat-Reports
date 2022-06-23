package com.aizistral.nochatreports.mixins;

import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DedicatedServer.class)
public class MixinDedicatedServer {

	/**
	 * @reason If we are present on server - we won't be using keys for anything,
	 * so this option ceases to make sense.
	 * @author Aizistral (Overwrite)
	 * @author Aven (Inject)
	 */

	@Inject(method = "enforceSecureProfile", at = @At("HEAD"), cancellable = true)
	private void enforceSecureProfile(CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(false);
	}

}
