package com.aizistral.nochatreports.core;

import com.aizistral.nochatreports.NoChatReports;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;

public enum ServerSafetyLevel {
	SECURE, UNINTRUSIVE, INSECURE, UNKNOWN;

	@Environment(EnvType.CLIENT)
	public Component getTooltip() {
		return Component.translatable("gui.nochatreports.status_" + this.name().toLowerCase());
	}
}
