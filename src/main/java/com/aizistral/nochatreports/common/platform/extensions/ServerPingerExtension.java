package com.aizistral.nochatreports.common.platform.extensions;

import com.aizistral.nochatreports.common.core.ServerDataExtension;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ServerData;

@Environment(EnvType.CLIENT)
public interface ServerPingerExtension {

	public ServerData getServerData();

}
