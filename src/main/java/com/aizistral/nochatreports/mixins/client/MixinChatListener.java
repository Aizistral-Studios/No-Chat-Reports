package com.aizistral.nochatreports.mixins.client;

import java.util.Base64;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.core.NoReportsConfig;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.client.multiplayer.chat.ChatTrustLevel;
import net.minecraft.network.chat.ChatSender;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;

@Mixin(ChatListener.class)
public class MixinChatListener {

	/**
	 * @reason No annoying safety notices, thanks. Your system is unsafe anyways.
	 * @author Aizistral
	 */

	@Inject(method = "evaluateTrustLevel", at = @At("HEAD"), cancellable = true)
	private void onEvaluateTrustLevel(ChatSender chatSender, PlayerChatMessage playerChatMessage,
			Component component, PlayerInfo playerInfo, CallbackInfoReturnable<ChatTrustLevel> info) {
		if (NoReportsConfig.suppressMessageTrustIndicators()) {
			info.setReturnValue(ChatTrustLevel.SECURE);
		}

		// Debug never dies
		if (NoReportsConfig.isDebugLogEnabled()) {
			NoChatReports.LOGGER.info("Received message: {}, from: {}, signature: {}",
					Component.Serializer.toStableJson(playerChatMessage.signedContent()),
					chatSender.name().getString(),
					Base64.getEncoder().encodeToString(playerChatMessage.signature().saltSignature().signature()));
		}
	}

}
