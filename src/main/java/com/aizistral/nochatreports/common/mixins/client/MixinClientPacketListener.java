package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.core.ServerSafetyLevel;
import com.aizistral.nochatreports.common.core.ServerSafetyState;
import com.mojang.brigadier.ParseResults;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.SignedArgumentList;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPacketListener {

	@Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"))
	private void onSend(Packet<?> packet, CallbackInfo info) {
		if (!ServerSafetyState.allowChatSigning() && !ServerSafetyState.isDetermined()) {
			if (packet instanceof ChatMessageC2SPacket chat) {
				ServerSafetyState.updateCurrent(ServerSafetyLevel.UNINTRUSIVE); // asume unintrusive until further notice
			} else if (packet instanceof CommandExecutionC2SPacket command) {
				if (!SignedArgumentList.of(this.parseCommand(command.command())).arguments().isEmpty()) {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.UNINTRUSIVE);
				}
			}
		}
	}

	@Shadow
	private ParseResults<CommandSource> parseCommand(String command) {
		throw new IllegalStateException("@Shadow transformation failed");
	}

}
