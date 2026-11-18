package com.aizistral.nochatreports.loader;

import java.nio.file.Path;

import net.neoforged.fml.loading.FMLPaths;

public final class LoaderPaths {

	private LoaderPaths() {
		throw new IllegalStateException("Can't touch this");
	}

	public static Path getConfigDir() {
		return FMLPaths.CONFIGDIR.get();
	}

}
