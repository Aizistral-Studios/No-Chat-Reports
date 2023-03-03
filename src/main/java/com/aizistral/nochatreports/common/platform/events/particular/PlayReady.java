package com.aizistral.nochatreports.common.platform.events.particular;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

@Environment(EnvType.CLIENT)
public interface PlayReady {
	public void handle(ClientPlayNetworkHandler handler, MinecraftClient client);
}
