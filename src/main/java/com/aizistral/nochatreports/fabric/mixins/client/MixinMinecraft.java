package com.aizistral.nochatreports.fabric.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.NCRCore;

import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MixinMinecraft {

	@Inject(method = "run", at = @At("HEAD"))
	private void onRun(CallbackInfo info) {
		NCRCore.LOGGER.info("Game entered main loop!");
	}

}
