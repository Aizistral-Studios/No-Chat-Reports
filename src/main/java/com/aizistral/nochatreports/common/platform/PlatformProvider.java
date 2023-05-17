package com.aizistral.nochatreports.common.platform;

import java.io.File;
import java.nio.file.Path;

import net.fabricmc.api.EnvType;

public interface PlatformProvider {

	public boolean isOnClient();

	public boolean isOnDedicatedServer();

	public Path getMinecraftDir();

	public Path getConfigDir();

}
