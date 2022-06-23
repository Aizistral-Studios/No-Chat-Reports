package com.aizistral.nochatreports.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import com.aizistral.nochatreports.NoBanUserApiService;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MixinMinecraft {
	@Inject(method = "createUserApiService", at = @At("RETURN"), cancellable = true)
	private void wrapUserApiService(YggdrasilAuthenticationService authService, GameConfig config, CallbackInfoReturnable<UserApiService> cir) {
		cir.setReturnValue(new NoBanUserApiService(cir.getReturnValue()));
	}
}
