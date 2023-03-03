package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.config.NCRConfig;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.SystemToast.Type;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;

@Mixin(ToastManager.class)
public class MixinToastComponent {

	@Inject(method = "addToast", at = @At("HEAD"), cancellable = true)
	private void onAddToast(Toast toast, CallbackInfo info) {
		if (NCRConfig.getClient().hideWarningToast())
			if (toast instanceof SystemToast sys && sys.getType() == Type.UNSECURE_SERVER_WARNING) {
				info.cancel();
			}
	}

}
