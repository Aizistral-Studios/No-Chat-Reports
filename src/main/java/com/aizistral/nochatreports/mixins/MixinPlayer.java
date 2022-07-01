package com.aizistral.nochatreports.mixins;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class MixinPlayer {

	/**
	 * @reason Prevents server from trying to validate signatures on incoming
	 * chat messages. Also prevents it from relaying public keys to other clients.
	 * @author Aizistral (Overwrite)
	 * @author Aven (Inject)
	 */

	@Inject(method = "getProfilePublicKey", at = @At("HEAD"), cancellable = true)
	private void onGetProfileKey(CallbackInfoReturnable<ProfilePublicKey> info) {
		info.setReturnValue(null);
	}

}
