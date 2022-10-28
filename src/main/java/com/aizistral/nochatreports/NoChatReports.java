package com.aizistral.nochatreports;

import java.nio.charset.Charset;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aizistral.nochatreports.config.NCRConfig;

import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.event.EventNetworkChannel;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod(NoChatReports.MODID)
public class NoChatReports {
	public static final String MODID = "nochatreports";
	public static final Logger LOGGER = LogManager.getLogger();
	private static final String PTC_VERSION = "1";
	private static final Predicate<String> ANY = obj -> true;
	private static SimpleChannel channel;
	private static boolean detectedOnClient = false, detectedOnServer = false;

	public NoChatReports() {
		LOGGER.info("KONNICHIWA ZA WARUDO!");
		LOGGER.info("Default JVM text encoding is: " + Charset.defaultCharset().displayName());

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);

		MinecraftForge.EVENT_BUS.register(this);
		NCRConfig.load();
	}

	private void onLoadComplete(FMLLoadCompleteEvent event) {
		// NO-OP
	}

	private void setup(FMLCommonSetupEvent event) {
		channel = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MODID, "main"))
				.networkProtocolVersion(() -> PTC_VERSION)
				.clientAcceptedVersions(version -> {
					if (!NCRConfig.getClient().enableMod())
						return true;

					detectedOnServer = !isAbsentOrVanilla(version);
					return NCRConfig.getClient().demandOnServer() ? PTC_VERSION.equals(version) : true;
				})
				.serverAcceptedVersions(version -> {
					if (ServerLifecycleHooks.getCurrentServer().isSingleplayer())
						return true;

					detectedOnClient = !isAbsentOrVanilla(version);
					return NCRConfig.getCommon().demandOnClient() ? PTC_VERSION.equals(version) : true;
				})
				.simpleChannel();
	}

	private void doClientStuff(FMLClientSetupEvent event) {
		// NO-OP
	}

	private void enqueueIMC(InterModEnqueueEvent event) {
		// NO-OP
	}

	private void processIMC(InterModProcessEvent event) {
		// NO-OP
	}

	private static boolean isAbsentOrVanilla(String protocol) {
		return NetworkRegistry.ABSENT.equals(protocol) || NetworkRegistry.ACCEPTVANILLA.equals(protocol);
	}

	public static boolean isDetectedOnClient() {
		return detectedOnClient;
	}

	public static boolean isDetectedOnServer() {
		return detectedOnServer;
	}

}
