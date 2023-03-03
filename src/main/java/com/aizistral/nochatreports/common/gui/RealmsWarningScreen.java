package com.aizistral.nochatreports.common.gui;

import com.aizistral.nochatreports.common.config.NCRConfig;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class RealmsWarningScreen extends TriageWarningScreen {
	private static final Component TITLE = Component.translatable("gui.nochatreports.realms_warning.header").withStyle(ChatFormatting.BOLD);
	private static final Component CONTENT = Component.translatable("gui.nochatreports.realms_warning.contents");
	private static final Component CHECK = Component.translatable("gui.nochatreports.realms_warning.check");
	private static final String WIKI_LINK = "https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/The-Realms-Question";
	private static boolean sessionSeen = false;
	private final Screen realms;

	public RealmsWarningScreen(Screen previous, Screen realms) {
		super(TITLE, CONTENT, CHECK, WIKI_LINK, previous);
		this.realms = realms;
	}

	@Override
	protected void onProceed(Button button) {
		this.minecraft.setScreen(this.realms);
		if (this.stopShowing.selected()) {
			NCRConfig.getClient().setSkipRealmsWarning(true);
		}

		sessionSeen = true;
	}

	@Override
	protected void onBack(Button button) {
		this.minecraft.setScreen(null);
	}

	public static boolean shouldShow() {
		return !sessionSeen && !NCRConfig.getClient().skipRealmsWarning();
	}

}
