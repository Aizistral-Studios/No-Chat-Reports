package com.aizistral.nochatreports.gui;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.NoChatReportsClient;
import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(value=EnvType.CLIENT)
public class AwaitConnectionScreen extends Screen {
	private final Screen parent;
	private boolean aborted = false;
	private long millisAwaiting = 0;

	public AwaitConnectionScreen(Screen parent) {
		super(GameNarrator.NO_TITLE);
		this.parent = parent;
	}

	@Override
	protected void init() {
		this.clearWidgets();
		this.millisAwaiting = Util.getMillis();

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> {
			if (NCRConfig.getCommon().enableDebugLog()) {
				NoChatReports.LOGGER.info("Aborted reconnect await!");
			}

			this.aborted = true;
			this.minecraft.setScreen(this.parent);
			ServerSafetyState.reset();
		}).pos(this.width / 2 - 100, this.height / 4 + 120 + 12).size(200, 20).build());
	}

	@Override
	public void tick() {
		if (!this.aborted)
			if (this.getRemainingTime() <= 0) {
				this.aborted = true;
				NoChatReportsClient.reconnectLastServer();
			}
	}

	@Override
	public void render(PoseStack poseStack, int i, int j, float f) {
		this.renderBackground(poseStack);
		Component status = Component.translatable("nochatreports.connect.await", (this.getRemainingTime()) / 1000L);
		ConnectScreen.drawCenteredString(poseStack, this.font, Component.translatable("nochatreports.connect.nokey_rejected"), this.width / 2, this.height / 2 - 50, 0xFFFFFF);
		ConnectScreen.drawCenteredString(poseStack, this.font, status, this.width / 2, this.height / 2 - 36, 0xFFFFFF);
		super.render(poseStack, i, j, f);
	}

	private long getRemainingTime() {
		long current = Util.getMillis();
		long disconnect = ServerSafetyState.getDisconnectMillis();

		if (current - disconnect <= this.getDisconnectAwaitTime() + 100L)
			return this.getDisconnectAwaitTime() - (current - disconnect);
		else
			return this.getAwaitTime() - (current - this.millisAwaiting);
	}

	private long getDisconnectAwaitTime() {
		return NCRConfig.getClient().getPostDisconnectAwaitSeconds() * 1000L;
	}

	private long getAwaitTime() {
		return NCRConfig.getClient().getReconnectAwaitSeconds() * 1000L;
	}

}
