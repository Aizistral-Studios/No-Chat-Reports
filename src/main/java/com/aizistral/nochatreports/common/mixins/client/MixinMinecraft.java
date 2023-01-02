package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.common.NCRClient;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerSafetyState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.multiplayer.ProfileKeyPairManager;

@Mixin(Minecraft.class)
public class MixinMinecraft {
	@Shadow @Final
	private ProfileKeyPairManager profileKeyPairManager;

	@Inject(method = "<init>(Lnet/minecraft/client/main/GameConfig;)V", at = @At("RETURN"))
	private void onConstructed(GameConfig config, CallbackInfo info) {
		NCRClient.setSigningKeysPresent(this.profileKeyPairManager.prepareKeyPair()
				.join().isPresent());
	}

	@Inject(method = "getProfileKeyPairManager", at = @At("HEAD"), cancellable = true)
	private void onGetProfileKeyPairManager(CallbackInfoReturnable<ProfileKeyPairManager> info) {
		if (!NCRConfig.getClient().enableMod())
			return;

		if (!ServerSafetyState.allowChatSigning()) {
			info.setReturnValue(ProfileKeyPairManager.EMPTY_KEY_MANAGER);
		}
	}

	@Inject(method = "allowsTelemetry", at = @At("HEAD"), cancellable = true)
	private void onAllowsTelemetry(CallbackInfoReturnable<Boolean> info) {
		if (NCRConfig.getClient().disableTelemetry()) {
			info.setReturnValue(false);
		}
	}

}
