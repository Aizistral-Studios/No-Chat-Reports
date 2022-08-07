package com.aizistral.nochatreports.mixins.client;

import com.aizistral.nochatreports.core.ServerSafetyState;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.ProfileKeyPairManager;
import net.minecraft.util.Signer;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ProfileKeyPairManager.class)
public class MixinProfileKeyPairManager {

	/**
	 * @reason Prevent the client from exposing its public key, unless it is necessary
	 * and user agreed to do so.
	 * @author Aizistral
	 */

	@Inject(method = "profilePublicKey", at = @At("HEAD"), cancellable = true)
	private void onProfilePublicKey(CallbackInfoReturnable<Optional<ProfilePublicKey>> info) {
		if (!ServerSafetyState.allowsUnsafeServer()) {
			info.setReturnValue(Optional.empty());
		}
	}

	/**
	 * @reason Prevent the client from exposing its public key, unless it is necessary
	 * and user agreed to do so.
	 * @author Aizistral
	 */

	@Inject(method = "parsePublicKey", at = @At("HEAD"), cancellable = true)
	private static void onProfilePublicKeyData(CallbackInfoReturnable<Optional<ProfilePublicKey.Data>> info) {
		if (!ServerSafetyState.allowsUnsafeServer()) {
			info.setReturnValue(null);
		}
	}

	/**
	 * @reason Prevent client from trying to produce signature in
	 * {@link ClientHandshakePacketListenerImpl#handleHello}.
	 * @author Aizistral
	 */

	@Inject(method = "signer", at = @At("HEAD"), cancellable = true)
	private void onSigner(CallbackInfoReturnable<Optional<Signer>> info) {
		if (!ServerSafetyState.allowsUnsafeServer()) {
			info.setReturnValue(null);
		}
	}

}
