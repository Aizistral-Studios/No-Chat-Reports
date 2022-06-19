package com.aizistral.nochatreports.mixins;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.client.multiplayer.ProfileKeyPairManager;
import net.minecraft.world.entity.player.ProfilePublicKey;

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

}
