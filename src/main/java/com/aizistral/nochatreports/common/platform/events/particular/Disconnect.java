package com.aizistral.nochatreports.common.platform.events.particular;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

@Environment(EnvType.CLIENT)
public interface Disconnect {
	public void handle(Minecraft client);
}
