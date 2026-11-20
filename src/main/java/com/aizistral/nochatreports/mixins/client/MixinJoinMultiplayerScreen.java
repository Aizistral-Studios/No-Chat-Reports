package com.aizistral.nochatreports.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.config.NCRConfig;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

/**
 * This is responsible for adding mod toggle button to server selection menu.
 * @author Aizistral
 */

@Mixin(JoinMultiplayerScreen.class)
public abstract class MixinJoinMultiplayerScreen extends Screen {
	private static final ResourceLocation TOGGLE_TEXTURE = new ResourceLocation("nochatreports", "textures/gui/ncr_toggle_button.png");

	protected MixinJoinMultiplayerScreen() {
		super(null);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = "init", at = @At("HEAD"))
	private void onInit(CallbackInfo info) {
		if (!NCRConfig.isModRunning() || NCRConfig.getClient().showNCRButton()) {
			int x = this.width/2 + 158, y = this.height - 28;

			if (this.width < 356) {
				x = this.width - 22;
				y = 6;
			}

			var button = new ImageButton(x, y, 20, 20,
					NCRConfig.getCommon().getSavedEnableMod() ? 0 : 20, 0, 20, TOGGLE_TEXTURE, 64, 64,
							btn -> {
								NCRConfig.getCommon().setSavedEnableMod(!NCRConfig.getCommon().getSavedEnableMod());
								boolean enabled = NCRConfig.getCommon().getSavedEnableMod();

								if (enabled) {
									((ImageButton)btn).xTexStart = 0;
								} else {
									((ImageButton)btn).xTexStart = 20;
								}
							}, (btn, poseStack, i, j) -> {
								boolean enabled = NCRConfig.getCommon().getSavedEnableMod();
								MutableComponent tooltip = Component.translatable("gui.nochatreports.ncr_toggle_tooltip",
										Language.getInstance().getOrDefault("gui.nochatreports.ncr_state_" + (enabled ? "on" : "off")));

								if (enabled != NCRConfig.isModRunning()) {
									tooltip.append("\n").append(Component.translatable("gui.nochatreports.ncr_toggle_restart_pending"));
								}

								this.renderTooltip(poseStack, this.minecraft.font.split(tooltip, 250), i, Math.max(j, 16));
							},
							Component.translatable("gui.nochatreports.ncr_toggle"));
			button.active = true;
			button.visible = true;
			this.addRenderableWidget(button);
		}
	}

}
