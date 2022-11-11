package com.aizistral.nochatreports.mixins.client;

import com.aizistral.nochatreports.gui.RealmsWarningScreen;
import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.CommonComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen extends Screen {

	protected MixinTitleScreen() {
		super(CommonComponents.EMPTY);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = "realmsButtonClicked", at = @At("HEAD"), cancellable = true)
	private void onRealmsButtonClicked(CallbackInfo info) {
		if (RealmsWarningScreen.shouldShow()) {
			this.minecraft.setScreen(new RealmsWarningScreen(new TitleScreen(), new RealmsMainScreen(this)));
			info.cancel();
		}
	}

}
