package com.aizistral.nochatreports.common.gui;

import com.aizistral.nochatreports.common.config.NCRConfig;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;

public class RealmsWarningScreen extends WarningScreen {
	private static final Component TITLE = Component.translatable("gui.nochatreports.realms_warning.header").withStyle(ChatFormatting.BOLD);
	private static final Component CONTENT = Component.translatable("gui.nochatreports.realms_warning.contents");
	private static final Component CHECK = Component.translatable("gui.nochatreports.realms_warning.check");
	private static final Component NARRATION = TITLE.copy().append("\n").append(CONTENT);
	private static final Component LEARN = Component.translatable("gui.nochatreports.realms_warning.learn_more");
	private static final String WIKI_LINK = "https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/The-Realms-Question";
	private static boolean sessionSeen = false;
	private final Screen previous;
	private final Screen realms;

	public RealmsWarningScreen(Screen previous, Screen realms) {
		super(TITLE, CONTENT, CHECK, NARRATION);
		this.previous = previous;
		this.realms = realms;
	}

	protected void onProceed(Button button) {
		this.minecraft.gui.setScreen(this.realms);
		if (this.stopShowing.selected()) {
			NCRConfig.getClient().setSkipRealmsWarning(true);
		}

		sessionSeen = true;
	}

	protected void onBack(Button button) {
		this.minecraft.gui.setScreen(this.previous);
	}

	public static boolean shouldShow() {
		return !sessionSeen && !NCRConfig.getClient().skipRealmsWarning();
	}

	@Override
	protected Layout addFooterButtons() {
		LinearLayout linearLayout = LinearLayout.horizontal().spacing(8);
		linearLayout.addChild(Button.builder(CommonComponents.GUI_PROCEED, this::onProceed).build());
		linearLayout.addChild(Button.builder(LEARN, button -> {
			Minecraft.getInstance().gui.setScreen(new ConfirmLinkScreen(agree -> {
				if (agree) {
					Util.getPlatform().openUri(WIKI_LINK);
				}

				Minecraft.getInstance().gui.setScreen(this);
			}, WIKI_LINK, true));
		}).build());
		linearLayout.addChild(Button.builder(CommonComponents.GUI_BACK, this::onBack).build());
		return linearLayout;
	}
}
