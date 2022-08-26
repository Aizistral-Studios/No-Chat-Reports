package com.aizistral.nochatreports.debug;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.config.NCRConfigLegacy;
import com.aizistral.nochatreports.network.ServerChannelHandler;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.BanDetails;
import com.mojang.authlib.minecraft.UserApiService;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.reporting.ChatReportScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket.Action;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket.PlayerUpdate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public final class NoChatReportsDebug implements ModInitializer {
	@Override
	public void onInitialize() {
		ServerPlayConnectionEvents.JOIN.register(this::onPlayReady);
	}

	private void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
		server.execute(() -> {
			UnrealPlayer player = new UnrealPlayer(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"), "Notch");
			player.join(server, handler);
			player.sendMessage(server, handler, "This message is so real and legit", null);

			//UnrealPlayer.DEFAULT.join(server, handler);
			//UnrealPlayer.DEFAULT.sendMessage(server, handler, "I'm definitely not gay", "§8I§a'§bM §cG§dA§eY");
			//UnrealPlayer.DEFAULT.sendMessage(server, handler, "gOODBYE LOL", null);
			//UnrealPlayer.DEFAULT.leave(server, handler);

			NoChatReports.LOGGER.info("Unreal player deployed");
		});
	}

	public static BanDetails getFakeBan() {
		return null;
		//		return new BanDetails(UUID.fromString(Minecraft.getInstance().getUser().getUuid()),
		//				null, null, null);
	}

}
