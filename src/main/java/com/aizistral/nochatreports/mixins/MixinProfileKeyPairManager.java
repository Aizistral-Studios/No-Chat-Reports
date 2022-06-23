package com.aizistral.nochatreports.mixins;

import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.ProfileKeyPairManager;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;
import net.minecraft.util.Signer;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ProfileKeyPairManager.class)
public class MixinProfileKeyPairManager {

	/**
	 * @reason We don't have to send our keys anywhere, and thus we won't.
	 * @author Aizistral
	 */	
	
	@Overwrite
	public Optional<ProfilePublicKey> profilePublicKey() {
		return Optional.empty();
	}
	
	/**
	 * @reason We don't have to send our keys anywhere, and thus we won't.
	 * @author Aizistral
	 */

	@Overwrite
	public Optional<ProfilePublicKey.Data> profilePublicKeyData() {
		return Optional.empty();
	}
	
	/**
	 * @reason Prevent client from trying to produce signature in 
	 * {@link ClientHandshakePacketListenerImpl#handleHello(ClientboundHelloPacket)}.
	 * @author Aizistral (Overwrite)
	 * @author Aven (Inject)
	 */

	@Inject(method = "signer", at = @At("HEAD"), cancellable = true)
	private void signer(CallbackInfoReturnable<Signer> cir) {
		cir.setReturnValue(null);
	}


}
