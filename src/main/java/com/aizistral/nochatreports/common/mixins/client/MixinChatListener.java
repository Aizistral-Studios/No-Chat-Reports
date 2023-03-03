package com.aizistral.nochatreports.common.mixins.client;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.client.network.message.MessageTrustStatus;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
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

@Mixin(MessageHandler.class)
public class MixinChatListener {

	@Shadow
	private boolean isSenderLocalPlayer(UUID uuid) {
		throw new IllegalStateException("@Shadow transformation failed. Should never happen.");
	}

	@Inject(method = "handleSystemMessage", at = @At("HEAD"), cancellable = true)
	private void onHandleSystemMessage(Text message, boolean overlay, CallbackInfo info) {
		if (message instanceof MutableText mutable && message.getContent() instanceof TranslatableTextContent translatable) {
			if (translatable.getKey().equals("chat.disabled.missingProfileKey")) {
				mutable.content = new TranslatableTextContent("chat.nochatreports.disabled.signing_requested", null, TranslatableTextContent.EMPTY_ARGUMENTS);

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
					MinecraftClient.getInstance().setScreen(new UnsafeServerScreen(MinecraftClient.getInstance().currentScreen
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
	private void onEvaluateTrustLevel(SignedMessage playerChatMessage, Text component, Instant instant, CallbackInfoReturnable<MessageTrustStatus> info) {
		if (this.isSenderLocalPlayer(playerChatMessage.getSender())) {
			info.setReturnValue(MessageTrustStatus.SECURE);
		} else {
			if (playerChatMessage.hasSignature() && ServerSafetyState.getCurrent() == ServerSafetyLevel.SECURE) {
				ServerSafetyState.updateCurrent(ServerSafetyLevel.UNINTRUSIVE);
			}

			var evaluate = MessageTrustStatus.getStatus(playerChatMessage, component, instant);

			if (evaluate == MessageTrustStatus.NOT_SECURE && NCRConfig.getClient().hideInsecureMessageIndicators()) {
				info.setReturnValue(MessageTrustStatus.SECURE);
			} else if (evaluate == MessageTrustStatus.MODIFIED && NCRConfig.getClient().hideModifiedMessageIndicators()) {
				info.setReturnValue(MessageTrustStatus.SECURE);
			}
		}

		// Debug never dies
		if (NCRConfig.getCommon().enableDebugLog()) {
			NCRCore.LOGGER.info("Received message: {}, from: {}, signature: {}",
					Text.Serializer.toSortedJsonString(playerChatMessage.unsignedContent()),
					playerChatMessage.link().sender(),
					Base64.getEncoder().encodeToString(playerChatMessage.signature() != null ? playerChatMessage.signature().data() : new byte[0]));
		}
	}

	@ModifyVariable(method = "narrateChatMessage(Lnet/minecraft/network/chat/ChatType$Bound;"
			+ "Lnet/minecraft/network/chat/Component;)V", at = @At("HEAD"), argsOnly = true)
	private Text decryptNarratedMessage(Text msg) {
		return EncryptionUtil.tryDecrypt(msg).orElse(msg);
	}

}
