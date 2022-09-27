package com.aizistral.nochatreports.gui;

import org.jetbrains.annotations.Nullable;

import com.aizistral.nochatreports.NoChatReportsClient;
import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.CommandBlockEditScreen;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

@Environment(EnvType.CLIENT)
public class EncryptionWarningScreen extends Screen {
	private static final Component TITLE = Component.translatable("gui.nochatreports.encryption_warning.header").withStyle(ChatFormatting.BOLD);
	private static final Component CONTENT = Component.translatable("gui.nochatreports.encryption_warning.contents");
	private static final Component CHECK = Component.translatable("gui.nochatreports.encryption_warning.check");
	private static final Component NARRATION = TITLE.copy().append("\n").append(CONTENT);
	private static final String WIKI_LINK = "https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/To-Encrypt-or-Not-to-Encrypt";
	private static boolean sessionSeen = false;
	private final Screen previous;
	private MultiLineLabel message = MultiLineLabel.EMPTY;
	private Checkbox stopShowing = null;

	public EncryptionWarningScreen(Screen previous) {
		super(TITLE);
		this.previous = previous;
	}

	@Override
	protected void init() {
		this.clearWidgets();
		super.init();
		this.message = MultiLineLabel.create(this.font, CONTENT, this.width - 100);
		int i = (this.message.getLineCount() + 1) * this.getLineHeight();
		if (CHECK != null) {
			int checkY = this.hugeGUI() ? 32 : 76;
			int j = this.font.width(CHECK);
			this.stopShowing = new Checkbox(this.width / 2 - j / 2 - 8, checkY + i, j + 24, 20, CHECK, false);
			this.addRenderableWidget(this.stopShowing);
		}
		this.initButtons(i);
	}

	private void initButtons(int i) {
		int offset = 28;
		int buttonY = this.hugeGUI() ? 60 : 100;

		this.addRenderableWidget(new Button(this.width / 2 - 260 + offset, buttonY + i, 150, 20, CommonComponents.GUI_PROCEED, button -> {
			this.minecraft.setScreen(new EncryptionConfigScreen(this.previous));
			if (this.stopShowing.selected()) {
				NCRConfig.getEncryption().disableWarning();
			}

			sessionSeen = true;
		}));
		this.addRenderableWidget(new Button(this.width / 2 - 100 + offset, buttonY + i, 150, 20, Component.translatable("gui.nochatreports.encryption_warning.learn_more"), button -> {
			Minecraft.getInstance().setScreen(new ConfirmLinkScreen(agree -> {
				if (agree) {
					Util.getPlatform().openUri(WIKI_LINK);
				}

				Minecraft.getInstance().setScreen(this);
			}, WIKI_LINK, true));
		}));
		this.addRenderableWidget(new Button(this.width / 2 + 60 + offset, buttonY + i, 150, 20, CommonComponents.GUI_BACK, button -> {
			this.minecraft.setScreen(this.previous);
		}));
	}

	@Override
	public void render(PoseStack poseStack, int i, int j, float f) {
		this.renderBackground(poseStack);
		this.renderTitle(poseStack);
		int k = this.width / 2 - this.message.getWidth() / 2;
		this.message.renderLeftAligned(poseStack, k, this.hugeGUI() ? 40 : 70, this.getLineHeight(), 0xFFFFFF);
		super.render(poseStack, i, j, f);
	}

	private void renderTitle(PoseStack poseStack) {
		drawString(poseStack, this.font, this.title, 25, this.hugeGUI() ? 15 : 30, 0xFFFFFF);
	}

	private int getLineHeight() {
		if (this.hugeGUI())
			return (int) (this.minecraft.font.lineHeight * 1.5) + 1;
		else
			return this.minecraft.font.lineHeight * 2;
	}

	private boolean hugeGUI() {
		return this.height <= 1080 / 4;
	}

	public static boolean seenOnThisSession() {
		return sessionSeen;
	}

}
