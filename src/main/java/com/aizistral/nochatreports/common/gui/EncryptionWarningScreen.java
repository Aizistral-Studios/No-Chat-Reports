package com.aizistral.nochatreports.common.gui;

import com.aizistral.nochatreports.common.config.NCRConfig;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class EncryptionWarningScreen extends TriageWarningScreen {
	private static final Text TITLE = Text.translatable("gui.nochatreports.encryption_warning.header").formatted(Formatting.BOLD);
	private static final Text CONTENT = Text.translatable("gui.nochatreports.encryption_warning.contents");
	private static final Text CHECK = Text.translatable("gui.nochatreports.encryption_warning.check");
	private static final String WIKI_LINK = "https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/To-Encrypt-or-Not-to-Encrypt";
	private static boolean sessionSeen = false;

	public EncryptionWarningScreen(Screen previous) {
		super(TITLE, CONTENT, CHECK, WIKI_LINK, previous);
	}

	@Override
	protected void onProceed(ButtonWidget button) {
		this.client.setScreen(new EncryptionConfigScreen(this.previous));
		if (this.stopShowing.isChecked()) {
			NCRConfig.getEncryption().disableWarning();
		}

		sessionSeen = true;
	}

	@Override
	protected void onBack(ButtonWidget button) {
		this.client.setScreen(this.previous);
	}

	public static boolean seenOnThisSession() {
		return sessionSeen;
	}

}
