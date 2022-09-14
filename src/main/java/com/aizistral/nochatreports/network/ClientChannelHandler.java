package com.aizistral.nochatreports.network;

import com.aizistral.nochatreports.NoChatReports;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.PlayChannelHandler;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

/**
 * Mod's client channel handler. So far just passively exists.
 * @author Aizistral
 */

@Environment(EnvType.CLIENT)
public final class ClientChannelHandler implements PlayChannelHandler {
	public static final ClientChannelHandler INSTANCE = new ClientChannelHandler();
	private boolean registered = false;

	private ClientChannelHandler() {
		// Can't touch this
	}

	@Override
	public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
		// NO-OP
	}

	public void register() {
		if (!this.registered) {
			ClientPlayNetworking.registerGlobalReceiver(NoChatReports.CHANNEL, ClientChannelHandler.INSTANCE);
			this.registered = true;
		}
	}

	public void unregister() {
		if (this.registered) {
			ClientPlayNetworking.unregisterGlobalReceiver(NoChatReports.CHANNEL);
			this.registered = false;
		}
	}

	public boolean isRegistered() {
		return this.registered;
	}

}
