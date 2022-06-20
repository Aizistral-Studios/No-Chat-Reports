package com.aizistral.nochatreports.mixins;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.multiplayer.ProfileKeyPairManager;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;
import net.minecraft.util.Signer;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;

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
	 * @author Aizistral
	 */

	@Overwrite
	public Signer signer() {
		return null;
	}

}
