package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.common.config.NCRConfig;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.Widget;

@Mixin(GridWidget.Adder.class)
public abstract class MixinRowHelper {

	@Inject(method = "addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)"
			+ "Lnet/minecraft/client/gui/components/AbstractWidget;", at = @At("HEAD"), cancellable = true)
	private void onAddChild(Widget element, CallbackInfoReturnable<ClickableWidget> info) {
		if (NCRConfig.getClient().disableTelemetry() && NCRConfig.getClient().removeTelemetryButton())
			if (element instanceof ButtonWidget button)
				if (OptionsScreen.TELEMETRY_TEXT.equals(button.getMessage())) {
					info.setReturnValue(button);
				}
	}

}
