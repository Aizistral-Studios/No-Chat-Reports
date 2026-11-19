package com.aizistral.nochatreports.common.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

//@Environment(EnvType.CLIENT)
public class ServerStatusCache {
	private static ThreadLocal<Boolean> preventsReports = new ThreadLocal<>();

	public static boolean doesPreventReports() {
		Boolean value = preventsReports.get();
		return value != null && value.booleanValue();
	}

	public static void setPreventsReports(boolean doesPreventReports) {
		preventsReports.set(doesPreventReports);
	}

}
