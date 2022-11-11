package com.aizistral.nochatreports.debug.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.aizistral.nochatreports.debug.NoChatReportsDebug;
import com.mojang.authlib.minecraft.BanDetails;

import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MixinMinecraft {
	/**
	 * Replaces real ban screen with a fake one.
	 * @return Fake ban screen
	 * @reason Debugging
	 * @author Aizistral
	 */
	@Overwrite
	public BanDetails multiplayerBan() {
		return NoChatReportsDebug.getFakeBan();
	}

}
