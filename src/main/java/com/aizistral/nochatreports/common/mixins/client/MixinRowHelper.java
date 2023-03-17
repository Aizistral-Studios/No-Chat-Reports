package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.common.config.NCRConfig;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.OptionsScreen;

@Mixin(GridLayout.RowHelper.class)
public abstract class MixinRowHelper {

	@Inject(method = "addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)"
			+ "Lnet/minecraft/client/gui/layouts/LayoutElement;", at = @At("HEAD"), cancellable = true)
	private void onAddChild(LayoutElement element, CallbackInfoReturnable<AbstractWidget> info) {
		if (NCRConfig.getClient().disableTelemetry() && NCRConfig.getClient().removeTelemetryButton())
			if (element instanceof Button button)
				if (OptionsScreen.TELEMETRY.equals(button.getMessage())) {
					info.setReturnValue(button);
				}
	}

}
