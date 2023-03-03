package com.aizistral.nochatreports.fabric.mixins.client;

import java.lang.reflect.Field;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerDataExtension;
import com.aizistral.nochatreports.common.platform.extensions.ServerPingerExtension;

@Mixin(targets = "net/minecraft/client/multiplayer/ServerStatusPinger$1")
public class MixinServerStatusPinger$1 implements ServerPingerExtension {
	@Shadow @Final
	private ServerInfo val$data;

	@Override
	public ServerInfo getServerData() {
		return this.val$data;
	}

	static {
		NCRCore.LOGGER.info("Fabric mixin into ServerStatusPinger$1 succeeded.");
	}

}

