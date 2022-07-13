package com.aizistral.nochatreports.mixins.client;

import com.aizistral.nochatreports.core.NoReportsConfig;
import net.minecraft.client.ClientTelemetryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientTelemetryManager.class)
public class MixinClientTelemetryManager {

	/**
	 * @reason Privacy.
	 * @author Aizistral
	 */

	@Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/SharedConstants;IS_RUNNING_IN_IDE:Z"))
	private boolean disableTelemetrySession() {
		return NoReportsConfig.disableTelemetry();
	}

}
