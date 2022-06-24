package com.aizistral.nochatreports.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.bridge.game.GameVersion;

import net.minecraft.WorldVersion;
import net.minecraft.client.gui.screens.TitleScreen;

@Mixin(TitleScreen.class)
public class MixinTitleScreen {

	@Redirect(method = "render", at = @At(value = "INVOKE", remap = false, target =
			"Lnet/minecraft/WorldVersion;getName()Ljava/lang/String;"), require = 0)
	private String overrideVersionName(WorldVersion version) {
		String original = version.getName();
		return original.equals("1.19.1") ? "1.19.84" : original;
	}

}
