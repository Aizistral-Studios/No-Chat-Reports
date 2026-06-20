package com.aizistral.nochatreports.common.gui;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerSafetyState;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public final class UnsafeServerScreen extends WarningScreen {
	private static final Component TITLE = Component.translatable("gui.nochatreports.signing_required.header").withStyle(ChatFormatting.BOLD);
	private static final Component CONTENT = Component.translatable("gui.nochatreports.signing_required.contents");
	private static final Component CHECK = Component.translatable("gui.nochatreports.signing_required.check");
	private static final Component NARRATION = TITLE.copy().append("\n").append(CONTENT);
	private final Screen previous;

	private static boolean hideThisSession = false;

	public UnsafeServerScreen(Screen previous) {
		super(TITLE, CONTENT, CHECK, NARRATION);
		this.previous = previous;
	}

	public static void setHideThisSession(boolean hide) {
		hideThisSession = hide;
	}

	public static boolean hideThisSession() {
		return hideThisSession;
	}

	@Override
	protected Layout addFooterButtons() {
		LinearLayout linearLayout = LinearLayout.horizontal().spacing(8);
		linearLayout.addChild(Button.builder(Component.translatable("gui.nochatreports.signing_required.allow_signing"), button -> {
			ServerSafetyState.setAllowChatSigning(true);
			this.minecraft.gui.setScreen(this.previous);
		}).build());
		linearLayout.addChild(Button.builder(Component.translatable("gui.nochatreports.signing_required.cancel"), button -> {
			if (this.stopShowing.selected()) {
				hideThisSession = true;
			}
			this.minecraft.gui.setScreen(this.previous);
		}).build());
		return linearLayout;
	}
}
