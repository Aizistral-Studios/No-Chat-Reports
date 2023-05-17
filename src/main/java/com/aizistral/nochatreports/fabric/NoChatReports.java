package com.aizistral.nochatreports.fabric;

import java.nio.file.Path;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.platform.PlatformProvider;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class NoChatReports implements ModInitializer, PlatformProvider {

	@Override
	public void onInitialize() {
		NCRCore.awaken(this);
	}

	@Override
	public boolean isOnClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}

	@Override
	public boolean isOnDedicatedServer() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
	}

	@Override
	public Path getMinecraftDir() {
		return FabricLoader.getInstance().getGameDir();
	}

	@Override
	public Path getConfigDir() {
		return FabricLoader.getInstance().getConfigDir();
	}

}
