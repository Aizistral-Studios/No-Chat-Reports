package com.aizistral.nochatreports.common.gui;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class AdvancedImageButton extends ImageButton {
	protected final Screen parent;

	public AdvancedImageButton(int x, int y, int xSize, int ySize, int xTexStart, int yTexStart, int yStateDiff,
			ResourceLocation texture, int textureWidth, int textureHeight, OnPress onPress, Component name,
			Screen parent) {
		super(x, y, xSize, ySize, xTexStart, yTexStart, yStateDiff, texture, textureWidth, textureHeight, onPress, name);
		this.parent = parent;
	}

	@Override
	public void updateTooltip() {
		if (this.tooltip instanceof AdvancedTooltip tooltip && tooltip.hasCustomRender())
			return;

		super.updateTooltip();
	}

	@Override
	public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		super.renderWidget(graphics, mouseX, mouseY, delta);

		if (this.isHovered)
			if (this.tooltip instanceof AdvancedTooltip tooltip && tooltip.hasCustomRender()) {
				tooltip.doCustomRender(this.parent, graphics, mouseX, mouseY, this.createTooltipPositioner());
			}
	}

}
