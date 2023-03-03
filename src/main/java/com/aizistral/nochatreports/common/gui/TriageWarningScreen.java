package com.aizistral.nochatreports.common.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public abstract class TriageWarningScreen extends AdaptiveWarningScreen {
	private final String wikiLink;

	public TriageWarningScreen(Text title, Text content, Text check, String wikiLink, Screen previous) {
		super(title, content, check, previous);
		this.wikiLink = wikiLink;
	}

	@Override
	protected void initButtons(int y) {
		int offset = 28;

		this.addDrawableChild(ButtonWidget.builder(ScreenTexts.PROCEED, this::onProceed)
				.position(this.width / 2 - 260 + offset, y).size(150, 20).build());

		this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.nochatreports.encryption_warning.learn_more"), button -> {
			MinecraftClient.getInstance().setScreen(new ConfirmLinkScreen(agree -> {
				if (agree) {
					Util.getOperatingSystem().open(this.wikiLink);
				}

				MinecraftClient.getInstance().setScreen(this);
			}, this.wikiLink, true));
		}).position(this.width / 2 - 100 + offset, y).size(150, 20).build());


		this.addDrawableChild(ButtonWidget.builder(ScreenTexts.BACK, this::onBack)
				.position(this.width / 2 + 60 + offset, y).size(150, 20).build());
	}

	protected abstract void onProceed(ButtonWidget button);

	protected abstract void onBack(ButtonWidget button);

}
