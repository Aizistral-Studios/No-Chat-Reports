package com.aizistral.nochatreports.common.gui;

import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public abstract class AdaptiveWarningScreen extends Screen {
	private final Text title;
	private final Text content;
	private final Text narration;
	@Nullable
	protected final Screen previous;
	@Nullable
	private final Text check;
	@Nullable
	protected CheckboxWidget stopShowing = null;
	protected MultilineText message = MultilineText.EMPTY;

	public AdaptiveWarningScreen(Text title, Text content, @Nullable Text check,
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
		this.clearChildren();
		super.init();
		this.message = MultilineText.create(this.textRenderer, this.content, this.width - (this.hugeGUI() ? 65 : 100));
		int i = (this.message.count() + 1) * this.getLineHeight();
		if (this.check != null) {
			int checkY = this.hugeGUI() ? 27 : 76;
			int j = this.textRenderer.getWidth(this.check);

			if (this.check != null) {
				this.stopShowing = new CheckboxWidget(this.width / 2 - j / 2 - 8, checkY + i, j + 24, 20, this.check, false);
				this.addDrawableChild(this.stopShowing);
			}
		}
		this.initButtons(i + (this.hugeGUI() ? 55 : 100));
	}

	protected abstract void initButtons(int y);

	@Override
	public void render(MatrixStack poseStack, int i, int j, float f) {
		this.renderBackground(poseStack);
		this.renderTitle(poseStack);
		int k = this.width / 2 - this.message.getMaxWidth() / 2;
		this.message.drawWithShadow(poseStack, k, this.hugeGUI() ? 35 : 70, this.getLineHeight(), 0xFFFFFF);
		super.render(poseStack, i, j, f);
	}

	private void renderTitle(MatrixStack poseStack) {
		drawTextWithShadow(poseStack, this.textRenderer, this.title, 25, this.hugeGUI() ? 15 : 30, 0xFFFFFF);
	}

	private boolean hugeGUI() {
		return this.height <= 1080 / 4;
	}

	protected int getLineHeight() {
		if (this.hugeGUI())
			return (int) (this.client.textRenderer.fontHeight * 1.5) + 1;
		else
			return this.client.textRenderer.fontHeight * 2;
	}

}