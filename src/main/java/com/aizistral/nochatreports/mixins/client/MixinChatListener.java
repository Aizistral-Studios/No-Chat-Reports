package com.aizistral.nochatreports.mixins.client;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.core.NoReportsConfig;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.client.multiplayer.chat.ChatTrustLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;

@Mixin(ChatListener.class)
public class MixinChatListener {

	@Inject(method = "isSenderLocalPlayer", at = @At("RETURN"))
	private boolean isSenderLocalPlayer(UUID uuid, CallbackInfoReturnable<Boolean> cir) {
		return cir.getReturnValue();
	}

	/**
	 * @reason Removes "Not Secure" and "Modified" statuses of chat messages. They ultimately
	 * server no purpose but to annoy the user and scare them away from servers that actually
	 * try to protect them by stripping message signatures.
	 * @author Aizistral
	 */

	@Inject(method = "evaluateTrustLevel", at = @At("HEAD"), cancellable = true)
	private void onEvaluateTrustLevel(PlayerChatMessage playerChatMessage,
			Component component, PlayerInfo playerInfo, Instant instant, CallbackInfoReturnable<ChatTrustLevel> info) {

		if (this.isSenderLocalPlayer(playerChatMessage.signer().profileId(), null)){
			info.setReturnValue(ChatTrustLevel.SECURE);
		}
		else {
			var evaluate = ChatTrustLevel.evaluate(playerChatMessage, component, playerInfo, instant);
			if((evaluate == ChatTrustLevel.NOT_SECURE || evaluate == ChatTrustLevel.BROKEN_CHAIN) && NoReportsConfig.hideRedChatIndicators())
				info.setReturnValue(ChatTrustLevel.SECURE);
			if((evaluate == ChatTrustLevel.FILTERED || evaluate == ChatTrustLevel.MODIFIED) && NoReportsConfig.hideYellowChatIndicators())
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

	/**
	 * @reason Prevent client from disconnecting when chat chain is broken.
	 * Normal average user will not even understand what happened, so this is absolutely
	 * useless for them.
	 * @author Aizistral
	 */

	@Inject(method = "onChatChainBroken", at = @At("HEAD"), cancellable = true)
	private void stopUselessDisconnects(CallbackInfo info) {
		info.cancel();
	}

}
