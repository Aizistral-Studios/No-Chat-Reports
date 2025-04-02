package com.aizistral.nochatreports.common.mixins.server;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.config.NCRConfig;

import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.ServerPlayerConnection;

@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class MixinServerCommonPacketListenerImpl {

	/**
	 * @reason Convert player message to system message if mod is configured respectively.
	 * This allows to circumvent signature check on client, as it only checks player messages.
	 * @author JFronny (original implementation)
	 * @author Aizistral
	 */

	@Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
	private void onSend(Packet<?> packet, CallbackInfo info) {
		Object self = this;

		if (self instanceof ServerGamePacketListenerImpl listener) {
			if (NCRConfig.getCommon().enableDebugLog() && packet instanceof ClientboundPlayerChatPacket chat) {
				NCRCore.LOGGER.info("Sending message: {}", chat.unsignedContent() != null ? chat.unsignedContent()
						: chat.body().content());
			}

			if (NCRConfig.getCommon().convertToGameMessage()) {
				if (packet instanceof ClientboundPlayerChatPacket chat) {
					packet = new ClientboundSystemChatPacket(chat.chatType().decorate(
							chat.unsignedContent() != null ? chat.unsignedContent()
									: Component.literal(chat.body().content())
							), false);

					info.cancel();
					listener.send(packet);
				}
			}
		}
	}

	/**
	 * @reason Ensure conversion works regardless of which send method is used.
	 * @author Aizistral
	 */

	@Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V",
			at = @At("HEAD"), cancellable = true)
	private void onSend(Packet<?> packet, @Nullable PacketSendListener packetSendListener, CallbackInfo info) {
		Object self = this;

		if (self instanceof ServerGamePacketListenerImpl listener) {
			if (NCRConfig.getCommon().enableDebugLog() && packet instanceof ClientboundPlayerChatPacket chat) {
				NCRCore.LOGGER.info("Sending message: {}", chat.unsignedContent() != null ? chat.unsignedContent()
						: chat.body().content());
			}

			if (NCRConfig.getCommon().convertToGameMessage()) {
				if (packet instanceof ClientboundPlayerChatPacket chat && packetSendListener != null) {
					info.cancel();
					listener.send(chat);
				}
			}

		}
	}

}
