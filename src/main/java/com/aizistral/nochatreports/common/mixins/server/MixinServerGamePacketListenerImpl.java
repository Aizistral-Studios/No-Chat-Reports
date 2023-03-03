package com.aizistral.nochatreports.common.mixins.server;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.config.NCRConfig;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.EntityTrackingListener;
import net.minecraft.text.Text;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class MixinServerGamePacketListenerImpl implements EntityTrackingListener {
	@Shadow
	public ServerPlayerEntity player;

	/**
	 * @reason Convert player message to system message if mod is configured respectively.
	 * This allows to circumvent signature check on client, as it only checks player messages.
	 * @author JFronny (original implementation)
	 * @author Aizistral
	 */

	@Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
	private void onSend(Packet<?> packet, CallbackInfo info) {
		if (NCRConfig.getCommon().enableDebugLog() && packet instanceof ChatMessageS2CPacket chat) {
			NCRCore.LOGGER.info("Sending message: {}", chat.unsignedContent() != null ? chat.unsignedContent()
					: chat.body().content());
		}

		if (NCRConfig.getCommon().convertToGameMessage()) {
			if (packet instanceof ChatMessageS2CPacket chat) {
				packet = new GameMessageS2CPacket(chat.serializedParameters().toParameters(this.player.world.getRegistryManager())
						.get().applyChatDecoration(chat.unsignedContent() != null ? chat.unsignedContent()
								: Text.literal(chat.body().content())), false);

				info.cancel();
				this.sendPacket(packet);
			}
		}
	}

	/**
	 * @reason Ensure conversion works regardless of which send method is used.
	 * @author Aizistral
	 */

	@Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V",
			at = @At("HEAD"), cancellable = true)
	private void onSend(Packet<?> packet, @Nullable PacketCallbacks packetSendListener, CallbackInfo info) {
		if (NCRConfig.getCommon().enableDebugLog() && packet instanceof ChatMessageS2CPacket chat) {
			NCRCore.LOGGER.info("Sending message: {}", chat.unsignedContent() != null ? chat.unsignedContent()
					: chat.body().content());
		}

		if (NCRConfig.getCommon().convertToGameMessage()) {
			if (packet instanceof ChatMessageS2CPacket chat && packetSendListener != null) {
				info.cancel();
				((ServerPlayNetworkHandler) (Object) this).sendPacket(chat);
			}
		}
	}

}
