package com.aizistral.nochatreports.common.gui;

import java.util.Objects;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.config.NCRConfigEncryption;
import com.aizistral.nochatreports.common.encryption.Encryption;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringHelper;

@Environment(EnvType.CLIENT)
public class EncryptionConfigScreen extends Screen {
	private static final Identifier ENCRYPTION_ICONS = new Identifier("nochatreports", "textures/gui/encryption_gui_icons.png");
	private static final Text HEADER = Text.translatable("gui.nochatreports.encryption_config.header");
	private static final Text KEY_DESC = Text.translatable("gui.nochatreports.encryption_config.key_desc");
	private static final Text PASS_DESC = Text.translatable("gui.nochatreports.encryption_config.passphrase_desc");
	private static final Text VALIDATION_OK = Text.translatable("gui.nochatreports.encryption_config.validation_ok");
	private static final Text VALIDATION_FAILED = Text.translatable("gui.nochatreports.encryption_config.validation_failed");
	private static final Text DICE_TOOLTIP = Text.translatable("gui.nochatreports.encryption_config.dice_tooltip");
	private static final Text PASS_NOT_ALLOWED = Text.translatable("gui.nochatreports.encryption_config.pass_not_allowed");
	private static final Text ENCRYPT_PUBLIC = Text.translatable("gui.nochatreports.encryption_config.encrypt_public");

	private static final int FIELDS_Y_START = 45;
	private final Screen previous;
	private CustomEditBox keyField, passField;
	private TexturedButtonWidget validationIcon;
	private CyclingButtonWidget<Encryption> algorithmButton;
	private MultilineText keyDesc = MultilineText.EMPTY, passDesc = MultilineText.EMPTY;
	protected CheckboxWidget encryptPublicCheck;
	private boolean settingPassKey = false;

	public EncryptionConfigScreen(Screen previous) {
		super(ScreenTexts.EMPTY);
		this.previous = previous;
	}

	private NCRConfigEncryption getConfig() {
		return NCRConfig.getEncryption();
	}

