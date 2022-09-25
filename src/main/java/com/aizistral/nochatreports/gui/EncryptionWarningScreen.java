package com.aizistral.nochatreports.gui;

import com.aizistral.nochatreports.NoChatReportsClient;
import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.core.ServerSafetyState;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.CommandBlockEditScreen;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class EncryptionWarningScreen extends WarningScreen {
	private static final Component TITLE = Component.translatable("gui.nochatreports.encryption_warning.header").withStyle(ChatFormatting.BOLD);
	private static final Component CONTENT = Component.translatable("gui.nochatreports.encryption_warning.contents");
	private static final Component CHECK = Component.translatable("gui.nochatreports.encryption_warning.check");
	private static final Component NARRATION = TITLE.copy().append("\n").append(CONTENT);
	private static final String WIKI_LINK = "https://github.com/Aizistral-Studios/No-Chat-Reports/wiki/To-Encrypt-or-Not-to-Encrypt";
	private static boolean sessionSeen = false;

	private final Screen previous;

	public EncryptionWarningScreen(Screen previous) {
		super(TITLE, CONTENT, CHECK, NARRATION);
		this.previous = previous;
	}

	@Override
	protected void initButtons(int i) {
		int offset = 28;

		this.addRenderableWidget(new Button(this.width / 2 - 260 + offset, 100 + i, 150, 20, CommonComponents.GUI_PROCEED, button -> {
			this.minecraft.setScreen(new EncryptionConfigScreen(this.previous));
			if (this.stopShowing.selected()) {
				NCRConfig.getEncryption().disableWarning();
			}

			sessionSeen = true;
		}));
		this.addRenderableWidget(new Button(this.width / 2 - 100 + offset, 100 + i, 150, 20, Component.translatable("gui.nochatreports.encryption_warning.learn_more"), button -> {
			Minecraft.getInstance().setScreen(new ConfirmLinkScreen(agree -> {
				if (agree) {
					Util.getPlatform().openUri(WIKI_LINK);
				}

				Minecraft.getInstance().setScreen(this);
			}, WIKI_LINK, true));
		}));
		this.addRenderableWidget(new Button(this.width / 2 + 60 + offset, 100 + i, 150, 20, CommonComponents.GUI_BACK, button -> {
			this.minecraft.setScreen(this.previous);
		}));
	}

	public static boolean seenOnThisSession() {
		return sessionSeen;
	}

}
