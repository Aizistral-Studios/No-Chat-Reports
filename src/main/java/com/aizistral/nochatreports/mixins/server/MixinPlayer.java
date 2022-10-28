package com.aizistral.nochatreports.mixins.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;

@Mixin(Player.class)
public class MixinPlayer {

	/**
	 * @reason Prevents server from trying to validate signatures on incoming
	 * chat messages. Also prevents it from relaying public keys to other clients.
	 * @author Aizistral
	 */

	@Inject(method = "getProfilePublicKey", at = @At("HEAD"), cancellable = true)
	private void onGetProfileKey(CallbackInfoReturnable<ProfilePublicKey> info) {
		info.setReturnValue(null);
	}

}
