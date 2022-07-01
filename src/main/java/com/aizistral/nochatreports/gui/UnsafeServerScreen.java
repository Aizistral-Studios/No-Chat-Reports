package com.aizistral.nochatreports.gui;

import javax.print.attribute.standard.MediaSize.NA;

import com.aizistral.nochatreports.core.NoReportsConfig;
import com.aizistral.nochatreports.core.ServerSafetyState;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class UnsafeServerScreen extends WarningScreen {
	private static final Component TITLE = Component.translatable("gui.nochatreports.unsafe_server.header").withStyle(ChatFormatting.BOLD);
	private static final Component CONTENT = Component.translatable("gui.nochatreports.unsafe_server.contents");
	private static final Component CHECK = Component.translatable("gui.nochatreports.unsafe_server.check");
	private static final Component NARRATION = TITLE.copy().append("\n").append(CONTENT);

	private final Screen previous = new TitleScreen();
	private final Screen joinMultiplayer = new JoinMultiplayerScreen(new TitleScreen());

	public UnsafeServerScreen() {
		super(TITLE, CONTENT, CHECK, NARRATION);
	}

	@Override
	protected void initButtons(int i) {
		this.addRenderableWidget(new Button(this.width / 2 - 155, 100 + i, 150, 20, CommonComponents.GUI_PROCEED, button -> {
			ServerAddress address = ServerSafetyState.getLastConnectedServer();

			if (address != null) {
				if (this.stopShowing.selected()) {
					NoReportsConfig.getWhitelistedServers().add(address.getHost() + ":" + address.getPort());
					NoReportsConfig.saveConfig();
				} else {
					ServerSafetyState.setAllowsUnsafeServer(true);
				}

				ConnectScreen.startConnecting(this, this.minecraft, address, null);
			}
		}));
		this.addRenderableWidget(new Button(this.width / 2 - 155 + 160, 100 + i, 150, 20, CommonComponents.GUI_BACK, button -> {
			this.minecraft.setScreen(this.joinMultiplayer);
		}));
	}

}
