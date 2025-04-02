package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerSafetyState;
import com.aizistral.nochatreports.common.gui.AdvancedImageButton;
import com.aizistral.nochatreports.common.gui.AdvancedTooltip;
import com.aizistral.nochatreports.common.gui.GUIShenanigans;
import com.aizistral.nochatreports.common.gui.SwitchableSprites;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * This is responsible for adding config reload button to server selection menu.
 * @author Aizistral
 */

@Mixin(JoinMultiplayerScreen.class)
public abstract class MixinJoinMultiplayerScreen extends Screen {
	private static final ResourceLocation RELOAD_TEXTURE = ResourceLocation.fromNamespaceAndPath("nochatreports", "textures/gui/config_reload_button.png"),
			TOGGLE_TEXTURE = ResourceLocation.fromNamespaceAndPath("nochatreports", "textures/gui/ncr_toggle_button.png");
	private static final Component RELOAD_TOOLTIP = Component.translatable("gui.nochatreports.reload_config_tooltip");

	protected MixinJoinMultiplayerScreen() {
		super(null);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = "init", at = @At("HEAD"))
	private void onInit(CallbackInfo info) {
		if (NCRConfig.getClient().showReloadButton()) {
			var button = new AdvancedImageButton(this.width/2 + 158, this.height - 54, 20, 20,
					SwitchableSprites.of(GUIShenanigans.getSprites("config_reload_button")),
					btn -> NCRConfig.load(), CommonComponents.EMPTY, this);
			button.setTooltip(new AdvancedTooltip(RELOAD_TOOLTIP).setMaxWidth(250));
			button.active = true;
			button.visible = true;
			this.addRenderableWidget(button);
		}

		if (NCRConfig.getClient().showNCRButton()) {
			var button = new AdvancedImageButton(this.width/2 + 158, this.height - 30, 20, 20,
					SwitchableSprites.of(
							GUIShenanigans.getSprites("ncr_active_button"),
							GUIShenanigans.getSprites("ncr_inactive_button")
							).setIndex(NCRConfig.getClient().enableMod() ? 0 : 1),
					btn -> {
						NCRConfig.getClient().toggleMod();
						boolean enabled = NCRConfig.getClient().enableMod();

						if (enabled) {
							((AdvancedImageButton)btn).useSprites(0);
						} else {
							((AdvancedImageButton)btn).useSprites(1);
						}

						ServerSafetyState.reset();
					}, Component.translatable("gui.nochatreports.ncr_toggle"), this);
			button.setTooltip(new AdvancedTooltip(() -> Component.translatable("gui.nochatreports.ncr_toggle_tooltip",
					Language.getInstance().getOrDefault("gui.nochatreports.ncr_state_" + (NCRConfig.getClient()
							.enableMod() ? "on" : "off")))).setMaxWidth(250));
			button.active = true;
			button.visible = true;
			this.addRenderableWidget(button);
		}
	}

}
