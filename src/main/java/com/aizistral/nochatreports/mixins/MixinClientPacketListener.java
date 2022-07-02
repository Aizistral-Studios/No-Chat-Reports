package com.aizistral.nochatreports.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.gui.UnsafeServerScreen;

import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onConstructed(CallbackInfo info) {
		var self = (ClientPacketListener) (Object) this;
		if (self.callbackScreen instanceof UnsafeServerScreen) {
			self.callbackScreen = new JoinMultiplayerScreen(new TitleScreen());
		}
	}

}
