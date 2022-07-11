package com.aizistral.nochatreports.mixins.client;

import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.ProfileKeyPairManager;
import net.minecraft.util.Signer;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.core.ServerSafetyState;

import java.util.Optional;

@Mixin(ProfileKeyPairManager.class)
public class MixinProfileKeyPairManager {

	/**
	 * @reason We don't have to send our keys anywhere, and thus we won't.
	 * @author Aizistral (Overwrite)
	 * @author Aven (Inject)
	 */

	@Inject(method = "profilePublicKey", at = @At("HEAD"), cancellable = true)
	private void onProfilePublicKey(CallbackInfoReturnable<Optional<ProfilePublicKey>> info) {
		if (!ServerSafetyState.allowsUnsafeServer()) {
			info.setReturnValue(Optional.empty());
		}
	}

	/**
	 * @reason We don't have to send our keys anywhere, and thus we won't.
	 * @author Aizistral (Overwrite)
	 * @author Aven (Inject)
	 */

	@Inject(method = "profilePublicKeyData", at = @At("HEAD"), cancellable = true)
	private void onProfilePublicKeyData(CallbackInfoReturnable<Optional<ProfilePublicKey.Data>> info) {
		if (!ServerSafetyState.allowsUnsafeServer()) {
			info.setReturnValue(Optional.empty());
		}
	}

	/**
	 * @reason Prevent client from trying to produce signature in
	 * {@link ClientHandshakePacketListenerImpl#handleHello}.
	 * @author Aizistral (Overwrite)
	 * @author Aven (Inject)
	 */

	@Inject(method = "signer", at = @At("HEAD"), cancellable = true)
	private void onSigner(CallbackInfoReturnable<Optional<Signer>> info) {
		if (!ServerSafetyState.allowsUnsafeServer()) {
			info.setReturnValue(null);
		}
	}

}
