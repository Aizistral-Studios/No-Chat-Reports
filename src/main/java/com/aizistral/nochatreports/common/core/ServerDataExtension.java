package com.aizistral.nochatreports.common.core;

import com.aizistral.nochatreports.common.mixins.client.MixinServerData;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.status.ServerStatus;

/**
 * Implemented by {@link MixinServerData} on {@link ServerData} class.
 *
 * @author fxmorin (original implementation)
 * @author Aizistral (current version)
 */

public interface ServerDataExtension {

	public boolean preventsChatReports();

	public void setPreventsChatReports(boolean prevents);

}