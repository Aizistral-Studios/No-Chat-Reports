package com.aizistral.nochatreports.neoforge;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.config.ClothConfigIntegration;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.platform.PlatformProvider;
import com.aizistral.nochatreports.common.platform.events.ClientEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

import java.nio.file.Path;

@Mod("nochatreports")
public class NoChatReports implements PlatformProvider {
	
	public NoChatReports() {
		NCRCore.awaken(this);
        if (isOnClient()) {
			if (NCRConfig.isModRunning()) {
				NeoForge.EVENT_BUS.addListener(ClientPlayerNetworkEvent.LoggingIn.class, Events::onPlayReady);
			}

			if (!ClothConfigIntegration.ACTIVE) {
        		NCRCore.LOGGER.warn("ClothConfig API not found, cannot provide config screen factory.");
				return;
			}
            ModLoadingContext.get().registerExtensionPoint(
                    IConfigScreenFactory.class,
                    () -> (minecraft, parent) -> ClothConfigIntegration.getConfigScreen(parent)
            );
        }
	}
	
	@Override
	public boolean isOnClient() {
		return FMLEnvironment.getDist() == Dist.CLIENT;
	}
	
	@Override
	public boolean isOnDedicatedServer() {
		return FMLEnvironment.getDist() == Dist.DEDICATED_SERVER;
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
