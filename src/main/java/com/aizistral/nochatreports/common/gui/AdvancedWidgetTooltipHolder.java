package com.aizistral.nochatreports.common.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetTooltipHolder;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;

public class AdvancedWidgetTooltipHolder extends WidgetTooltipHolder {

	public AdvancedWidgetTooltipHolder() {
		super();
	}

	public AdvancedWidgetTooltipHolder(Tooltip tooltip) {
		this();
	}

	public boolean hasCustomRender() {
		return this.get() instanceof AdvancedTooltip advanced && advanced.hasCustomRender();
	}

	public void doCustomRender(Screen screen, GuiGraphics graphics, int x, int y, ClientTooltipPositioner positioner) {
		((AdvancedTooltip) this.get()).doCustomRender(screen, graphics, x, y, positioner);
	}

	@Override // ideally tooltip shouldn't control it's own render like this, but for now it does
	public void refreshTooltipForNextRenderPass(boolean hovered, boolean focused, ScreenRectangle screenRectangle) {
		if (this.hasCustomRender())
			return;

		super.refreshTooltipForNextRenderPass(hovered, focused, screenRectangle);
	}

}
