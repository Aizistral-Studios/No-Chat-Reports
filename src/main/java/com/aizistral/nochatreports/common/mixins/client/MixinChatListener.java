package com.aizistral.nochatreports.common.mixins.client;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.common.NCRClient;
import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.EncryptionUtil;
import com.aizistral.nochatreports.common.core.ServerSafetyLevel;
import com.aizistral.nochatreports.common.core.ServerSafetyState;
import com.aizistral.nochatreports.common.core.SigningMode;
import com.aizistral.nochatreports.common.gui.UnsafeServerScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.client.multiplayer.chat.ChatTrustLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.chat.contents.TranslatableContents;

@Mixin(ChatListener.class)
public class MixinChatListener {

	@Shadow
	private boolean isSenderLocalPlayer(UUID uuid) {
		throw new IllegalStateException("@Shadow transformation failed. Should never happen.");
	}

	@Inject(method = "handleSystemMessage", at = @At("HEAD"), cancellable = true)
	private void onHandleSystemMessage(Component message, boolean overlay, CallbackInfo info) {
		if (message instanceof MutableComponent mutable && message.getContents() instanceof TranslatableContents translatable) {
			if (translatable.getKey().equals("chat.disabled.missingProfileKey")) {
				mutable.contents = new TranslatableContents("chat.nochatreports.disabled.signing_requested", null, TranslatableContents.NO_ARGS);

				if (!ServerSafetyState.isOnRealms()) {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.INSECURE);
				}

				if (UnsafeServerScreen.hideThisSession() || ServerSafetyState.allowChatSigning())
					return;

				if (NCRConfig.getServerPreferences().hasModeCurrent(SigningMode.ON_DEMAND)) {
					ServerSafetyState.scheduleSigningAction(NCRClient::resendLastChatMessage);
					ServerSafetyState.setAllowChatSigning(true);

					if (NCRConfig.getClient().hideSigningRequestMessage()) {
						info.cancel();
					}

					return;
				}

				if (NCRConfig.getServerPreferences().hasModeCurrent(SigningMode.PROMPT)) {
					Minecraft.getInstance().setScreen(new UnsafeServerScreen(Minecraft.getInstance().screen
							instanceof ChatScreen chat ? chat : new ChatScreen("")));

					if (NCRConfig.getClient().hideSigningRequestMessage()) {
						info.cancel();
					}
				}
			}
		}
	}

	/**
	 * @reason Removes "Not Secure" and "Modified" statuses of chat messages. They ultimately
	 * serve no purpose but to annoy the user and scare them away from servers that actually
	 * try to protect them by stripping message signatures.
	 * @author Aizistral
	 */

	@Inject(method = "evaluateTrustLevel", at = @At("HEAD"), cancellable = true)
	private void onEvaluateTrustLevel(PlayerChatMessage playerChatMessage, Component component, Instant instant, CallbackInfoReturnable<ChatTrustLevel> info) {
		if (this.isSenderLocalPlayer(playerChatMessage.sender())) {
			info.setReturnValue(ChatTrustLevel.SECURE);
		} else {
			if (playerChatMessage.hasSignature() && ServerSafetyState.getCurrent() == ServerSafetyLevel.SECURE) {
				ServerSafetyState.updateCurrent(ServerSafetyLevel.UNINTRUSIVE);
			}

			var evaluate = ChatTrustLevel.evaluate(playerChatMessage, component, instant);

			if (evaluate == ChatTrustLevel.NOT_SECURE && NCRConfig.getClient().hideInsecureMessageIndicators()) {
				info.setReturnValue(ChatTrustLevel.SECURE);
			} else if (evaluate == ChatTrustLevel.MODIFIED && NCRConfig.getClient().hideModifiedMessageIndicators()) {
				info.setReturnValue(ChatTrustLevel.SECURE);
			}
		}

		// Debug never dies
		if (NCRConfig.getCommon().enableDebugLog()) {
			NCRCore.LOGGER.info("Received message: {}, from: {}, signature: {}",
					Component.Serializer.toStableJson(playerChatMessage.unsignedContent()),
					playerChatMessage.link().sender(),
					Base64.getEncoder().encodeToString(playerChatMessage.signature() != null ? playerChatMessage.signature().bytes() : new byte[0]));
		}
	}

	@ModifyVariable(method = "narrateChatMessage(Lnet/minecraft/network/chat/ChatType$Bound;"
			+ "Lnet/minecraft/network/chat/Component;)V", at = @At("HEAD"), argsOnly = true)
	private Component decryptNarratedMessage(Component msg) {
		return EncryptionUtil.tryDecrypt(msg).orElse(msg);
	}

}
