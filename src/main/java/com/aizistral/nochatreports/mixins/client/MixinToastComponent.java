package com.aizistral.nochatreports.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.config.NoReportsConfig;

import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.SystemToast.SystemToastIds;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;

@Mixin(ToastComponent.class)
public class MixinToastComponent {

	@Inject(method = "addToast", at = @At("HEAD"), cancellable = true)
	private void onAddToast(Toast toast, CallbackInfo info) {
		if (NoReportsConfig.hideWarningToast())
			if (toast instanceof SystemToast sys && sys.getToken() == SystemToastIds.UNSECURE_SERVER_WARNING) {
				info.cancel();
			}
	}

}
