package com.aizistral.nochatreports.common.gui;

import com.aizistral.nochatreports.common.config.NCRConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class RealmsWarningScreen extends TriageWarningScreen {
	private static final Text TITLE = Text.translatable("gui.nochatreports.realms_warning.header").formatted(Formatting.BOLD);
	private static final Text CONTENT = Text.translatable("gui.nochatreports.realms_warning.contents");
	private static final Text CHECK = Text.translatable("gui.nochatreports.realms_warning.check");
	private static final String WIKI_LINK = "https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/The-Realms-Question";
	private static boolean sessionSeen = false;
	private final Screen realms;

	public RealmsWarningScreen(Screen previous, Screen realms) {
		super(TITLE, CONTENT, CHECK, WIKI_LINK, previous);
		this.realms = realms;
	}

	@Override
	protected void onProceed(ButtonWidget button) {
		this.client.setScreen(this.realms);
		if (this.stopShowing.isChecked()) {
			NCRConfig.getClient().setSkipRealmsWarning(true);
		}

		sessionSeen = true;
	}

	@Override
	protected void onBack(ButtonWidget button) {
		this.client.setScreen(null);
	}

	public static boolean shouldShow() {
		return !sessionSeen && !NCRConfig.getClient().skipRealmsWarning();
	}

}
