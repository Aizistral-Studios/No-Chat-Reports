package com.aizistral.nochatreports;

import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aizistral.nochatreports.handlers.NoReportsConfig;

import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.network.simple.SimpleChannel;

@Mod(NoChatReports.MODID)
public class NoChatReports {
	public static final String MODID = "nochatreports";
	public static final Logger LOGGER = LogManager.getLogger();
	private static final String PTC_VERSION = "1";
	private static final Predicate<String> ANY = obj -> true;
	private static SimpleChannel packetInstance;

	public NoChatReports() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onLoadComplete);

		MinecraftForge.EVENT_BUS.register(this);

		NoReportsConfig.loadConfig();
	}

	private void onLoadComplete(final FMLLoadCompleteEvent event) {
		// NO-OP
	}

	private void setup(final FMLCommonSetupEvent event) {
		packetInstance = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MODID, "main"))
				.networkProtocolVersion(() -> PTC_VERSION)
				.clientAcceptedVersions(NoReportsConfig.demandsOnServer() ? PTC_VERSION::equals : ANY)
				.serverAcceptedVersions(NoReportsConfig.demandsOnClient() ? PTC_VERSION::equals : ANY)
				.simpleChannel();
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		// NO-OP
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		// NO-OP
	}

	private void processIMC(final InterModProcessEvent event) {
		// NO-OP
	}

}
