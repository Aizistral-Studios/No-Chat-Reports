package com.aizistral.nochatreports.debug;

import java.util.UUID;

import com.mojang.authlib.minecraft.BanDetails;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public final class NoChatReportsDebug implements ModInitializer {
	@Override
	public void onInitialize() {
		ServerPlayConnectionEvents.JOIN.register(this::onPlayReady);
	}

	private void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
		server.execute(() -> {
			UnrealPlayer player = new UnrealPlayer(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"), "Notch");
			//player.join(server, handler);
			Component component = Component.Serializer.fromJson("{\"translate\":\"%s\",\"with\":[{\"extra\":[{\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/tell Aizistral \"},\"hoverEvent\":{\"action\":\"show_entity\",\"contents\":{\"id\":\"bfa45411-874a-4ee0-b3bd-00c716059d95\",\"name\":{\"text\":\"Aizistral\"},\"type\":\"minecraft:player\"}},\"insertion\":\"Aizistral\",\"text\":\"Aizistral\"},{\"text\":\"> \"},{\"text\":\"['(9^~©²~.2>5¶(4,+:.9!¿¿\"}],\"text\":\"<\"},{\"text\":\"['(9^~©²~.2>5¶(4,+:.9!¿¿\"}]}");
			//player.sendSystem(server, handler, component);

			//player.sendMessage(server, handler, "This message is so real and legit", null);

			//UnrealPlayer.DEFAULT.join(server, handler);
			//UnrealPlayer.DEFAULT.sendMessage(server, handler, "I'm definitely not gay", "§8I§a'§bM §cG§dA§eY");
			//UnrealPlayer.DEFAULT.sendMessage(server, handler, "gOODBYE LOL", null);
			//UnrealPlayer.DEFAULT.leave(server, handler);

			//NoChatReports.LOGGER.info("Unreal player deployed");
		});
	}

	public static BanDetails getFakeBan() {
		return null;
		//		return new BanDetails(UUID.fromString(Minecraft.getInstance().getUser().getUuid()),
		//				null, null, null);
	}

}
