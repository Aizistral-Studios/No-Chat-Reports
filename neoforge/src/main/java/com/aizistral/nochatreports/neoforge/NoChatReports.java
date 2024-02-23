package com.aizistral.nochatreports.neoforge;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.platform.PlatformProvider;
import com.aizistral.nochatreports.common.platform.events.ClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

@Mod("nochatreports")
public class NoChatReports implements PlatformProvider {
	
	public NoChatReports() {
		NCRCore.awaken(this);
	}
	
	@Override
	public boolean isOnClient() {
		return FMLEnvironment.dist == Dist.CLIENT;
	}
	
	@Override
	public boolean isOnDedicatedServer() {
		return FMLEnvironment.dist == Dist.DEDICATED_SERVER;
	}

	@Override
	public Path getMinecraftDir() {
		return FMLPaths.GAMEDIR.get();
	}

	@Override
	public Path getConfigDir() {
		return FMLPaths.CONFIGDIR.get();
	}
	
	@EventBusSubscriber(modid = "nochatreports", bus = Bus.FORGE)
	public static class Events {
		
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public static void onPlayReady(ClientPlayerNetworkEvent.LoggingIn event) {
			Minecraft client = Minecraft.getInstance();
			ClientPacketListener handler = client.getConnection();
			ClientEvents.PLAY_READY.invoker().handle(handler, client);
		}
		
	}
	
}
