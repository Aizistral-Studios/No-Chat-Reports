package com.aizistral.nochatreports.common.gui;

import java.util.Objects;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.config.NCRConfigEncryption;
import com.aizistral.nochatreports.common.encryption.Encryption;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;

@Environment(EnvType.CLIENT)
public class EncryptionConfigScreen extends Screen {
	private static final ResourceLocation ENCRYPTION_ICONS = new ResourceLocation("nochatreports", "textures/gui/encryption_gui_icons.png");
	private static final Component HEADER = Component.translatable("gui.nochatreports.encryption_config.header");
	private static final Component KEY_DESC = Component.translatable("gui.nochatreports.encryption_config.key_desc");
	private static final Component PASS_DESC = Component.translatable("gui.nochatreports.encryption_config.passphrase_desc");
	private static final Component VALIDATION_OK = Component.translatable("gui.nochatreports.encryption_config.validation_ok");
	private static final Component VALIDATION_FAILED = Component.translatable("gui.nochatreports.encryption_config.validation_failed");
	private static final Component DICE_TOOLTIP = Component.translatable("gui.nochatreports.encryption_config.dice_tooltip");
	private static final Component PASS_NOT_ALLOWED = Component.translatable("gui.nochatreports.encryption_config.pass_not_allowed");
	private static final Component ENCRYPT_PUBLIC = Component.translatable("gui.nochatreports.encryption_config.encrypt_public");

	private static final int FIELDS_Y_START = 45;
	private final Screen previous;
	private CustomEditBox keyField, passField;
	private ImageButton validationIcon;
	private CycleButton<Encryption> algorithmButton;
	private MultiLineLabel keyDesc = MultiLineLabel.EMPTY, passDesc = MultiLineLabel.EMPTY;
	protected Checkbox encryptPublicCheck;
	private boolean settingPassKey = false;

	public EncryptionConfigScreen(Screen previous) {
		super(CommonComponents.EMPTY);
		this.previous = previous;
	}

	private NCRConfigEncryption getConfig() {
		return NCRConfig.getEncryption();
	}

