package com.aizistral.nochatreports.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayChannelHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class ServerChannelHandler implements PlayChannelHandler {
	public static final ServerChannelHandler INSTANCE = new ServerChannelHandler();

	private ServerChannelHandler() {
		// Can't touch this
	}

	@Override
	public void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
		// NO-OP
	}

}
