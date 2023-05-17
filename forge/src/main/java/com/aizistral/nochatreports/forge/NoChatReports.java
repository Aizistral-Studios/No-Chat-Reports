package com.aizistral.nochatreports.forge;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.platform.PlatformProvider;
import com.aizistral.nochatreports.common.platform.events.ClientEvents;

import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.event.EventNetworkChannel;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

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
