package com.aizistral.nochatreports.common.gui;

import com.aizistral.nochatreports.common.config.NCRConfig;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class EncryptionWarningScreen extends TriageWarningScreen {
	private static final Component TITLE = Component.translatable("gui.nochatreports.encryption_warning.header").withStyle(ChatFormatting.BOLD);
	private static final Component CONTENT = Component.translatable("gui.nochatreports.encryption_warning.contents");
	private static final Component CHECK = Component.translatable("gui.nochatreports.encryption_warning.check");
	private static final String WIKI_LINK = "https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/To-Encrypt-or-Not-to-Encrypt";
	private static boolean sessionSeen = false;

	public EncryptionWarningScreen(Screen previous) {
		super(TITLE, CONTENT, CHECK, WIKI_LINK, previous);
	}

	@Override
	protected void onProceed(Button button) {
		this.minecraft.setScreen(new EncryptionConfigScreen(this.previous));
		if (this.stopShowing.selected()) {
			NCRConfig.getEncryption().disableWarning();
		}

		sessionSeen = true;
	}

	@Override
	protected void onBack(Button button) {
		this.minecraft.setScreen(this.previous);
	}

	public static boolean seenOnThisSession() {
		return sessionSeen;
	}

}
