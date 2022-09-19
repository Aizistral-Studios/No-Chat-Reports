package com.aizistral.nochatreports.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractCommandBlockEditScreen;
import net.minecraft.client.gui.screens.inventory.CommandBlockEditScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BaseCommandBlock;

@Environment(EnvType.CLIENT)
public class EncryptionConfigScreen extends Screen {
	private static final ResourceLocation ENCRYPTION_ICONS = new ResourceLocation("nochatreports", "textures/gui/encryption_gui_icons.png");
	private static final int FIELDS_Y_START = 40;
	private final Screen previous;
	protected EditBox keyField;
	private MultiLineLabel keyDesc = MultiLineLabel.EMPTY;

	protected EncryptionConfigScreen(Screen previous) {
		super(CommonComponents.EMPTY);
		this.previous = previous;
	}

	@Override
	protected void init() {
		super.init();

		int w = (int) (this.width * 0.7);

		this.keyDesc = MultiLineLabel.create(this.font, Component.translatable("gui.nochatreports.encryption_config.key_desc"), w);
		int i = (this.keyDesc.getLineCount() + 1) * this.getLineHeight();

		w -= 6;

		this.keyField = new EditBox(this.font, (this.width - w)/2, FIELDS_Y_START + i, w, 20, CommonComponents.EMPTY);
		this.keyField.setMaxLength(32500);
		this.keyField.setResponder(this::onEdited);
		this.addWidget(this.keyField);
	}

	@Override
	public void render(PoseStack poseStack, int i, int j, float f) {
		this.renderBackground(poseStack);
		AbstractCommandBlockEditScreen.drawCenteredString(poseStack, this.font, Component.literal("Set command"), this.width / 2, 10, 0xFFFFFF);

		int k = (this.width - this.keyDesc.getWidth()) / 2;
		this.keyDesc.renderLeftAligned(poseStack, k, FIELDS_Y_START, this.getLineHeight(), 0xFFFFFF);

		this.keyField.render(poseStack, i, j, f);
		super.render(poseStack, i, j, f);
	}

	protected int getLineHeight() {
		return this.font.lineHeight * 2;
	}

	@Override
	public void tick() {
		this.keyField.tick();
	}

	private void onEdited(String string) {
		// TODO Validate
	}

	protected void onDone() {
		// NO-OP
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(this.previous);
	}


}
