package com.aizistral.nochatreports.common.gui;

import org.jetbrains.annotations.Nullable;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.Checkbox.OnValueChange;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class AdaptiveWarningScreen extends Screen {
	private final Component title;
	private final Component content;
	private final Component narration;
	@Nullable
	protected final Screen previous;
	@Nullable
	private final Component check;
	@Nullable
	protected Checkbox stopShowing = null;
	protected MultiLineLabel message = MultiLineLabel.EMPTY;

	public AdaptiveWarningScreen(Component title, Component content, @Nullable Component check,
			@Nullable Screen previous) {
		super(title);
		this.title = title;
		this.content = content;
		this.check = check;
		this.narration = this.title.copy().append("\n").append(this.content);
		this.previous = previous;
	}

	@Override
	protected void init() {
		this.clearWidgets();
		super.init();
		this.message = MultiLineLabel.create(this.font, this.content, this.width - (this.hugeGUI() ? 65 : 100));
		int i = (this.message.getLineCount() + 1) * this.getLineHeight();
		if (this.check != null) {
			int checkY = this.hugeGUI() ? 27 : 76;
			int j = this.font.width(this.check);

			if (this.check != null) {
				this.stopShowing = Checkbox.builder(this.check, this.font).pos(this.width / 2 - j / 2 - 8, checkY + i)
						.build();
				this.addRenderableWidget(this.stopShowing);
			}
		}
		this.initButtons(i + (this.hugeGUI() ? 55 : 100));
	}

	protected abstract void initButtons(int y);

	@Override
	public void render(GuiGraphics graphics, int i, int j, float f) {
		super.render(graphics, i, j, f);
		this.renderTitle(graphics);
		int k = this.width / 2 - this.message.getWidth() / 2;
		this.message.renderLeftAligned(graphics, k, this.hugeGUI() ? 35 : 70, this.getLineHeight(), 0xFFFFFF);
	}

	private void renderTitle(GuiGraphics graphics) {
		graphics.drawString(this.font, this.title, 25, this.hugeGUI() ? 15 : 30, 0xFFFFFF);
	}

	private boolean hugeGUI() {
		return this.height <= 1080 / 4;
	}

	protected int getLineHeight() {
		if (this.hugeGUI())
			return (int) (this.minecraft.font.lineHeight * 1.5) + 1;
		else
			return this.minecraft.font.lineHeight * 2;
	}

}