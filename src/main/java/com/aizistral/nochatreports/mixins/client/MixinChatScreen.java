package com.aizistral.nochatreports.mixins.client;

import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.aizistral.nochatreports.gui.AdvancedImageButton;
import com.aizistral.nochatreports.gui.AdvancedTooltip;
import com.aizistral.nochatreports.gui.EncryptionButton;
import com.aizistral.nochatreports.gui.EncryptionWarningScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This is responsible for adding safety status indicator to the bottom-right corner of chat screen.
 * @author Aizistral
 */

@Mixin(ChatScreen.class)
public abstract class MixinChatScreen extends Screen {
	private static final ResourceLocation CHAT_STATUS_ICONS = new ResourceLocation("nochatreports", "textures/gui/chat_status_icons_extended.png");
	private static final ResourceLocation ENCRYPTION_BUTTON = new ResourceLocation("nochatreports", "textures/gui/encryption_toggle_button.png");
	private ImageButton safetyStatusButton;

	protected MixinChatScreen() {
		super(null);
		throw new IllegalStateException("Can't touch this");
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
						if (NCRConfig.getClient().whitelistAllServers())
							return;

						var address = ServerSafetyState.getLastServer();
						if (address != null) {
							var whitelist = NCRConfig.getServerWhitelist();

							if (!whitelist.isWhitelisted(address)) {
								whitelist.add(address);
								whitelist.saveFile();

								if (ServerSafetyState.getCurrent() != ServerSafetyLevel.SECURE) {
									ServerSafetyState.setAllowChatSigning(true);
									ServerSafetyState.updateCurrent(ServerSafetyLevel.INSECURE);
								}
							} else {
								whitelist.remove(address);
								whitelist.saveFile();
							}
						}
					}, Component.empty(), this);
			this.safetyStatusButton.setTooltip(new AdvancedTooltip(() -> {
				MutableComponent tooltip = this.getSafetyLevel().getTooltip();
				ServerAddress address = ServerSafetyState.getLastServer();
				String signing = "gui.nochatreports.status_signing_denied";

				if (ServerSafetyState.getCurrent() == ServerSafetyLevel.REALMS) {
					signing = "gui.nochatreports.status_signing_allowed_realms";
				} else if (ServerSafetyState.getCurrent() == ServerSafetyLevel.SECURE) {
					signing = "gui.nochatreports.status_signing_denied_secure";
				} else if (NCRConfig.getServerWhitelist().isWhitelisted(address)) {
					signing = "gui.nochatreports.status_signing_allowed_whitelisted";
				} else if (ServerSafetyState.allowChatSigning()) {
					signing = "gui.nochatreports.status_signing_allowed";
				}

				tooltip.append("\n\n").append(Component.translatable(signing));

				if (address != null) {
					String status = "gui.nochatreports.status_whitelist_no";

					if (NCRConfig.getClient().whitelistAllServers()) {
						status = "gui.nochatreports.status_whitelist_all";
					} else if (NCRConfig.getServerWhitelist().isWhitelisted(address)) {
						status = "gui.nochatreports.status_whitelist_yes";
					}

					tooltip.append("\n\n").append(Component.translatable(
							"gui.nochatreports.status_whitelist_mode", Component.translatable(status)));
				}

				return tooltip;
			}).setMaxWidth(250).setRenderWithoutGap(true));

			this.addRenderableWidget(this.safetyStatusButton);
			buttonX -= 25;
		}

		if (!NCRConfig.getEncryption().showEncryptionButton())
			return;

		int xStart = !NCRConfig.getEncryption().isValid() ? 40 : (NCRConfig.getEncryption().isEnabled() ? 0 : 20);

		var button = new EncryptionButton(buttonX, this.height - 37, 20, 20, xStart,
				0, 20, ENCRYPTION_BUTTON, 64, 64, btn -> {
					if (!EncryptionWarningScreen.seenOnThisSession() && !NCRConfig.getEncryption().isWarningDisabled()
							&& !NCRConfig.getEncryption().isEnabled()) {
						Minecraft.getInstance().setScreen(new EncryptionWarningScreen(this));
					} else if (NCRConfig.getEncryption().isValid()) {
						NCRConfig.getEncryption().toggleEncryption();
						((EncryptionButton)btn).xTexStart = NCRConfig.getEncryption().isEnabledAndValid() ? 0 : 20;
					} else {
						((EncryptionButton)btn).openEncryptionConfig();
					}
				}, Component.empty(), this);
		button.setTooltip(new AdvancedTooltip(() -> {
			if (NCRConfig.getEncryption().isValid())
				return Component.translatable("gui.nochatreports.encryption_tooltip", Language.getInstance()
						.getOrDefault("gui.nochatreports.encryption_state_" + (NCRConfig.getEncryption()
								.isEnabledAndValid() ? "on" : "off")));
			else
				return Component.translatable("gui.nochatreports.encryption_tooltip_invalid", Language.getInstance()
						.getOrDefault("gui.nochatreports.encryption_state_" + (NCRConfig.getEncryption()
								.isEnabledAndValid() ? "on" : "off")));
		}).setMaxWidth(250));
		button.active = true;
		button.visible = true;

		this.addRenderableWidget(button);
	}

	@Inject(method = "tick", at = @At("RETURN"))
	private void onTick(CallbackInfo info) {
		if (this.safetyStatusButton != null) {
			this.safetyStatusButton.xTexStart = this.getXOffset();
		}
	}

	private ServerSafetyLevel getSafetyLevel() {
		return this.minecraft.isLocalServer() ? ServerSafetyLevel.SECURE : ServerSafetyState.getCurrent();
	}

	private int getXOffset() {
		return this.getXOffset(this.getSafetyLevel());
	}

	private int getXOffset(ServerSafetyLevel level) {
		return switch (level) {
		case SECURE -> 21;
		case UNINTRUSIVE -> 42;
		case INSECURE -> 0;
		case REALMS -> 63;
		case UNKNOWN -> 84;
		case UNDEFINED -> 105;
		};
	}

}
