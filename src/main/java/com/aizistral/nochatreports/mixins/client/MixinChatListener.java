package com.aizistral.nochatreports.mixins.client;

import java.time.Instant;
import java.util.Base64;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.core.NoReportsConfig;

import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.client.multiplayer.chat.ChatTrustLevel;
import net.minecraft.network.chat.ChatSender;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;

@Mixin(ChatListener.class)
public class MixinChatListener {

	/**
	 * @reason Removes "Not Secure" and "Modified" statuses of chat messages. They ultimately
	 * server no purpose but to annoy the user and scare them away from servers that actually
	 * try to protect them by stripping message signatures.
	 * @author Aizistral
	 */

	@Inject(method = "evaluateTrustLevel", at = @At("HEAD"), cancellable = true)
	private void onEvaluateTrustLevel(PlayerChatMessage playerChatMessage,
			Component component, PlayerInfo playerInfo, Instant instant, CallbackInfoReturnable<ChatTrustLevel> info) {
		if (NoReportsConfig.suppressVanillaSecurityNotices()) {
			info.setReturnValue(ChatTrustLevel.SECURE);
		}

		// Debug never dies
		if (NoReportsConfig.isDebugLogEnabled()) {
			NoChatReports.LOGGER.info("Received message: {}, from: {}, signature: {}",
					Component.Serializer.toStableJson(playerChatMessage.signedContent().decorated()),
					playerChatMessage.signer().profileId(),
					Base64.getEncoder().encodeToString(playerChatMessage.headerSignature().bytes()));
		}
	}

}
