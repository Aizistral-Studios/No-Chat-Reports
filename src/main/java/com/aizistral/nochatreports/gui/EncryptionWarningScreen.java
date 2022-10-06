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
public class EncryptionWarningScreen extends ExtendedWarningScreen {
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
