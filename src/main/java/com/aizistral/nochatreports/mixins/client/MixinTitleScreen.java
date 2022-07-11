package com.aizistral.nochatreports.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.bridge.game.GameVersion;

import net.minecraft.WorldVersion;
import net.minecraft.client.gui.screens.TitleScreen;

@Mixin(TitleScreen.class)
public class MixinTitleScreen {

	/**
	 * @reason Replace "1.19.1" with "1.19.84" in title screen. No functional purpose.
	 * @author Aizistral
	 */

	@Redirect(method = "render", at = @At(value = "INVOKE", remap = true, target =
			"Lnet/minecraft/WorldVersion;getName()Ljava/lang/String;"), require = 0)
	private String onGetVersionName(WorldVersion version) {
		String original = version.getName();
		return original.contains("1.19.1") ? original.replace("1.19.1", "1.19.84") : original;
	}

}
