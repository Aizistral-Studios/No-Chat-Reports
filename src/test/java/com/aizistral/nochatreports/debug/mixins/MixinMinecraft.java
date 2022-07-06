package com.aizistral.nochatreports.debug.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.aizistral.nochatreports.debug.NoChatReportsDebug;
import com.mojang.authlib.minecraft.BanDetails;

import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MixinMinecraft {

	@Overwrite
	public BanDetails multiplayerBan() {
		return NoChatReportsDebug.getFakeBan();
	}

}
