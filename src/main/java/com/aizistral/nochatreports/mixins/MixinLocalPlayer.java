package com.aizistral.nochatreports.mixins;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.mojang.brigadier.ParseResults;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ArgumentSignatures;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.MessageSigner;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {

	/**
	 * @reason Never sign messages, so that neither server nor other clients have
	 * proof of them being sent from your account.
	 * @author Aizistral
	 */

	@Overwrite
	private MessageSignature signMessage(MessageSigner signer, Component message) {
		return MessageSignature.unsigned();
	}

	/**
	 * @reason Same as above, except commands mostly concern only server.
	 * @author Aizistral
	 */

	@Overwrite
	private ArgumentSignatures signCommandArguments(MessageSigner signer, ParseResults<SharedSuggestionProvider> results, @Nullable Component component) {
		return ArgumentSignatures.empty();
	}

}
