package com.aizistral.nochatreports.debug.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.gui.screens.social.PlayerEntry;
import net.minecraft.client.multiplayer.chat.report.AbuseReportSender;

@Mixin(PlayerEntry.class)
public class MixinPlayerEntry {

	/**
	 * @reason Always allow to report players on server, even if services are
	 * unreachable (which they are in IDE). Useful when used along with
	 * {@link MixinAbuseReportSenderServices}.
	 * @author Aizistral
	 */

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target =
			"Lnet/minecraft/client/multiplayer/chat/report/AbuseReportSender;isEnabled()Z"))
	private boolean forceEnableReporting(AbuseReportSender sender) {
		return true;
	}

}
