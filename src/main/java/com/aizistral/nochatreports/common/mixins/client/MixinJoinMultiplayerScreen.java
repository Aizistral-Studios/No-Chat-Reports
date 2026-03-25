package com.aizistral.nochatreports.common.mixins.client;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerSafetyState;
import net.minecraft.client.gui.components.CycleButton;
import com.aizistral.nochatreports.common.gui.GUIShenanigans;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * This is responsible for adding config reload button to server selection menu.
 * @author Aizistral
 */

@Mixin(JoinMultiplayerScreen.class)
public abstract class MixinJoinMultiplayerScreen extends Screen {
	@Shadow @Final
	private HeaderAndFooterLayout layout;
	private static final Identifier RELOAD_TEXTURE = Identifier.fromNamespaceAndPath("nochatreports", "textures/gui/config_reload_button.png"),
			TOGGLE_TEXTURE = Identifier.fromNamespaceAndPath("nochatreports", "textures/gui/ncr_toggle_button.png");
	private static final Component RELOAD_TOOLTIP = Component.translatable("gui.nochatreports.reload_config_tooltip");

	protected MixinJoinMultiplayerScreen() {
		super(null);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = "init", at = @At("HEAD"))
	private void onInit(CallbackInfo info) {
		LinearLayout ncrButtons = this.layout.addToFooter(LinearLayout.vertical().spacing(4), layoutSettings -> {
			layoutSettings.paddingLeft(336);
		});

		if (NCRConfig.getClient().showReloadButton()) {
			var button = new ImageButton(0, 0, 20, 20,
					GUIShenanigans.getSprites("config_reload_button"),
					btn -> NCRConfig.load(), CommonComponents.EMPTY);
			button.setTooltip(Tooltip.create(RELOAD_TOOLTIP));
			button.active = true;
			button.visible = true;
			ncrButtons.addChild(button);
		}

		if (NCRConfig.getClient().showNCRButton()) {
			var button = CycleButton.onOffBuilder(NCRConfig.getClient().enableMod())
					.withSprite((btn, value) -> {
						var sprites = GUIShenanigans.getSprites(
								value ? "ncr_active_button" : "ncr_inactive_button"
						);
						return sprites.get(btn.isActive(), btn.isHoveredOrFocused());
					})
					.withTooltip(value -> Tooltip.create(
							Component.translatable(
									"gui.nochatreports.ncr_toggle_tooltip",
									Language.getInstance().getOrDefault("gui.nochatreports.ncr_state_" + (value ? "on" : "off"))
							)
					))
					.displayState(CycleButton.DisplayState.HIDE)
					.create(
							0, 0, 20, 20,
							Component.empty(),
							(btn, value) -> {
								NCRConfig.getClient().setEnableMod(value);
								ServerSafetyState.reset();
							}
					);

			button.active = true;
			button.visible = true;
			ncrButtons.addChild(button);
		}
	}

}
