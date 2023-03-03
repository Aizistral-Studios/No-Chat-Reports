package com.aizistral.nochatreports.common.core;

import com.aizistral.nochatreports.common.mixins.client.MixinServerData;
import com.aizistral.nochatreports.common.mixins.common.MixinServerStatus;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.server.ServerMetadata;

/**
 * Implemented by {@link MixinServerStatus} on {@link ServerMetadata} class and {@link MixinServerData}
 * on {@link ServerInfo} class.
 *
 * @author fxmorin (original implementation)
 * @author Aizistral (current version)
 */

public interface ServerDataExtension {

	public boolean preventsChatReports();

	public void setPreventsChatReports(boolean prevents);

}