package com.aizistral.nochatreports;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;

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
		LOGGER.info("Default JVM text encoding is: " + Charset.defaultCharset().displayName());

		ServerPlayNetworking.registerGlobalReceiver(CHANNEL, ServerChannelHandler.INSTANCE);
		ServerPlayConnectionEvents.JOIN.register(this::onPlayReady);
		NCRConfig.load();
	}

	private void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
		server.execute(() -> {
			if (server.isSingleplayerOwner(handler.player.getGameProfile()))
				return;

			if (NCRConfig.getCommon().demandOnClient() && !ServerPlayNetworking.canSend(handler, CHANNEL)) {
				handler.disconnect(Component.literal(NCRConfig.getCommon().demandOnClientMessage()));
			}
		});
	}

}
