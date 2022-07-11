package com.aizistral.nochatreports.mixins.server;

import com.aizistral.nochatreports.core.NoReportsConfig;

import net.minecraft.core.Registry;
import net.minecraft.network.chat.ChatTypeDecoration;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.ServerPlayerConnection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl implements ServerPlayerConnection {

	@Shadow public ServerPlayer player;

	/**
	 * @reason Convert player message to system message if mod is configured respectively.
	 * This allows to circumvent signature check on client, as it only checks player messages.
	 * @author JFronny (original implementation)
	 * @author Aizistral
	 */

	@Inject(method = "send", at = @At("HEAD"), cancellable = true)
	private void onSend(Packet<?> packet, CallbackInfo info) {
		if (NoReportsConfig.convertToGameMessage()) {
			if (packet instanceof ClientboundPlayerChatPacket chat) {
				Component component = chat.unsignedContent().orElse(chat.signedContent());
				ChatTypeDecoration decoration = player.level.registryAccess().registryOrThrow(Registry.CHAT_TYPE_REGISTRY).byId(chat.typeId()).chat();
				component = decoration.decorate(component, chat.sender());
				packet = new ClientboundSystemChatPacket(component, false);

				info.cancel();
				this.send(packet);
			}
		}
	}

}
