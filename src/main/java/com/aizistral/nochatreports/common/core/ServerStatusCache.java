package com.aizistral.nochatreports.common.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

//@Environment(EnvType.CLIENT)
public class ServerStatusCache {
	private static final ThreadLocal<Boolean> PREVENTS_REPORTS = new ThreadLocal<>();

	public static boolean doesPreventReports() {
		Boolean value = PREVENTS_REPORTS.get();
		return value != null && value.booleanValue();
	}

	public static void setPreventsReports(boolean doesPreventReports) {
		PREVENTS_REPORTS.set(Boolean.valueOf(doesPreventReports));
	}

}
