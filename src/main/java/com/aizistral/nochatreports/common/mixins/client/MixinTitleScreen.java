package com.aizistral.nochatreports.common.mixins.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.CommonComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.gui.RealmsWarningScreen;
import com.mojang.realmsclient.RealmsMainScreen;

@Mixin(TitleScreen.class)
public class MixinTitleScreen extends Screen {

	protected MixinTitleScreen() {
		super(CommonComponents.EMPTY);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = { "method_55814", "lambda$createNormalMenuOptions$11" }, at = @At("HEAD"), cancellable = true)
	private void onRealmsButtonClicked(Button button, CallbackInfo info) {
		if (RealmsWarningScreen.shouldShow()) {
			this.minecraft.setScreen(new RealmsWarningScreen(new TitleScreen(), new RealmsMainScreen(this)));
			info.cancel();
		}
	}
}
