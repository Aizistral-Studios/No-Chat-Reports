package com.aizistral.nochatreports.gui;

import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.mixins.client.MixinChatScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EncryptionButton extends ImageButton {
	private final Screen parent;

	public EncryptionButton(int x, int y, int xSize, int ySize, int xTexStart, int yTexStart, int yStateDiff,
			ResourceLocation texture, int textureWidth, int textureHeight, OnPress onPress, OnTooltip onTooltip,
			Component name, Screen parent) {
		super(x, y, xSize, ySize, xTexStart, yTexStart, yStateDiff, texture, textureWidth, textureHeight, onPress,
				onTooltip, name);
		this.parent = parent;
	}

	@Override
	public boolean mouseClicked(double x, double y, int i) {
		if (!this.active || !this.visible)
			return false;

		if (i == 1 && this.clicked(x, y)) {
			this.playDownSound(Minecraft.getInstance().getSoundManager());
			this.openEncryptionConfig();
			return true;
		}

		return super.mouseClicked(x, y, i);
	}

	public void openEncryptionConfig() {
		if (!EncryptionWarningScreen.seenOnThisSession() && !NCRConfig.getEncryption().isWarningDisabled()
				&& !NCRConfig.getEncryption().isValid()) {
			Minecraft.getInstance().setScreen(new EncryptionWarningScreen(this.parent));
		} else {
			Minecraft.getInstance().setScreen(new EncryptionConfigScreen(this.parent));
		}
	}

}
