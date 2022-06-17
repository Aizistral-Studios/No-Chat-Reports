package com.aizistral.nochatreports.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;

@Mixin(Player.class)
public class MixinPlayer {

	/**
	 * @reason Prevents server from trying to validate signatures on incoming
	 * chat messages. Also prevents it from relaying public keys to other clients.
	 * @author Aizistral
	 */

	@Overwrite
	public ProfilePublicKey getProfilePublicKey() {
		return null;
	}

}
