package com.aizistral.nochatreports.common.gui;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerSafetyState;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public final class UnsafeServerScreen extends AdaptiveWarningScreen {
	private static final Text TITLE = Text.translatable("gui.nochatreports.signing_required.header").formatted(Formatting.BOLD);
	private static final Text CONTENT = Text.translatable("gui.nochatreports.signing_required.contents");
	private static final Text CHECK = Text.translatable("gui.nochatreports.signing_required.check");
	private static final Text NARRATION = TITLE.copy().append("\n").append(CONTENT);
	private static boolean hideThisSession = false;

	public UnsafeServerScreen(Screen previous) {
		super(TITLE, CONTENT, CHECK, previous);
	}

	@Override
	protected void initButtons(int i) {
		this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.nochatreports.signing_required.allow_signing"),
				button -> {
					ServerSafetyState.setAllowChatSigning(true);
					this.client.setScreen(this.previous);
				}).dimensions(this.width / 2 - 155, i, 150, 20)
				.build());

		this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.nochatreports.signing_required.cancel"),
				button -> {
					if (this.stopShowing.isChecked()) {
						hideThisSession = true;
					}
					this.client.setScreen(this.previous);
				}).dimensions(this.width / 2 - 155 + 160, i, 150, 20).build());
	}

	private void resendLastMessage() {
		if (this.previous instanceof ChatScreen chat) {
			chat.sendMessage(NCRConfig.getEncryption().getLastMessage(), false);
		}
	}

	public static void setHideThisSession(boolean hide) {
		hideThisSession = hide;
	}

	public static boolean hideThisSession() {
		return hideThisSession;
	}

}
