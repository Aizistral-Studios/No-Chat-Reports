package com.aizistral.nochatreports.common.platform.events.particular;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public interface Disconnect {
	public void handle(MinecraftClient client);
}
