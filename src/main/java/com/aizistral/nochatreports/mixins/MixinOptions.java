package com.aizistral.nochatreports.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Mixin(Options.class)
public class MixinOptions {
	private OptionInstance<Boolean> alternativeOption;

	/**
	 * @reason Disable secure chat option, since it will not work anyways.
	 * @author Aizistral
	 */

	@Overwrite
	public OptionInstance<Boolean> onlyShowSecureChat() {
		if (this.alternativeOption == null) {
			this.alternativeOption = new OptionInstance<>("options.onlyShowSecureChat",
					OptionInstance.cachedConstantTooltip(Component.translatable("gui.nochatreport.secureChat")),
					(component, value) -> value ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF,
					new OptionInstance.Enum<>(ImmutableList.of(Boolean.FALSE), Codec.BOOL), false, (value) -> {});
		}

		return this.alternativeOption;
	}

}
