package com.aizistral.nochatreports.core;

import com.aizistral.nochatreports.mixins.client.MixinServerData;
import com.aizistral.nochatreports.mixins.common.MixinServerStatus;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.status.ServerStatus;

/**
 * Implemented by {@link MixinServerStatus} on {@link ServerStatus} class and {@link MixinServerData}
 * on {@link ServerData} class.
 *
 * @author fxmorin (original implementation)
 * @author Aizistral (current version)
 */

public interface ServerDataExtension {

	public boolean preventsChatReports();

	public void setPreventsChatReports(boolean prevents);

}