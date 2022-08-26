package com.aizistral.nochatreports.debug;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import com.aizistral.nochatreports.debug.mixins.MixinAbuseReportSenderServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.icu.text.SimpleDateFormat;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import com.mojang.authlib.minecraft.report.AbuseReport;
import com.mojang.authlib.yggdrasil.request.AbuseReportRequest;

import net.fabricmc.loader.api.FabricLoader;

/**
 * Allows to save chat reports locally. See {@link MixinAbuseReportSenderServices} for details.
 *
 * @author Aizistral
 */

public final class ReportWriter {
	private static final ObjectMapper MAPPER = ObjectMapper.create();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static void saveReport(AbuseReportRequest report) {
		String string = MAPPER.writeValueAsString(report);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_hh.mm.ss");
		Path path = FabricLoader.getInstance().getGameDir().resolve("intercepted-reports/report-"
				+ format.format(Date.from(report.report.createdTime)) + ".json");
		path.getParent().toFile().mkdirs();

		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			GSON.toJson(GSON.fromJson(string, Object.class), writer);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