	@Override
	protected void init() {
		this.clearChildren();
		super.init();

		int w = (int) (this.width * (this.hugeGUI() ? 0.9 : 0.7));

		this.keyDesc = MultilineText.create(this.textRenderer, KEY_DESC, w - 5);
		int keyDescSpace = (this.keyDesc.count() + 1) * this.getLineHeight();

		this.passDesc = MultilineText.create(this.textRenderer, PASS_DESC, w - 5);
		int passDescSpace = (this.passDesc.count() + 1) * this.getLineHeight();

		w -= 52;

		this.keyField = new CustomEditBox(this.textRenderer, (this.width - w)/2 - 2, (this.hugeGUI() ? 25 : FIELDS_Y_START) + keyDescSpace - 15,
				w, 18, ScreenTexts.EMPTY);
		this.keyField.setMaxLength(512);
		this.keyField.setChangedListener(this::onKeyUpdate);
		this.addSelectableChild(this.keyField);

		var button = new AdvancedImageButton(this.keyField.getX() + this.keyField.getWidth() - 15, this.keyField.getY() + 3, 12,
				12, 0, 0, 0, ENCRYPTION_ICONS, 64, 64, btn -> {}, Text.empty(), this);
		button.setTooltip(new AdvancedTooltip(() -> this.validationIcon != null &&
				this.validationIcon.v == 0 ? VALIDATION_OK : VALIDATION_FAILED).setMaxWidth(250));
		button.active = false;
		button.visible = true;

		this.addDrawable(this.validationIcon = button);

		button = new AdvancedImageButton(this.keyField.getX() - 22, this.keyField.getY() - 0, 18, 18, 0,
				28, 0, ENCRYPTION_ICONS, 64, 64, btn -> {}, Text.empty(), this);
		button.active = false;
		button.visible = true;

		this.addDrawable(button);

		button = new AdvancedImageButton(this.keyField.getX() + this.keyField.getWidth() + 4, this.keyField.getY() - 1, 23, 20, 41,
				24, 20, ENCRYPTION_ICONS, 64, 64, btn -> {
					this.unfocusFields();
					this.keyField.setText(this.algorithmButton.getValue().getRandomKey());
				}, Text.empty(), this);
		button.setTooltip(new AdvancedTooltip(DICE_TOOLTIP).setMaxWidth(250));
		button.active = true;
		button.visible = true;

		this.addDrawableChild(button);

		w += 25;

		this.passField = new CustomEditBox(this.textRenderer, (this.width - w)/2 + 11, this.keyField.getY() +
				this.keyField.getHeight() + passDescSpace + (this.hugeGUI() ? -3 : 13), w, 18, ScreenTexts.EMPTY);
		this.passField.setMaxLength(512);
		this.passField.setChangedListener(this::onPassphraseUpdate);
		this.addSelectableChild(this.passField);

		button = new AdvancedImageButton(this.passField.getX() - 22, this.passField.getY() - 0, 18, 18, 0,
				46, 0, ENCRYPTION_ICONS, 64, 64, btn -> {}, Text.empty(), this);
		button.active = false;
		button.visible = true;

		this.addDrawable(button);

		int checkWidth = this.textRenderer.getWidth(ENCRYPT_PUBLIC);
		this.encryptPublicCheck = new CheckboxWidget(this.width / 2 - checkWidth / 2 - 8, this.passField.getY() + 24, checkWidth + 24, 20,
				ENCRYPT_PUBLIC, NCRConfig.getEncryption().shouldEncryptPublic());
		this.addDrawableChild(this.encryptPublicCheck);

		this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, btn -> {
			this.onDone();
			this.close();
		}).position(this.width / 2 + 4, this.passField.getY() + 48).size(219, 20).build());

		CyclingButtonWidget<Encryption> cycle = CyclingButtonWidget.<Encryption>builder(value -> {
			return Text.translatable("gui.nochatreports.encryption_config.algorithm",
					Text.translatable("algorithm.nochatreports." + value.getID() + ".name"));
		}).values(Encryption.getRegistered()).omitKeyText().initially(this.getConfig()
				.getAlgorithm()).tooltip(value -> new AdvancedTooltip(Text.translatable(
						"algorithm.nochatreports." + value.getID())).setMaxWidth(250))
				.build(this.width / 2 - 4 - 218, this.passField.getY() + 48, 218, 20, ScreenTexts.EMPTY,
						(cycleButton, value) -> {
							this.unfocusFields();
							this.onAlgorithmUpdate(value);
						});

		this.addDrawableChild(this.algorithmButton = cycle);

		this.onAlgorithmUpdate(this.algorithmButton.getValue());

		if (!StringHelper.isEmpty(this.getConfig().getEncryptionPassphrase())) {
			this.passField.setText(this.getConfig().getEncryptionPassphrase());
		} else if (!StringHelper.isEmpty(this.getConfig().getEncryptionKey())) {
			if (!Objects.equals(this.getConfig().getEncryptionKey(), this.algorithmButton.getValue()
					.getDefaultKey())) {
				this.keyField.setText(this.getConfig().getEncryptionKey());
			} else {
				this.keyField.setText("");
			}
		}
	}

	@Override
	public void render(MatrixStack poseStack, int i, int j, float f) {
		if (!this.passField.isNarratable()) {
			if (this.passField.isFocused()) {
				this.passField.setFocused(false);
			}
			this.passField.setEditable(false);
		}

		this.renderBackground(poseStack);
		Screen.drawCenteredTextWithShadow(poseStack, this.textRenderer, HEADER, this.width / 2, this.hugeGUI() ? 8 : 16, 0xFFFFFF);

		this.keyDesc.drawWithShadow(poseStack, this.keyField.getX() - 20, (this.hugeGUI() ? 25 : FIELDS_Y_START), this.getLineHeight(), 0xFFFFFF);

		this.keyField.render(poseStack, i, j, f);

		this.passDesc.drawWithShadow(poseStack, this.passField.getX() - 20, this.keyField.getY() + this.keyField.getHeight() + (this.hugeGUI() ? 12 : 28),
				this.getLineHeight(), 0xFFFFFF);

		this.passField.render(poseStack, i, j, f);

		//		if (this.algorithmButton != null && this.algorithmButton.isMouseOver(i, j)) {
		//			this.renderTooltip(poseStack, this.algorithmButton.getTooltip(), i, j);
		//		}

		super.render(poseStack, i, j, f);

		if (StringHelper.isEmpty(this.keyField.getText()) && !this.keyField.isFocused()) {
			Screen.drawTextWithShadow(poseStack, this.textRenderer,
					Text.translatable("gui.nochatreports.encryption_config.default_key",
							this.algorithmButton.getValue().getDefaultKey()), this.keyField.getX() + 4,
					this.keyField.getY() + 5, 0x999999);
		}

		if (!this.passField.active) {
			Screen.drawTextWithShadow(poseStack, this.textRenderer, PASS_NOT_ALLOWED, this.passField.getX() + 4,
					this.passField.getY() + 5, 0x999999);
			RenderSystem.setShaderTexture(0, ENCRYPTION_ICONS);
			RenderSystem.enableDepthTest();
			drawTexture(poseStack, this.passField.getX() - 20, this.passField.getY() + 3, 50, 0, 14, 13, 64, 64);
		}
	}

	private int getLineHeight() {
		if (this.hugeGUI())
			return (int) (this.client.textRenderer.fontHeight * 1.5) + 1;
		else
			return this.client.textRenderer.fontHeight * 2;
	}

	@Override
	public void tick() {
		this.keyField.tick();
	}

	private void onKeyUpdate(String key) {
		if (!this.settingPassKey) {
			this.passField.setText("");
		}

		if (!StringHelper.isEmpty(key)) {
			this.validationIcon.v = this.algorithmButton.getValue().validateKey(key) ? 0 : 12;
		} else {
			this.validationIcon.v = 0;
		}
	}

	private void onPassphraseUpdate(String pass) {
		Encryption encryption = this.algorithmButton.getValue();

		this.settingPassKey = true;
		if (!StringHelper.isEmpty(pass)) {
			if (encryption.supportsPassphrases()) {
				this.keyField.setText(encryption.getPassphraseKey(pass));
			}
		} else {
			this.onKeyUpdate(this.keyField.getText());
		}
		this.settingPassKey = false;
	}

	private void onAlgorithmUpdate(Encryption encryption) {
		if (!encryption.supportsPassphrases()) {
			this.passField.setFocused(false);
			this.passField.setEditable(this.passField.active = false);
			this.onKeyUpdate(this.keyField.getText());
		} else {
			this.passField.setEditable(this.passField.active = true);
			this.onPassphraseUpdate(this.passField.getText());
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
		config.setEncryptionKey(!StringHelper.isEmpty(this.keyField.getText()) ? this.keyField.getText()
				: encryption.getDefaultKey());
		config.setEncryptPublic(this.encryptPublicCheck.isChecked());
	}

	private boolean hugeGUI() {
		return this.height <= 1080 / 4;
	}

	@Override
	public void close() {
		this.client.setScreen(this.previous);
	}

	private static class CustomEditBox extends TextFieldWidget {
		public CustomEditBox(TextRenderer font, int i, int j, int k, int l, Text component) {
			super(font, i, j, k, l, component);
		}

		@Override
		public void setFocused(boolean bl) {
			super.setFocused(bl);
		}
	}

}
