package com.aizistral.nochatreports.neoforge.mixins.client;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.platform.extensions.ServerPingerExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;

import java.lang.reflect.Field;

@Mixin(targets = "net/minecraft/client/multiplayer/ServerStatusPinger$1")
public class MixinServerStatusPinger$1 implements ServerPingerExtension {
	private static final Field SERVER_DATA_FIELD;

	static {
		// We use reflection here because Mixin's AP dies when trying to process @Shadow of this lol
		try {
			Class<?> pinger = Class.forName("net.minecraft.client.multiplayer.ServerStatusPinger$1");

			SERVER_DATA_FIELD = pinger.getDeclaredField("val$p_105460_");
			SERVER_DATA_FIELD.setAccessible(true);
		} catch (Exception ex) {
			Error error = new Error("Reflection failed in MixinServerStatusPinger$1!", ex);

			NCRCore.LOGGER.catching(error);
			Minecraft.getInstance().execute(() -> {
				throw error;
			});

			throw error;
		}
	}
	
	@Override
	public ServerData getServerData() {
		try {
			return (ServerData) SERVER_DATA_FIELD.get(this);
		} catch (Exception ex) {
			throw new RuntimeException("Failed to get ServerData field via reflection", ex);
		}
	}

}
