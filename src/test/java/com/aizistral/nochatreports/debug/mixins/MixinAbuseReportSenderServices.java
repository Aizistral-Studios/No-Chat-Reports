package com.aizistral.nochatreports.debug.mixins;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.aizistral.nochatreports.debug.ReportWriter;
import com.mojang.authlib.minecraft.report.AbuseReport;
import com.mojang.authlib.yggdrasil.request.AbuseReportRequest;
import com.mojang.datafixers.util.Unit;

import net.minecraft.client.multiplayer.chat.report.AbuseReportSender;
import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;

@Mixin(AbuseReportSender.Services.class)
public class MixinAbuseReportSenderServices {
	@Shadow @Final
	private ReportEnvironment environment;

	/**
	 * @reason Write report locally instead of trying to send it to Mojang.
	 * Allows to intercept and examine contents of the report.
	 * @author Aizistral
	 */

	@Overwrite
	public CompletableFuture<Unit> send(UUID uuid, AbuseReport abuseReport) {
		AbuseReportRequest abuseReportRequest = new AbuseReportRequest(1, uuid,
				abuseReport, this.environment.clientInfo(), this.environment.thirdPartyServerInfo(),
				this.environment.realmInfo());
		ReportWriter.saveReport(abuseReportRequest);
		return CompletableFuture.supplyAsync(() -> Unit.INSTANCE);
	}

}
