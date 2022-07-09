package com.aizistral.nochatreports.mixins;

import java.time.Instant;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.core.NoReportsConfig;
import com.mojang.authlib.minecraft.BanDetails;
import com.mojang.authlib.minecraft.UserApiService;

import net.fabricmc.fabric.mixin.client.rendering.MixinInGameHud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Minecraft.ChatStatus;
import net.minecraft.world.entity.player.ChatVisiblity;

@Mixin(Minecraft.class)
public class MixinMinecraft {

	@Inject(method = "createTitle", at = @At("RETURN"), cancellable = true, require = 0)
	private void onCreateTitle(CallbackInfoReturnable<String> info) {
		if (info.getReturnValue().contains("1.19.1")) {
			info.setReturnValue(info.getReturnValue().replace("1.19.1", "1.19.84"));
		}
	}

}
