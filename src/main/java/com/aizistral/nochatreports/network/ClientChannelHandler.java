package com.aizistral.nochatreports.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

/**
 * Mod's client channel handler. So far just passively exists.
 * @author Aizistral
 */

@Environment(EnvType.CLIENT)
public class ClientChannelHandler implements PlayChannelHandler {
	public static final ClientChannelHandler INSTANCE = new ClientChannelHandler();

	private ClientChannelHandler() {
		// Can't touch this
	}

	@Override
	public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
		// NO-OP
	}
}
