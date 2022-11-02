package com.aizistral.nochatreports.gui;

import com.aizistral.nochatreports.NoChatReports;
import com.aizistral.nochatreports.NoChatReportsClient;
import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.core.ServerSafetyState;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public final class UnsafeServerScreen extends WarningScreen {
	private static final Component TITLE = Component.translatable("gui.nochatreports.signing_required.header").withStyle(ChatFormatting.BOLD);
	private static final Component CONTENT = Component.translatable("gui.nochatreports.signing_required.contents");
	private static final Component CHECK = Component.translatable("gui.nochatreports.signing_required.check");
	private static final Component NARRATION = TITLE.copy().append("\n").append(CONTENT);
	private static boolean hideThisSession = false;

	private final Screen previous;

	public UnsafeServerScreen(Screen previous) {
		super(TITLE, CONTENT, CHECK, NARRATION);
		this.previous = previous;
	}

	@Override
	protected void initButtons(int i) {
		this.addRenderableWidget(Button.builder(Component.translatable("gui.nochatreports.signing_required.allow_signing"),
				button -> {
					ServerSafetyState.setAllowChatSigning(true);
					this.minecraft.setScreen(this.previous);
				}).pos(this.width / 2 - 260 + 28, 100 + i).size(150, 20)
				.build());

		ServerAddress address = ServerSafetyState.getLastServer();
		var whitelist = Button.builder(Component.translatable("gui.nochatreports.signing_required.whitelist_server"),
				button -> {
					if (address != null) {
						NCRConfig.getServerWhitelist().add(address);
						NCRConfig.getServerWhitelist().saveFile();
					}

					ServerSafetyState.setAllowChatSigning(true);
					this.minecraft.setScreen(this.previous);
				}).pos(this.width / 2 - 100 + 28, 100 + i).size(150, 20)
				.build();

		if (address == null) {
			whitelist.active = false;
		}

		this.addRenderableWidget(whitelist);

		this.addRenderableWidget(Button.builder(Component.translatable("gui.nochatreports.signing_required.cancel"),
				button -> {
					if (this.stopShowing.selected()) {
						hideThisSession = true;
					}
					this.minecraft.setScreen(this.previous);
				}).pos(this.width / 2 + 60 + 28, 100 + i).size(150, 20).build());
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
