package com.aizistral.nochatreports.common.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class AdvancedImageButton extends ImageButton {
	protected final Screen parent;
	protected final SwitchableSprites switchable;

	public AdvancedImageButton(int x, int y, int xSize, int ySize, SwitchableSprites sprites, OnPress onPress,
			Component name, Screen parent) {
		super(x, y, xSize, ySize, sprites.getDefault(), onPress, name);

		this.parent = parent;
		this.switchable = sprites;
	}

	public void useSprites(int index) {
		this.switchable.setIndex(index);
	}

	public int getSpritesIndex() {
		return this.switchable.getIndex();
	}

	public ResourceLocation getCurrentTexture() {
		return this.switchable.getCurrent().get(this.isActive(), this.isHoveredOrFocused());
	}

	@Override
	public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.getCurrentTexture(), this.getX(), this.getY(), this.width, this.height);

		if (this.isHovered)
			if (this.tooltip instanceof AdvancedWidgetTooltipHolder holder && holder.hasCustomRender()) {
				holder.doCustomRender(this.parent, graphics, mouseX, mouseY, holder.createTooltipPositioner(
						this.getRectangle(), this.isHovered(), this.isFocused()));
			}
	}

	@Override
	public void setTooltip(Tooltip tooltip) {
		if (tooltip instanceof AdvancedTooltip) {
			this.tooltip = new AdvancedWidgetTooltipHolder();
		}

		super.setTooltip(tooltip);
	}

}
