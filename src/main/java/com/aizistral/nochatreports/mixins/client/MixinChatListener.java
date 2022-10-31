package com.aizistral.nochatreports.mixins.client;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.core.EncryptionUtil;

import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.client.multiplayer.chat.ChatTrustLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;

@Mixin(ChatListener.class)
public class MixinChatListener {

	@Shadow
	private boolean isSenderLocalPlayer(UUID uuid) {
		throw new IllegalStateException("@Shadow transformation failed. Should never happen.");
	}

	/**
	 * @reason Removes "Not Secure" and "Modified" statuses of chat messages. They ultimately
	 * serve no purpose but to annoy the user and scare them away from servers that actually
	 * try to protect them by stripping message signatures.
	 * @author Aizistral
	 */

	@Inject(method = "evaluateTrustLevel", at = @At("HEAD"), cancellable = true)
	private void onEvaluateTrustLevel(PlayerChatMessage playerChatMessage, Component component, PlayerInfo playerInfo,
			Instant instant, CallbackInfoReturnable<ChatTrustLevel> info) {

		if (this.isSenderLocalPlayer(playerChatMessage.link().sender())) {
			info.setReturnValue(ChatTrustLevel.SECURE);
		} else {
			var evaluate = ChatTrustLevel.evaluate(playerChatMessage, component, instant);

			if (evaluate == ChatTrustLevel.NOT_SECURE && NCRConfig.getClient().hideInsecureMessageIndicators()) {
				info.setReturnValue(ChatTrustLevel.SECURE);
			} else if (evaluate == ChatTrustLevel.MODIFIED && NCRConfig.getClient().hideModifiedMessageIndicators()) {
				info.setReturnValue(ChatTrustLevel.SECURE);
			}
		}

		// Debug never dies
		if (NCRConfig.getCommon().enableDebugLog()) {
			NoChatReports.LOGGER.info("Received message: {}, from: {}, signature: {}",
					Component.Serializer.toStableJson(playerChatMessage.unsignedContent()),
					playerChatMessage.link().sender(),
					Base64.getEncoder().encodeToString(playerChatMessage.signature().bytes()));
		}
	}

	@ModifyVariable(method = "narrateChatMessage(Lnet/minecraft/network/chat/ChatType$Bound;"
			+ "Lnet/minecraft/network/chat/Component;)V", at = @At("HEAD"), argsOnly = true)
	private Component decryptNarratedMessage(Component msg) {
		return EncryptionUtil.tryDecrypt(msg).orElse(msg);
	}

}
