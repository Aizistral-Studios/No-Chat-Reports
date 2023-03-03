package com.aizistral.nochatreports.common.gui;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerSafetyState;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public final class UnsafeServerScreen extends AdaptiveWarningScreen {
	private static final Component TITLE = Component.translatable("gui.nochatreports.signing_required.header").withStyle(ChatFormatting.BOLD);
	private static final Component CONTENT = Component.translatable("gui.nochatreports.signing_required.contents");
	private static final Component CHECK = Component.translatable("gui.nochatreports.signing_required.check");
	private static final Component NARRATION = TITLE.copy().append("\n").append(CONTENT);
	private static boolean hideThisSession = false;

	public UnsafeServerScreen(Screen previous) {
		super(TITLE, CONTENT, CHECK, previous);
	}

	@Override
	protected void initButtons(int i) {
		this.addRenderableWidget(Button.builder(Component.translatable("gui.nochatreports.signing_required.allow_signing"),
				button -> {
					ServerSafetyState.setAllowChatSigning(true);
					this.minecraft.setScreen(this.previous);
				}).bounds(this.width / 2 - 155, i, 150, 20)
				.build());

		this.addRenderableWidget(Button.builder(Component.translatable("gui.nochatreports.signing_required.cancel"),
				button -> {
					if (this.stopShowing.selected()) {
						hideThisSession = true;
					}
					this.minecraft.setScreen(this.previous);
				}).bounds(this.width / 2 - 155 + 160, i, 150, 20).build());
	}

	private void resendLastMessage() {
		if (this.previous instanceof ChatScreen chat) {
			chat.handleChatInput(NCRConfig.getEncryption().getLastMessage(), false);
		}
	}

	public static void setHideThisSession(boolean hide) {
		hideThisSession = hide;
	}

	public static boolean hideThisSession() {
		return hideThisSession;
	}

}
