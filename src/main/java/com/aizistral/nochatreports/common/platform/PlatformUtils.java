package com.aizistral.nochatreports.common.platform;

public class PlatformUtils {

	public static Platform getPlatform() {
		try {
			var loader = Class.forName("net.minecraftforge.common.MinecraftForge");
			return Platform.FORGE;
		} catch (Exception ex) {
			return Platform.FABRIC;
		}
	}

}
