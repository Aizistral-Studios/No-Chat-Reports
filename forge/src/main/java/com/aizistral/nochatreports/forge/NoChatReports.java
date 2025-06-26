package com.aizistral.nochatreports.forge;

import java.nio.file.Path;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.platform.PlatformProvider;
import com.aizistral.nochatreports.common.platform.events.ClientEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

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
	
	@EventBusSubscriber(modid = "nochatreports", bus = Bus.FORGE, value = Dist.CLIENT)
	public static class Events {
		
		@SubscribeEvent
		public static void onPlayReady(ClientPlayerNetworkEvent.LoggingIn event) {
			Minecraft client = Minecraft.getInstance();
			ClientPacketListener handler = client.getConnection();
			ClientEvents.PLAY_READY.invoker().handle(handler, client);
		}
		
	}
	
}
