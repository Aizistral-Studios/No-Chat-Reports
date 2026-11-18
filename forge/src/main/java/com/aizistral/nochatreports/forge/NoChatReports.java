package com.aizistral.nochatreports.forge;

import java.nio.file.Path;
import java.util.function.Function;

import com.aizistral.nochatreports.common.NCRClient;
import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.platform.PlatformProvider;
import com.aizistral.nochatreports.common.platform.events.ClientEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod("nochatreports")
public class NoChatReports implements PlatformProvider {
	
	public NoChatReports(FMLJavaModLoadingContext context) {
		NCRCore.awaken(this);

		if (NCRConfig.isModRunning() && this.isOnClient()) {
			ClientPlayerNetworkEvent.LoggingIn.BUS.addListener(Events::onPlayReady);
		}

        Function<Screen, Screen> configScreenFactory = NCRClient.getConfigScreen();
        if (isOnClient() && configScreenFactory != null) {
            context.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) -> configScreenFactory.apply(parent)
                )
            );
        }
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
	
	public static class Events {
		
		public static void onPlayReady(ClientPlayerNetworkEvent.LoggingIn event) {
			Minecraft client = Minecraft.getInstance();
			ClientPacketListener handler = client.getConnection();
			ClientEvents.PLAY_READY.invoker().handle(handler, client);
		}
		
	}
	
}
