package com.aizistral.nochatreports.mixins;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.handlers.NoReportsConfig;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.ChatTypeDecoration;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.ServerPlayerConnection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl implements ServerPlayerConnection {

	/**
	 * @reason Convert player message to system message if mod is configured respectively.
	 * This allows to circumvent signature check on client, as it only checks player messages.
	 * @author JFronny (original implementation)
	 * @author Aizistral
	 */

	@Inject(method = "send", at = @At("HEAD"), cancellable = true)
	private void onSend(Packet<?> packet, CallbackInfo info) {
		if (NoReportsConfig.convertsToGameMessage()) {
			if (packet instanceof ClientboundPlayerChatPacket chat) {
				Component component = chat.unsignedContent().orElse(chat.signedContent());
				component = ChatTypeDecoration.withSender("chat.type.text").decorate(component, chat.sender());
				packet = new ClientboundSystemChatPacket(component, this.resolveChatTypeID(ChatType.SYSTEM));

				info.cancel();
				this.send(packet);
			}
		}
	}

	private int resolveChatTypeID(ResourceKey<ChatType> resourceKey) {
		var self = (ServerGamePacketListenerImpl) (Object) this;
		Registry<ChatType> registry = self.player.level.registryAccess().registryOrThrow(Registry.CHAT_TYPE_REGISTRY);
		return registry.getId(registry.get(resourceKey));
	}

}
