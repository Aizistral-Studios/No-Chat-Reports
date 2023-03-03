package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.gui.RealmsWarningScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.screen.ScreenTexts;

@Mixin(TitleScreen.class)
public class MixinTitleScreen extends Screen {

	protected MixinTitleScreen() {
		super(ScreenTexts.EMPTY);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = "realmsButtonClicked", at = @At("HEAD"), cancellable = true)
	private void onRealmsButtonClicked(CallbackInfo info) {
		if (RealmsWarningScreen.shouldShow()) {
			this.client.setScreen(new RealmsWarningScreen(new TitleScreen(), new RealmsMainScreen(this)));
			info.cancel();
		}
	}

}
