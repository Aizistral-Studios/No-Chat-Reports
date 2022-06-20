package com.aizistral.nochatreports.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;

@Mixin(DedicatedServer.class)
public class MixinDedicatedServer {

	/**
	 * @reason If we are present on server - we won't be using keys for anything,
	 * so this option ceases to make sense.
	 * @author Aizistral
	 */
	
	@Overwrite
	public boolean enforceSecureProfile() {
		return false;
	}

}
