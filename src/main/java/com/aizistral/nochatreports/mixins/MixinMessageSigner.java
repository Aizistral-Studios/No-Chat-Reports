package com.aizistral.nochatreports.mixins;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.core.NoReportsConfig;
import com.aizistral.nochatreports.core.TimestampScrambler;

import net.minecraft.network.chat.MessageSigner;

@Mixin(MessageSigner.class)
public class MixinMessageSigner {

	@Inject(method = "create", at = @At("RETURN"), cancellable = true)
	private static void onCreate(UUID id, CallbackInfoReturnable<MessageSigner> info) {
		if (NoReportsConfig.scrambleTimestamps()) {
			info.setReturnValue(TimestampScrambler.randomizeTimestamp(info.getReturnValue()));
		}
	}

}
