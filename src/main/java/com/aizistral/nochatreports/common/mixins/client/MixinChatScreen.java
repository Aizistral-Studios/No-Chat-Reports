package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.common.NCRClient;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.config.NCRServerPreferences;
import com.aizistral.nochatreports.common.core.ServerSafetyLevel;
import com.aizistral.nochatreports.common.core.ServerSafetyState;
import com.aizistral.nochatreports.common.core.SigningMode;
import com.aizistral.nochatreports.common.gui.AdvancedImageButton;
import com.aizistral.nochatreports.common.gui.AdvancedTooltip;
import com.aizistral.nochatreports.common.gui.EncryptionButton;
import com.aizistral.nochatreports.common.gui.EncryptionWarningScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;

/**
 * This is responsible for adding safety status indicator to the bottom-right corner of chat screen.
 * @author Aizistral
 */

@Mixin(ChatScreen.class)
public abstract class MixinChatScreen extends Screen {
	private static final Identifier CHAT_STATUS_ICONS = new Identifier("nochatreports", "textures/gui/chat_status_icons_extended.png");
	private static final Identifier ENCRYPTION_BUTTON = new Identifier("nochatreports", "textures/gui/encryption_toggle_button.png");
	private TexturedButtonWidget safetyStatusButton;

	protected MixinChatScreen() {
		super(null);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = "handleChatInput", at = @At("HEAD"), cancellable = true)
	public void handleChatInput(String string, boolean bl, CallbackInfoReturnable<Boolean> info) {
		if (NCRConfig.getServerPreferences().hasModeCurrent(SigningMode.ALWAYS) && !ServerSafetyState.allowChatSigning()) {
			if (this.client.getNetworkHandler().getConnection().isEncrypted()) {
				if (!this.normalizeChatMessage(string).isEmpty()) {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.INSECURE);
					ServerSafetyState.scheduleSigningAction(NCRClient::resendLastChatMessage);
					ServerSafetyState.setAllowChatSigning(true);
					info.setReturnValue(true);
				}
			}
		}
	}

	@Inject(method = "normalizeChatMessage", at = @At("RETURN"), cancellable = true)
	public void onBeforeMessage(String original, CallbackInfoReturnable<String> info) {
		String message = info.getReturnValue();
		NCRConfig.getEncryption().setLastMessage(message);

		if (!message.isEmpty() && !Screen.hasControlDown() && NCRConfig.getEncryption().shouldEncrypt(message)) {
			NCRConfig.getEncryption().getEncryptor().ifPresent(e -> {
				int index = NCRConfig.getEncryption().getEncryptionStartIndex(message);
				String noencrypt = message.substring(0, index);
				String encrypt = message.substring(index, message.length());

				if (encrypt.length() > 0) {
					info.setReturnValue(noencrypt + e.encrypt("#%" + encrypt));
				}
			});
		}
	}

	@Inject(method = "init", at = @At("HEAD"))
	private void onInit(CallbackInfo info) {
		int buttonX = this.width - 23;

		if (NCRConfig.getClient().showServerSafety() && NCRConfig.getClient().enableMod()) {
			this.safetyStatusButton = new AdvancedImageButton(buttonX, this.height - 37, 20, 20, this.getXOffset(),
					0, 20, CHAT_STATUS_ICONS, 128, 128, btn -> {
						if (!NCRClient.areSigningKeysPresent())
							return;

						var address = ServerSafetyState.getLastServer();

						if (address != null) {
							var preferences = NCRConfig.getServerPreferences();
							preferences.setMode(address, preferences.getModeUnresolved(address).next());
							preferences.saveFile();
						}
					}, Text.empty(), this);
			this.safetyStatusButton.setTooltip(new AdvancedTooltip(() -> {
				MutableText tooltip = this.getSafetyLevel().getTooltip();

				if (ServerSafetyState.allowChatSigning()) {
					tooltip = Text.translatable("gui.nochatreports.safety_status.insecure_signing");
				} else if (ServerSafetyState.isInSingleplayer())
					return tooltip;

				ServerAddress address = ServerSafetyState.getLastServer();
				SigningMode mode = NCRConfig.getServerPreferences().getModeUnresolved(address);
				String signing = "gui.nochatreports.signing_status.";

				if (!this.client.getNetworkHandler().getConnection().isEncrypted()) {
					signing += "disabled_offline";
				} else if (ServerSafetyState.getCurrent() == ServerSafetyLevel.REALMS) {
					signing += "allowed_realms";
				} else if (mode.resolve() == SigningMode.ALWAYS) {
					if (ServerSafetyState.allowChatSigning()) {
						signing += "allowed";
					} else {
						signing += "disabled_allowance_pending";
					}
				} else if (ServerSafetyState.allowChatSigning()) {
					signing += "allowed_session";
				} else {
					signing += "disabled";
				}

				tooltip.append("\n\n");
				tooltip.append(Text.translatable(signing));

				if (ServerSafetyState.isOnRealms())
					return tooltip;

				tooltip.append("\n\n");
				tooltip.append(Text.translatable("gui.nochatreports.safety_status_button.controls"));
				tooltip.append("\n\n");
				tooltip.append(Text.translatable("gui.nochatreports.signing_mode",
						mode.getName().formatted(Formatting.BOLD, Formatting.AQUA)));
				tooltip.append("\n");
				tooltip.append((mode == SigningMode.DEFAULT ? mode.resolve() : mode).getTooltip());

				return tooltip;
			}).setMaxWidth(250).setRenderWithoutGap(true));

			this.addDrawableChild(this.safetyStatusButton);
			buttonX -= 25;
		}

		if (!NCRConfig.getEncryption().showEncryptionButton())
			return;

		int xStart = !NCRConfig.getEncryption().isValid() ? 40 : (NCRConfig.getEncryption().isEnabled() ? 0 : 20);

		var button = new EncryptionButton(buttonX, this.height - 37, 20, 20, xStart,
				0, 20, ENCRYPTION_BUTTON, 64, 64, btn -> {
					if (!EncryptionWarningScreen.seenOnThisSession() && !NCRConfig.getEncryption().isWarningDisabled()
							&& !NCRConfig.getEncryption().isEnabled()) {
						MinecraftClient.getInstance().setScreen(new EncryptionWarningScreen(this));
					} else if (NCRConfig.getEncryption().isValid()) {
						NCRConfig.getEncryption().toggleEncryption();
						((EncryptionButton)btn).u = NCRConfig.getEncryption().isEnabledAndValid() ? 0 : 20;
					} else {
						((EncryptionButton)btn).openEncryptionConfig();
					}
				}, Text.empty(), this);
		button.setTooltip(new AdvancedTooltip(() -> {
			if (NCRConfig.getEncryption().isValid())
				return Text.translatable("gui.nochatreports.encryption_tooltip", Language.getInstance()
						.get("gui.nochatreports.encryption_state_" + (NCRConfig.getEncryption()
								.isEnabledAndValid() ? "on" : "off")));
			else
				return Text.translatable("gui.nochatreports.encryption_tooltip_invalid", Language.getInstance()
						.get("gui.nochatreports.encryption_state_" + (NCRConfig.getEncryption()
								.isEnabledAndValid() ? "on" : "off")));
		}).setMaxWidth(250));
		button.active = true;
		button.visible = true;

		this.addDrawableChild(button);
	}

	@Inject(method = "tick", at = @At("RETURN"))
	private void onTick(CallbackInfo info) {
		if (this.safetyStatusButton != null) {
			this.safetyStatusButton.u = this.getXOffset();
		}
	}

	private ServerSafetyLevel getSafetyLevel() {
		return ServerSafetyState.getCurrent();
	}

	private int getXOffset() {
		return this.getXOffset(this.getSafetyLevel());
	}

	private int getXOffset(ServerSafetyLevel level) {
		return switch (level) {
		case SECURE, SINGLEPLAYER -> 21;
		case UNINTRUSIVE -> 42;
		case INSECURE -> 0;
		case REALMS -> 63;
		case UNKNOWN -> 84;
		case UNDEFINED -> 105;
		};
	}

	@Shadow
	public abstract String normalizeChatMessage(String string);

}
