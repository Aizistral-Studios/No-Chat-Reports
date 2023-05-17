package com.aizistral.nochatreports.common.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class TooltipHelper {

	private TooltipHelper() {
		throw new IllegalStateException("Can't touch this");
	}

	public static MutableComponent getCtrl() {
		if (Minecraft.ON_OSX)
			return Component.translatable("key.nochatreports.cmd");
		else
			return Component.translatable("key.nochatreports.ctrl");
	}

}
