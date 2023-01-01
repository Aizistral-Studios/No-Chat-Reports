package com.aizistral.nochatreports.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
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
	public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float delta) {
		super.renderButton(poseStack, mouseX, mouseY, delta);

		if (this.isHovered)
			if (this.tooltip instanceof AdvancedTooltip tooltip && tooltip.hasCustomRender()) {
				tooltip.doCustomRender(this.parent, poseStack, mouseX, mouseY);
			}
	}

}
