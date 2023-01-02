package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.core.ServerSafetyLevel;
import com.aizistral.nochatreports.common.core.ServerSafetyState;
import com.mojang.brigadier.ParseResults;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.SignableCommand;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener {

	@Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"))
	private void onSend(Packet<?> packet, CallbackInfo info) {
		if (!ServerSafetyState.allowChatSigning() && !ServerSafetyState.isDetermined()) {
			if (packet instanceof ServerboundChatPacket chat) {
				ServerSafetyState.updateCurrent(ServerSafetyLevel.UNINTRUSIVE); // asume unintrusive until further notice
			} else if (packet instanceof ServerboundChatCommandPacket command) {
				if (!SignableCommand.of(this.parseCommand(command.command())).arguments().isEmpty()) {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.UNINTRUSIVE);
				}
			}
		}
	}

	@Shadow
	private ParseResults<SharedSuggestionProvider> parseCommand(String command) {
		throw new IllegalStateException("@Shadow transformation failed");
	}

}
