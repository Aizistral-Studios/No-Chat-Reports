package com.aizistral.nochatreports.gui;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public abstract class ExtendedWarningScreen extends Screen {
	private final Component TITLE;
	private final Component CONTENT;
	private final Component CHECK;
	private final Component NARRATION;
	private final String WIKI_LINK;
	protected final Screen previous;
	protected MultiLineLabel message = MultiLineLabel.EMPTY;
	protected Checkbox stopShowing = null;

	public ExtendedWarningScreen(Component title, Component content, Component check, String wikiLink, Screen previous) {
		super(title);
		this.TITLE = title;
		this.CONTENT = content;
		this.CHECK = check;
		this.WIKI_LINK = wikiLink;
		this.NARRATION = this.TITLE.copy().append("\n").append(this.CONTENT);
		this.previous = previous;
	}

	@Override
	protected void init() {
		this.clearWidgets();
		super.init();
		this.message = MultiLineLabel.create(this.font, this.CONTENT, this.width - (this.hugeGUI() ? 65 : 100));
		int i = (this.message.getLineCount() + 1) * this.getLineHeight();
		if (this.CHECK != null) {
			int checkY = this.hugeGUI() ? 27 : 76;
			int j = this.font.width(this.CHECK);
			this.stopShowing = new Checkbox(this.width / 2 - j / 2 - 8, checkY + i, j + 24, 20, this.CHECK, false);
			this.addRenderableWidget(this.stopShowing);
		}
		this.initButtons(i);
	}

	private void initButtons(int i) {
		int offset = 28;
		int buttonY = this.hugeGUI() ? 55 : 100;

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_PROCEED, this::onProceed)
				.pos(this.width / 2 - 260 + offset, buttonY + i).size(150, 20).build());

		this.addRenderableWidget(Button.builder(Component.translatable("gui.nochatreports.encryption_warning.learn_more"), button -> {
			Minecraft.getInstance().setScreen(new ConfirmLinkScreen(agree -> {
				if (agree) {
					Util.getPlatform().openUri(this.WIKI_LINK);
				}

				Minecraft.getInstance().setScreen(this);
			}, this.WIKI_LINK, true));
		}).pos(this.width / 2 - 100 + offset, buttonY + i).size(150, 20).build());


		this.addRenderableWidget(Button.builder(CommonComponents.GUI_BACK, this::onBack)
				.pos(this.width / 2 + 60 + offset, buttonY + i).size(150, 20).build());
	}

	protected abstract void onProceed(Button button);

	protected abstract void onBack(Button button);

	@Override
	public void render(PoseStack poseStack, int i, int j, float f) {
		this.renderBackground(poseStack);
		this.renderTitle(poseStack);
		int k = this.width / 2 - this.message.getWidth() / 2;
		this.message.renderLeftAligned(poseStack, k, this.hugeGUI() ? 35 : 70, this.getLineHeight(), 0xFFFFFF);
		super.render(poseStack, i, j, f);
	}

	private void renderTitle(PoseStack poseStack) {
		drawString(poseStack, this.font, this.title, 25, this.hugeGUI() ? 15 : 30, 0xFFFFFF);
	}

	private boolean hugeGUI() {
		return this.height <= 1080 / 4;
	}

	protected int getLineHeight() {
		if (this.hugeGUI())
			return (int) (this.minecraft.font.lineHeight * 1.5) + 1;
		else
			return this.minecraft.font.lineHeight * 2;
	}

}
