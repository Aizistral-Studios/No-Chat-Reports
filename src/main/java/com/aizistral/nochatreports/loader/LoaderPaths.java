package com.aizistral.nochatreports.loader;

import java.nio.file.Path;

import net.fabricmc.loader.api.FabricLoader;

public final class LoaderPaths {

	private LoaderPaths() {
		throw new IllegalStateException("Can't touch this");
	}

	public static Path getConfigDir() {
		return FabricLoader.getInstance().getConfigDir();
	}

}
