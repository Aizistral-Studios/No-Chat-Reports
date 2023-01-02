package com.aizistral.nochatreports.mixins.client;

import net.minecraft.client.GuiMessageTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.config.NCRConfig;

@Mixin(GuiMessageTag.class)
public class MixinGuiMessageTag {

	/**
	 * @reason Remove gray bar as indication besides system messages, if mod is configured
	 * respectively. The mod can convert all player messages to system messages when
	 * installed on server, so we don't need that most of the time.
	 * @author Aizistral
	 */

	@Inject(method = { "system", "systemSinglePlayer" }, at = @At("HEAD"), cancellable = true)
	private static void onSystem(CallbackInfoReturnable<GuiMessageTag> info) {
		if (NCRConfig.getClient().hideSystemMessageIndicators()) {
			info.setReturnValue(null);
		}
	}

}
