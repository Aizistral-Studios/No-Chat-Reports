package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.common.NCRClient;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerSafetyLevel;
import com.aizistral.nochatreports.common.core.ServerSafetyState;
import com.aizistral.nochatreports.common.core.SigningMode;
import com.aizistral.nochatreports.common.gui.AdvancedImageButton;
import com.aizistral.nochatreports.common.gui.AdvancedTooltip;
import com.aizistral.nochatreports.common.gui.GUIShenanigans;
import com.aizistral.nochatreports.common.gui.SwitchableSprites;
import com.aizistral.nochatreports.common.gui.TooltipHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * This is responsible for adding safety status indicator to the bottom-right corner of chat screen.
 * @author Aizistral
 */

@Mixin(ChatScreen.class)
public abstract class MixinChatScreen extends Screen {
	private static final ResourceLocation CHAT_STATUS_ICONS = ResourceLocation.fromNamespaceAndPath("nochatreports", "textures/gui/chat_status_icons_extended.png");
	private static final ResourceLocation ENCRYPTION_BUTTON = ResourceLocation.fromNamespaceAndPath("nochatreports", "textures/gui/encryption_toggle_button.png");
	private AdvancedImageButton safetyStatusButton;
	@Shadow
	protected EditBox input;

	protected MixinChatScreen() {
		super(null);
		throw new IllegalStateException("Can't touch this");
	}

	@Override
	protected void changeFocus(ComponentPath path) {
		if (GUIShenanigans.getLeaf(path).component() instanceof EditBox) {
			super.changeFocus(path);
		}
	}

	@Override
	public void setFocused(GuiEventListener listener) {
		if (listener instanceof EditBox) {
			super.setFocused(listener);
		}
	}

	@Inject(method = "handleChatInput", at = @At("HEAD"), cancellable = true)
	private void onHandleChatInput(String string, boolean bl, CallbackInfo info) {
		if (NCRConfig.getServerPreferences().hasModeCurrent(SigningMode.ALWAYS) && !ServerSafetyState.allowChatSigning()) {
			if (this.minecraft.getConnection().getConnection().isEncrypted()) {
				if (!this.normalizeChatMessage(string).isEmpty()) {
					ServerSafetyState.updateCurrent(ServerSafetyLevel.INSECURE);
					ServerSafetyState.scheduleSigningAction(NCRClient::resendLastChatMessage);
					ServerSafetyState.setAllowChatSigning(true);
					info.cancel();
				}
			}
		}
	}

	@Inject(method = "normalizeChatMessage", at = @At("RETURN"), cancellable = true)
	public void onBeforeMessage(String original, CallbackInfoReturnable<String> info) {
		String message = info.getReturnValue();
		ServerSafetyState.setLastMessage(message);
	}

	@Inject(method = "init", at = @At("HEAD"))
	private void onInit(CallbackInfo info) {
		int buttonX = this.width - 23;

		if (NCRConfig.getClient().showServerSafety() && NCRConfig.getClient().enableMod()) {
			this.safetyStatusButton = new AdvancedImageButton(buttonX, this.height - 37, 20, 20,
					SwitchableSprites.of(
							GUIShenanigans.getSprites("safety_state/insecure"),
							GUIShenanigans.getSprites("safety_state/unintrusive"),
							GUIShenanigans.getSprites("safety_state/secure"),
							GUIShenanigans.getSprites("safety_state/realms"),
							GUIShenanigans.getSprites("safety_state/unknown"),
							GUIShenanigans.getSprites("safety_state/undefined")
							).setIndex(this.getSpriteSet()),
					btn -> {
						if (!NCRClient.areSigningKeysPresent())
							return;

						var address = ServerSafetyState.getLastServer();

						if (address != null) {
							var preferences = NCRConfig.getServerPreferences();
							preferences.setMode(address, preferences.getModeUnresolved(address).next());
							preferences.saveFile();
						}
					}, Component.empty(), this);
			this.safetyStatusButton.setTooltip(new AdvancedTooltip(() -> {
				MutableComponent tooltip = this.getSafetyLevel().getTooltip();

				if (ServerSafetyState.allowChatSigning()) {
					tooltip = Component.translatable("gui.nochatreports.safety_status.insecure_signing");
				} else if (ServerSafetyState.isInSingleplayer())
					return tooltip;

				ServerAddress address = ServerSafetyState.getLastServer();
				SigningMode mode = NCRConfig.getServerPreferences().getModeUnresolved(address);
				String signing = "gui.nochatreports.signing_status.";

				if (!this.minecraft.getConnection().getConnection().isEncrypted()) {
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
				tooltip.append(Component.translatable(signing));

				if (ServerSafetyState.isOnRealms())
					return tooltip;

				tooltip.append("\n\n");
				tooltip.append(Component.translatable("gui.nochatreports.safety_status_button.controls"));
				tooltip.append("\n\n");
				tooltip.append(Component.translatable("gui.nochatreports.signing_mode",
						mode.getName().withStyle(ChatFormatting.BOLD, ChatFormatting.AQUA)));
				tooltip.append("\n");
				tooltip.append((mode == SigningMode.DEFAULT ? mode.resolve() : mode).getTooltip());

				return tooltip;
			}).setMaxWidth(250).setRenderWithoutGap(true));

			this.addRenderableWidget(this.safetyStatusButton);
			buttonX -= 25;
		}
	}

	@Override
	public void tick() {
		if (this.safetyStatusButton != null) {
			this.safetyStatusButton.useSprites(this.getSpriteSet());
		}
	}

	private ServerSafetyLevel getSafetyLevel() {
		return ServerSafetyState.getCurrent();
	}

	private int getSpriteSet() {
		return this.getSpriteSet(this.getSafetyLevel());
	}

	private int getSpriteSet(ServerSafetyLevel level) {
		return switch (level) {
		case INSECURE -> 0;
		case UNINTRUSIVE -> 1;
		case SECURE, SINGLEPLAYER -> 2;
		case REALMS -> 3;
		case UNKNOWN -> 4;
		case UNDEFINED -> 5;
		};
	}

	@Shadow
	public abstract String normalizeChatMessage(String string);

}
