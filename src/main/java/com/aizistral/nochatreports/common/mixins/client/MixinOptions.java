
package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;

@Mixin(Options.class)
public class MixinOptions {
	private static final Component SECURE_CHAT_TOOLTIP = Component.translatable("gui.nochatreports.secure_chat");
	private OptionInstance<Boolean> alternativeOption;

	/**
	 * @reason Disable secure chat option, since it will not work anyways.
	 * @author Aizistral
	 */

	@Inject(method = "onlyShowSecureChat", at = @At("RETURN"), cancellable = true)
	private void onlyShowSecureChat(CallbackInfoReturnable<OptionInstance<Boolean>> cir) {
		if (!NCRConfig.getClient().enableMod())
			return;

		if (this.alternativeOption == null) {
			this.alternativeOption = new OptionInstance<>("options.onlyShowSecureChat",
					OptionInstance.cachedConstantTooltip(SECURE_CHAT_TOOLTIP),
					OptionInstance.BOOLEAN_TO_STRING,
					new OptionInstance.Enum<>(ImmutableList.of(Boolean.FALSE), Codec.BOOL),
					false, value -> {});
		}

		cir.setReturnValue(this.alternativeOption);
	}

}
