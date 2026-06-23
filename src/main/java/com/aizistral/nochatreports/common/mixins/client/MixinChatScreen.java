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
import com.aizistral.nochatreports.common.gui.GUIShenanigans;
import com.aizistral.nochatreports.common.gui.TooltipHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

/**
 * This is responsible for adding safety status indicator to the bottom-right corner of chat screen.
 * @author Aizistral
 */

@Mixin(ChatScreen.class)
public abstract class MixinChatScreen extends Screen {
	private static final Identifier CHAT_STATUS_ICONS = Identifier.fromNamespaceAndPath("nochatreports", "textures/gui/chat_status_icons_extended.png");
	private static final Identifier ENCRYPTION_BUTTON = Identifier.fromNamespaceAndPath("nochatreports", "textures/gui/encryption_toggle_button.png");
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
			if (this.minecraft.getConnection().onlineMode()) {
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
		if (NCRConfig.getClient().showServerSafety() && NCRConfig.getClient().enableMod()) {
			CycleButton<SigningMode> safetyStatusButton = CycleButton.<SigningMode>builder(
					mode -> Component.empty(),
					NCRConfig.getServerPreferences().getModeUnresolved(ServerSafetyState.getLastServer())
			)
			.withValues(NCRClient.areSigningKeysPresent()
					? new SigningMode[]{ SigningMode.DEFAULT, SigningMode.NEVER, SigningMode.ALWAYS, SigningMode.PROMPT, SigningMode.ON_DEMAND }
					: new SigningMode[]{ SigningMode.NEVER_FORCED })
			.withSprite((btn, value) -> {
				var sprites = switch (ServerSafetyState.getCurrent()) {
					case INSECURE -> GUIShenanigans.getSprites("safety_state/insecure");
					case UNINTRUSIVE -> GUIShenanigans.getSprites("safety_state/unintrusive");
					case SECURE, SINGLEPLAYER -> GUIShenanigans.getSprites("safety_state/secure");
					case REALMS -> GUIShenanigans.getSprites("safety_state/realms");
					case UNKNOWN -> GUIShenanigans.getSprites("safety_state/unknown");
					case UNDEFINED -> GUIShenanigans.getSprites("safety_state/undefined");
				};
				return sprites.get(btn.isActive(), btn.isHoveredOrFocused());
			})
			.withTooltip(mode -> Tooltip.create(createSafetyTooltip(mode)))
			.displayState(CycleButton.DisplayState.HIDE)
			.create(
					this.width - 23, this.height - 37, 20, 20,
					Component.empty(),
					(btn, mode) -> {
						if (!NCRClient.areSigningKeysPresent())
							return;

						var address = ServerSafetyState.getLastServer();
						if (address != null) {
							var preferences = NCRConfig.getServerPreferences();
							preferences.setMode(address, mode);
							preferences.saveFile();
						}
					}
			);

			this.addRenderableWidget(safetyStatusButton);
		}
	}

	private ServerSafetyLevel getSafetyLevel() {
		return ServerSafetyState.getCurrent();
	}

	private Component createSafetyTooltip(SigningMode mode) {
		MutableComponent tooltip = this.getSafetyLevel().getTooltip();

		if (ServerSafetyState.allowChatSigning()) {
			tooltip = Component.translatable("gui.nochatreports.safety_status.insecure_signing");
		} else if (ServerSafetyState.isInSingleplayer())
			return tooltip;

		String signing = "gui.nochatreports.signing_status.";

		if (!this.minecraft.getConnection().onlineMode()) {
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
	}

	@Shadow
	public abstract String normalizeChatMessage(String string);

}
