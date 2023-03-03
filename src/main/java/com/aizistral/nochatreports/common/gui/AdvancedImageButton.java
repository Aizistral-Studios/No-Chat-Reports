package com.aizistral.nochatreports.common.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AdvancedImageButton extends TexturedButtonWidget {
	protected final Screen parent;

	public AdvancedImageButton(int x, int y, int xSize, int ySize, int xTexStart, int yTexStart, int yStateDiff,
			Identifier texture, int textureWidth, int textureHeight, PressAction onPress, Text name,
			Screen parent) {
		super(x, y, xSize, ySize, xTexStart, yTexStart, yStateDiff, texture, textureWidth, textureHeight, onPress, name);
		this.parent = parent;
	}

	@Override
	public void applyTooltip() {
		if (this.tooltip instanceof AdvancedTooltip tooltip && tooltip.hasCustomRender())
			return;

		super.applyTooltip();
	}

	@Override
	public void renderButton(MatrixStack poseStack, int mouseX, int mouseY, float delta) {
		super.renderButton(poseStack, mouseX, mouseY, delta);

		if (this.hovered)
			if (this.tooltip instanceof AdvancedTooltip tooltip && tooltip.hasCustomRender()) {
				tooltip.doCustomRender(this.parent, poseStack, mouseX, mouseY, this.getTooltipPositioner());
			}
	}

}
