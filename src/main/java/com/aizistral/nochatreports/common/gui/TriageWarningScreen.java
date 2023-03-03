package com.aizistral.nochatreports.common.gui;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public abstract class TriageWarningScreen extends AdaptiveWarningScreen {
	private final String wikiLink;

	public TriageWarningScreen(Component title, Component content, Component check, String wikiLink, Screen previous) {
		super(title, content, check, previous);
		this.wikiLink = wikiLink;
	}

	@Override
	protected void initButtons(int y) {
		int offset = 28;

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_PROCEED, this::onProceed)
				.pos(this.width / 2 - 260 + offset, y).size(150, 20).build());

		this.addRenderableWidget(Button.builder(Component.translatable("gui.nochatreports.encryption_warning.learn_more"), button -> {
			Minecraft.getInstance().setScreen(new ConfirmLinkScreen(agree -> {
				if (agree) {
					Util.getPlatform().openUri(this.wikiLink);
				}

				Minecraft.getInstance().setScreen(this);
			}, this.wikiLink, true));
		}).pos(this.width / 2 - 100 + offset, y).size(150, 20).build());


		this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, this::onBack)
				.pos(this.width / 2 + 60 + offset, y).size(150, 20).build());
	}

	protected abstract void onProceed(Button button);

	protected abstract void onBack(Button button);

}
