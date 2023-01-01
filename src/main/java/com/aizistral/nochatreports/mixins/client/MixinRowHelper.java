package com.aizistral.nochatreports.mixins.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.config.NCRConfig;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.GridWidget;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Mixin(GridWidget.RowHelper.class)
public abstract class MixinRowHelper {

	@Inject(method = "addChild(Lnet/minecraft/client/gui/components/AbstractWidget;)"
			+ "Lnet/minecraft/client/gui/components/AbstractWidget;", at = @At("HEAD"), cancellable = true)
	private void onAddChild(AbstractWidget widget, CallbackInfoReturnable<AbstractWidget> info) {
		if (NCRConfig.getClient().disableTelemetry() && NCRConfig.getClient().removeTelemetryButton())
			if (OptionsScreen.TELEMETRY.equals(widget.getMessage())) {
				info.setReturnValue(widget);
			}
	}

}
