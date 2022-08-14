package com.aizistral.nochatreports.mixins.server;

import com.aizistral.nochatreports.core.NoReportServerStatus;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

	@Redirect(method = "<init>", at = @At(value = "NEW",
			target = "Lnet/minecraft/network/protocol/status/ServerStatus;<init>()V"), require = 0)
	private ServerStatus useCustomServerStatus() {
		return new NoReportServerStatus();
	}

}