	@Override
	protected void init() {
		this.clearWidgets();
		super.init();

		int w = (int) (this.width * (this.hugeGUI() ? 0.9 : 0.7));

		this.keyDesc = MultiLineLabel.create(this.font, KEY_DESC, w - 5);
		int keyDescSpace = (this.keyDesc.getLineCount() + 1) * this.getLineHeight();

		this.passDesc = MultiLineLabel.create(this.font, PASS_DESC, w - 5);
		int passDescSpace = (this.passDesc.getLineCount() + 1) * this.getLineHeight();

		w -= 52;

		this.keyField = new CustomEditBox(this.font, (this.width - w)/2 - 2, (this.hugeGUI() ? 25 : FIELDS_Y_START) + keyDescSpace - 15,
				w, 18, CommonComponents.EMPTY);
		this.keyField.setMaxLength(512);
		this.keyField.setResponder(this::onKeyUpdate);
		this.addWidget(this.keyField);

		var button = new AdvancedImageButton(this.keyField.getX() + this.keyField.getWidth() - 15, this.keyField.getY() + 3, 12,
				12, 0, 0, 0, ENCRYPTION_ICONS, 64, 64, btn -> {}, Component.empty(), this);
		button.setTooltip(new AdvancedTooltip(() -> this.validationIcon != null &&
				this.validationIcon.yTexStart == 0 ? VALIDATION_OK : VALIDATION_FAILED).setMaxWidth(250));
		button.active = false;
		button.visible = true;

		this.addRenderableOnly(this.validationIcon = button);

		button = new AdvancedImageButton(this.keyField.getX() - 22, this.keyField.getY() - 0, 18, 18, 0,
				28, 0, ENCRYPTION_ICONS, 64, 64, btn -> {}, Component.empty(), this);
		button.active = false;
		button.visible = true;

		this.addRenderableOnly(button);

		button = new AdvancedImageButton(this.keyField.getX() + this.keyField.getWidth() + 4, this.keyField.getY() - 1, 23, 20, 41,
				24, 20, ENCRYPTION_ICONS, 64, 64, btn -> {
					this.unfocusFields();
					this.keyField.setValue(this.algorithmButton.getValue().getRandomKey());
				}, Component.empty(), this);
		button.setTooltip(new AdvancedTooltip(DICE_TOOLTIP).setMaxWidth(250));
		button.active = true;
		button.visible = true;

		this.addRenderableWidget(button);

		w += 25;

		this.passField = new CustomEditBox(this.font, (this.width - w)/2 + 11, this.keyField.getY() +
				this.keyField.getHeight() + passDescSpace + (this.hugeGUI() ? -3 : 13), w, 18, CommonComponents.EMPTY);
		this.passField.setMaxLength(512);
		this.passField.setResponder(this::onPassphraseUpdate);
		this.addWidget(this.passField);

		button = new AdvancedImageButton(this.passField.getX() - 22, this.passField.getY() - 0, 18, 18, 0,
				46, 0, ENCRYPTION_ICONS, 64, 64, btn -> {}, Component.empty(), this);
		button.active = false;
		button.visible = true;

		this.addRenderableOnly(button);

		int checkWidth = this.font.width(ENCRYPT_PUBLIC);
		this.encryptPublicCheck = new Checkbox(this.width / 2 - checkWidth / 2 - 8, this.passField.getY() + 24, checkWidth + 24, 20,
				ENCRYPT_PUBLIC, NCRConfig.getEncryption().shouldEncryptPublic());
		this.addRenderableWidget(this.encryptPublicCheck);

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, btn -> {
			this.onDone();
			this.onClose();
		}).pos(this.width / 2 + 4, this.passField.getY() + 48).size(219, 20).build());

		CycleButton<Encryption> cycle = CycleButton.<Encryption>builder(value -> {
			return Component.translatable("gui.nochatreports.encryption_config.algorithm",
					Component.translatable("algorithm.nochatreports." + value.getID() + ".name"));
		}).withValues(Encryption.getRegistered()).displayOnlyValue().withInitialValue(this.getConfig()
				.getAlgorithm()).withTooltip(value -> new AdvancedTooltip(Component.translatable(
						"algorithm.nochatreports." + value.getID())).setMaxWidth(250))
				.create(this.width / 2 - 4 - 218, this.passField.getY() + 48, 218, 20, CommonComponents.EMPTY,
						(cycleButton, value) -> {
							this.unfocusFields();
							this.onAlgorithmUpdate(value);
						});

		this.addRenderableWidget(this.algorithmButton = cycle);

		this.onAlgorithmUpdate(this.algorithmButton.getValue());

		if (!StringUtil.isNullOrEmpty(this.getConfig().getEncryptionPassphrase())) {
			this.passField.setValue(this.getConfig().getEncryptionPassphrase());
		} else if (!StringUtil.isNullOrEmpty(this.getConfig().getEncryptionKey())) {
			if (!Objects.equals(this.getConfig().getEncryptionKey(), this.algorithmButton.getValue()
					.getDefaultKey())) {
				this.keyField.setValue(this.getConfig().getEncryptionKey());
			} else {
				this.keyField.setValue("");
			}
		}
	}

	@Override
	public void render(GuiGraphics graphics, int i, int j, float f) {
		if (!this.passField.isActive()) {
			if (this.passField.isFocused()) {
				this.passField.setFocused(false);
			}
			this.passField.setEditable(false);
		}

		this.renderBackground(graphics);
		graphics.drawCenteredString(this.font, HEADER, this.width / 2, this.hugeGUI() ? 8 : 16, 0xFFFFFF);

		this.keyDesc.renderLeftAligned(graphics, this.keyField.getX() - 20, (this.hugeGUI() ? 25 : FIELDS_Y_START), this.getLineHeight(), 0xFFFFFF);

		this.keyField.render(graphics, i, j, f);

		this.passDesc.renderLeftAligned(graphics, this.passField.getX() - 20, this.keyField.getY() + this.keyField.getHeight() + (this.hugeGUI() ? 12 : 28),
				this.getLineHeight(), 0xFFFFFF);

		this.passField.render(graphics, i, j, f);

		//		if (this.algorithmButton != null && this.algorithmButton.isMouseOver(i, j)) {
		//			this.renderTooltip(poseStack, this.algorithmButton.getTooltip(), i, j);
		//		}

		super.render(graphics, i, j, f);

		if (StringUtil.isNullOrEmpty(this.keyField.getValue()) && !this.keyField.isFocused()) {
			graphics.drawString(this.font,
					Component.translatable("gui.nochatreports.encryption_config.default_key",
							this.algorithmButton.getValue().getDefaultKey()), this.keyField.getX() + 4,
					this.keyField.getY() + 5, 0x999999);
		}

		if (!this.passField.active) {
			graphics.drawString(this.font, PASS_NOT_ALLOWED, this.passField.getX() + 4,
					this.passField.getY() + 5, 0x999999);
			RenderSystem.enableDepthTest();
			graphics.blit(ENCRYPTION_ICONS, this.passField.getX() - 20, this.passField.getY() + 3, 50, 0, 14, 13, 64, 64);
		}
	}

	private int getLineHeight() {
		if (this.hugeGUI())
			return (int) (this.minecraft.font.lineHeight * 1.5) + 1;
		else
			return this.minecraft.font.lineHeight * 2;
	}

	@Override
	public void tick() {
		this.keyField.tick();
	}

	private void onKeyUpdate(String key) {
		if (!this.settingPassKey) {
			this.passField.setValue("");
		}

		if (!StringUtil.isNullOrEmpty(key)) {
			this.validationIcon.yTexStart = this.algorithmButton.getValue().validateKey(key) ? 0 : 12;
		} else {
			this.validationIcon.yTexStart = 0;
		}
	}

	private void onPassphraseUpdate(String pass) {
		Encryption encryption = this.algorithmButton.getValue();

		this.settingPassKey = true;
		if (!StringUtil.isNullOrEmpty(pass)) {
			if (encryption.supportsPassphrases()) {
				this.keyField.setValue(encryption.getPassphraseKey(pass));
			}
		} else {
			this.onKeyUpdate(this.keyField.getValue());
		}
		this.settingPassKey = false;
	}

	private void onAlgorithmUpdate(Encryption encryption) {
		if (!encryption.supportsPassphrases()) {
			this.passField.setFocused(false);
			this.passField.setEditable(this.passField.active = false);
			this.onKeyUpdate(this.keyField.getValue());
		} else {
			this.passField.setEditable(this.passField.active = true);
			this.onPassphraseUpdate(this.passField.getValue());
		}
	}

	private void unfocusFields() {
		this.keyField.setFocused(false);
		this.passField.setFocused(false);
	}

	private void onDone() {
		var config = NCRConfig.getEncryption();
		var encryption = this.algorithmButton.getValue();
		config.setAlgorithm(encryption);
		config.setEncryptionKey(!StringUtil.isNullOrEmpty(this.keyField.getValue()) ? this.keyField.getValue()
				: encryption.getDefaultKey());
		config.setEncryptPublic(this.encryptPublicCheck.selected());
	}

	private boolean hugeGUI() {
		return this.height <= 1080 / 4;
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(this.previous);
	}

	private static class CustomEditBox extends EditBox {
		public CustomEditBox(Font font, int i, int j, int k, int l, Component component) {
			super(font, i, j, k, l, component);
		}

		@Override
		public void setFocused(boolean bl) {
			super.setFocused(bl);
		}
	}

}
