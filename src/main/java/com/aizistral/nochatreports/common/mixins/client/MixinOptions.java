
package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

@Mixin(GameOptions.class)
public class MixinOptions {
	private static final Text SECURE_CHAT_TOOLTIP = Text.translatable("gui.nochatreports.secure_chat");
	private SimpleOption<Boolean> alternativeOption;

	/**
	 * @reason Disable secure chat option, since it will not work anyways.
	 * @author Aizistral
	 */

	@Inject(method = "onlyShowSecureChat", at = @At("RETURN"), cancellable = true)
	private void onlyShowSecureChat(CallbackInfoReturnable<SimpleOption<Boolean>> cir) {
		if (!NCRConfig.getClient().enableMod())
			return;

		if (this.alternativeOption == null) {
			this.alternativeOption = new SimpleOption<>("options.onlyShowSecureChat",
					SimpleOption.constantTooltip(SECURE_CHAT_TOOLTIP),
					SimpleOption.BOOLEAN_TEXT_GETTER,
					new SimpleOption.PotentialValuesBasedCallbacks<>(ImmutableList.of(Boolean.FALSE), Codec.BOOL),
					false, value -> {});
		}

		cir.setReturnValue(this.alternativeOption);
	}

}
