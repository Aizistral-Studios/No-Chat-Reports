package com.aizistral.nochatreports;

import com.aizistral.nochatreports.handlers.NoReportsConfig;
import com.aizistral.nochatreports.network.ClientChannelHandler;
import com.aizistral.nochatreports.network.ServerChannelHandler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class NoChatReportsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		NoChatReports.LOGGER.info("Client initialization...");
		ClientPlayNetworking.registerGlobalReceiver(NoChatReports.CHANNEL, ClientChannelHandler.INSTANCE);
		ClientPlayConnectionEvents.JOIN.register(this::onPlayReady);
	}

	private void onPlayReady(ClientPacketListener handler, PacketSender sender, Minecraft client) {
		client.execute(() -> {
			if (NoReportsConfig.demandsOnServer() && !ClientPlayNetworking.canSend(NoChatReports.CHANNEL)) {
				handler.getConnection().disconnect(Component.translatable("disconnect.nochatreports.client"));
			}
		});
	}

}
