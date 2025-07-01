package com.aizistral.nochatreports.common.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

public class AdvancedIconButton extends Button {
    protected final Screen parent;
    protected final SwitchableButtonIcon icons;

    public AdvancedIconButton(int x, int y, int width, int height, SwitchableButtonIcon icons, OnPress onPress, Screen parent) {
        super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        this.icons = icons;
        this.parent = parent;
    }

    public void setIconIndex(int index) {
        icons.setIndex(index);
    }

    public int getIconIndex() {
        return icons.getIndex();
    }

    public ButtonIconData getCurrentIcon() {
        return icons.getCurrent();
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.renderWidget(graphics, mouseX, mouseY, delta);

        IconData icon = getCurrentIcon().getIconData(this.active);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, icon.iconTexture(),
                this.getX() + this.width / 2 - icon.textureWidth() / 2,
                this.getY() + this.getHeight() / 2 - icon.textureHeight() / 2,
                icon.textureWidth(), icon.textureHeight());

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
