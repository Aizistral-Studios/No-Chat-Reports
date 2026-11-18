package com.aizistral.nochatreports.common.mixins.client;

import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.gui.AdvancedImageButton;
import com.aizistral.nochatreports.common.gui.AdvancedTooltip;
import com.aizistral.nochatreports.common.gui.GUIShenanigans;
import com.aizistral.nochatreports.common.gui.SwitchableSprites;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * This is responsible for adding mod toggle button to server selection menu.
 * @author Aizistral
 */

@Mixin(JoinMultiplayerScreen.class)
public abstract class MixinJoinMultiplayerScreen extends Screen {
	@Shadow @Final
	private HeaderAndFooterLayout layout;

	protected MixinJoinMultiplayerScreen() {
		super(null);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = "init", at = @At("HEAD"))
	private void onInit(CallbackInfo info) {
		LinearLayout ncrButtons = this.layout.addToFooter(LinearLayout.vertical().spacing(4), layoutSettings -> {
			layoutSettings.paddingLeft(336);
		});

		if (!NCRConfig.isModRunning() || NCRConfig.getClient().showNCRButton()) {
			var button = new AdvancedImageButton(0, 0, 20, 20,
					SwitchableSprites.of(
							GUIShenanigans.getSprites("ncr_active_button"),
							GUIShenanigans.getSprites("ncr_inactive_button")
							).setIndex(NCRConfig.getCommon().getSavedEnableMod() ? 0 : 1),
					btn -> {
						boolean enabled = !NCRConfig.getCommon().getSavedEnableMod();
						NCRConfig.getCommon().setSavedEnableMod(enabled);

						if (enabled) {
							((AdvancedImageButton)btn).useSprites(0);
						} else {
							((AdvancedImageButton)btn).useSprites(1);
						}
					}, Component.translatable("gui.nochatreports.ncr_toggle"), this);
			button.setTooltip(new AdvancedTooltip(() -> {
				boolean enabled = NCRConfig.getCommon().getSavedEnableMod();
				MutableComponent tooltip = Component.translatable("gui.nochatreports.ncr_toggle_tooltip",
						Language.getInstance().getOrDefault("gui.nochatreports.ncr_state_" + (enabled ? "on" : "off")));

				if (enabled != NCRConfig.isModRunning()) {
					tooltip.append("\n").append(Component.translatable("gui.nochatreports.ncr_toggle_restart_pending"));
				}

				return tooltip;
			}).setMaxWidth(250));
			button.active = true;
			button.visible = true;
			ncrButtons.addChild(button);
		}
	}

}
