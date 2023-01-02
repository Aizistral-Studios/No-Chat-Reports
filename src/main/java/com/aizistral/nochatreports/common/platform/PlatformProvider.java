package com.aizistral.nochatreports.common.platform;

import java.io.File;
import java.nio.file.Path;

import net.fabricmc.api.EnvType;

public interface PlatformProvider {

	public EnvType getEnvironment();

	public Path getMinecraftDir();

	public Path getConfigDir();

}
