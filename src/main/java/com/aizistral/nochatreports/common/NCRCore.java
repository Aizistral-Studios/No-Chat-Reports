package com.aizistral.nochatreports.common;

import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.platform.PlatformProvider;
import com.google.common.base.Preconditions;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class NCRCore {
	public static final Logger LOGGER = LogManager.getLogger("NoChatReports");
	private static PlatformProvider provider = null;

	private NCRCore() {
		throw new IllegalStateException("Can't touch this");
	}

	public static PlatformProvider getProvider() {
		return provider;
	}

	public static void awaken(PlatformProvider platformProvider) {
		Preconditions.checkArgument(provider == null, "NCRCore already awake");
		provider = platformProvider;

		setup();

		if (provider.isOnClient()) {
			clientSetup();
		}
	}

	private static void setup() {
		LOGGER.info("KONNICHIWA ZA WARUDO!");
		LOGGER.info("Default JVM text encoding is: " + Charset.defaultCharset().displayName());

		NCRConfig.load();
	}

	@Environment(EnvType.CLIENT)
	private static void clientSetup() {
		NCRClient.setup();
	}

}
