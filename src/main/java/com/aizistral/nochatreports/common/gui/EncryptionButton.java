package com.aizistral.nochatreports.common.gui;

import com.aizistral.nochatreports.common.config.NCRConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EncryptionButton extends AdvancedImageButton {

	public EncryptionButton(int x, int y, int xSize, int ySize, int useSprites, OnPress onPress, Component name,
			Screen parent) {
		super(x, y, xSize, ySize, getSprites(useSprites), onPress, name, parent);
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
				&& !NCRConfig.getEncryption().isEnabledAndValid()) {
			Minecraft.getInstance().setScreen(new EncryptionWarningScreen(this.parent));
		} else {
			Minecraft.getInstance().setScreen(new EncryptionConfigScreen(this.parent));
		}
	}

	private static SwitchableSprites getSprites(int useIndex) {
		SwitchableSprites sprites = SwitchableSprites.of(
				GUIShenanigans.getSprites("encryption/active_button"),
				GUIShenanigans.getSprites("encryption/inactive_button"),
				GUIShenanigans.getSprites("encryption/error_button")
				);
		sprites.setIndex(useIndex);
		return sprites;
	}

}
