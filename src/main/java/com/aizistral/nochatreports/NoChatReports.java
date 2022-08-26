package com.aizistral.nochatreports;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.network.ServerChannelHandler;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

/**
 * Common initializer for the mod. Some networking and config setup here.
 * @author Aizistral
 */

public final class NoChatReports implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
	public static final ResourceLocation CHANNEL = new ResourceLocation("nochatreports", "sync");

	@Override
	public void onInitialize() {
		LOGGER.info("KONNICHIWA ZA WARUDO!");

		ServerPlayNetworking.registerGlobalReceiver(CHANNEL, ServerChannelHandler.INSTANCE);
		ServerPlayConnectionEvents.JOIN.register(this::onPlayReady);
		NCRConfig.load();
	}

	private void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
		server.execute(() -> {
			if (NCRConfig.getCommon().demandOnClient() && !ServerPlayNetworking.canSend(handler, CHANNEL)) {
				handler.disconnect(Component.literal(NCRConfig.getCommon().demandOnClientMessage()));
			}

			//handler.disconnect(Component.translatable("multiplayer.disconnect.missing_public_key"));
		});
	}

}
