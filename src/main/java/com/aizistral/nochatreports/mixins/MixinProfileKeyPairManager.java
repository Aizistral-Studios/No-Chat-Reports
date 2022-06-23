package com.aizistral.nochatreports.mixins;

import java.util.Optional;

import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.multiplayer.ProfileKeyPairManager;
import net.minecraft.util.Signer;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProfileKeyPairManager.class)
public class MixinProfileKeyPairManager {

	/**
	 * @reason We don't have to send our keys anywhere, and thus we won't.
	 * @author Aizistral (Overwrite)
	 * @author Aven (Inject)
	 */

	@Inject(method = "profilePublicKey", at = @At("HEAD"), cancellable = true)
	private void preventServerValidation(CallbackInfoReturnable<Optional<ProfilePublicKey>> cir) {
		cir.setReturnValue(Optional.empty());
	}

	/**
	 * @reason We don't have to send our keys anywhere, and thus we won't.
	 * @author Aizistral (Overwrite)
	 * @author Aven (Inject)
	 */

	@Inject(method = "profilePublicKeyData", at = @At("HEAD"), cancellable = true)
	private void dontSendKeys(CallbackInfoReturnable<Optional<ProfilePublicKey.Data>> cir) {
		cir.setReturnValue(Optional.empty());
	}

	/**
	 * @reason Prevent client from trying to produce signature in
	 * {@link ClientHandshakePacketListenerImpl#handleHello}.
	 * @author Aizistral (Overwrite)
	 * @author Aven (Inject)
	 */

	@Inject(method = "signer", at = @At("HEAD"), cancellable = true)
	private void preventSignature(CallbackInfoReturnable<Optional<Signer>> cir) {
		cir.setReturnValue(null);
	}

}
