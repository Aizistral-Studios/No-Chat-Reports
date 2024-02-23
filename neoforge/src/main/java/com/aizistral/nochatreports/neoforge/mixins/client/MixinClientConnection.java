package com.aizistral.nochatreports.neoforge.mixins.client;

import com.aizistral.nochatreports.common.platform.events.ClientEvents;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class MixinClientConnection {
	@Shadow
	private PacketListener packetListener;

	@Inject(method = "channelInactive", at = @At("HEAD"))
	private void handleDisconnect(ChannelHandlerContext context, CallbackInfo info) {
		// not the case for client/server query
		if (this.packetListener instanceof ClientHandshakePacketListenerImpl
				|| this.packetListener instanceof ClientPacketListener) {
			ClientEvents.DISCONNECT.invoker().handle(Minecraft.getInstance());
		}
	}

}
