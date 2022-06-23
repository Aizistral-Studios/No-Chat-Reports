package com.aizistral.nochatreports.mixins;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.handlers.NoReportsConfig;
import net.minecraft.network.chat.ChatDecoration;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer {

	@Shadow
	private int resolveChatTypeId(ResourceKey<ChatType> resourceKey) {
		throw new IllegalStateException("@Shadow transformation failed. Should never happen");
	}

	/**
	 * @reason Convert player message to system message if mod is configured respectively.
	 * This allows to circumvent signature check on client, as it only checks player messages.
	 * @author JFronny
	 */

	@Redirect(method = "sendChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Lnet/minecraft/network/chat/ChatSender;Lnet/minecraft/resources/ResourceKey;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"))
	void extractChatMessage(ServerGamePacketListenerImpl listener, Packet<?> packet) {
		if (NoReportsConfig.convertsToGameMessage()) {
			if (packet instanceof ClientboundPlayerChatPacket chat) {
				Component component = chat.unsignedContent().orElse(chat.signedContent());
				component = ChatDecoration.withSender("chat.type.text").decorate(component, chat.sender());
				packet = new ClientboundSystemChatPacket(component, this.resolveChatTypeId(ChatType.SYSTEM));
			} else {
				NoChatReports.LOGGER.warn("Unexpected packet type in sendChatMessage, skipping");
			}
		}
		listener.send(packet);
	}

}