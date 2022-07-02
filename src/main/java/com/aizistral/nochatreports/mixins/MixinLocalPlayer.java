package com.aizistral.nochatreports.mixins;

import com.aizistral.nochatreports.core.ServerSafetyState;
import com.mojang.brigadier.ParseResults;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ArgumentSignatures;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.MessageSigner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {

	/**
	 * @reason Never sign messages, so that neither server nor other clients have
	 * proof of them being sent from your account.
	 * @author Aizistral (Overwrite)
	 * @author Aven (Inject)
	 */

	@Inject(method = "signMessage", at = @At("HEAD"), cancellable = true)
	private void onSignMessage(MessageSigner signer, Component message, CallbackInfoReturnable<MessageSignature> info) {
		if (!ServerSafetyState.forceSignedMessages()) {
			info.setReturnValue(MessageSignature.unsigned());
		}
	}

	/**
	 * @reason Same as above, except commands mostly concern only server.
	 * @author Aizistral (Overwrite)
	 * @author Aven (Inject)
	 */

	@Inject(method = "signCommandArguments", at = @At("HEAD"), cancellable = true)
	private void onSignCommand(MessageSigner signer, ParseResults<SharedSuggestionProvider> results, @Nullable Component component, CallbackInfoReturnable<ArgumentSignatures> info) {
		if (!ServerSafetyState.forceSignedMessages()) {
			info.setReturnValue(ArgumentSignatures.empty());
		}
	}

}
