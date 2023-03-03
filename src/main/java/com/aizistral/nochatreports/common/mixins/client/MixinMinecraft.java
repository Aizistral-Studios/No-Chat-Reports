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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.util.ProfileKeys;

@Mixin(MinecraftClient.class)
public class MixinMinecraft {
	@Shadow @Final
	private ProfileKeys profileKeyPairManager;

	@Inject(method = "<init>(Lnet/minecraft/client/main/GameConfig;)V", at = @At("RETURN"))
	private void onConstructed(RunArgs config, CallbackInfo info) {
		NCRClient.setSigningKeysPresent(this.profileKeyPairManager.fetchKeyPair()
				.join().isPresent());
	}

	@Inject(method = "getProfileKeyPairManager", at = @At("HEAD"), cancellable = true)
	private void onGetProfileKeyPairManager(CallbackInfoReturnable<ProfileKeys> info) {
		if (!NCRConfig.getClient().enableMod())
			return;

		if (!ServerSafetyState.allowChatSigning()) {
			info.setReturnValue(ProfileKeys.MISSING);
		}
	}

	@Inject(method = "allowsTelemetry", at = @At("HEAD"), cancellable = true)
	private void onAllowsTelemetry(CallbackInfoReturnable<Boolean> info) {
		if (NCRConfig.getClient().disableTelemetry()) {
			info.setReturnValue(false);
		}
	}

}
