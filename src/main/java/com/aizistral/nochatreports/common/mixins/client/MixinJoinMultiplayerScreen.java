package com.aizistral.nochatreports.common.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerSafetyState;
import com.aizistral.nochatreports.common.gui.AdvancedImageButton;
import com.aizistral.nochatreports.common.gui.AdvancedTooltip;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;

/**
 * This is responsible for adding config reload button to server selection menu.
 * @author Aizistral
 */

@Mixin(MultiplayerScreen.class)
public abstract class MixinJoinMultiplayerScreen extends Screen {
	private static final Identifier RELOAD_TEXTURE = new Identifier("nochatreports", "textures/gui/config_reload_button.png"),
			TOGGLE_TEXTURE = new Identifier("nochatreports", "textures/gui/ncr_toggle_button.png");
	private static final Text RELOAD_TOOLTIP = Text.translatable("gui.nochatreports.reload_config_tooltip");

	protected MixinJoinMultiplayerScreen() {
		super(null);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = "init", at = @At("HEAD"))
	private void onInit(CallbackInfo info) {
		if (NCRConfig.getClient().showReloadButton()) {
			var button = new AdvancedImageButton(this.width/2 + 158, this.height - 52, 20, 20, 0, 0, 20,
					RELOAD_TEXTURE, 64, 64, btn -> NCRConfig.load(), ScreenTexts.EMPTY, this);
			button.setTooltip(new AdvancedTooltip(RELOAD_TOOLTIP).setMaxWidth(250).setRenderWithoutGap(true));
			button.active = true;
			button.visible = true;
			this.addDrawableChild(button);
		}

		if (NCRConfig.getClient().showNCRButton()) {
			var button = new AdvancedImageButton(this.width/2 + 158, this.height - 28, 20, 20,
					NCRConfig.getClient().enableMod() ? 0 : 20, 0, 20, TOGGLE_TEXTURE, 64, 64,
							btn -> {
								NCRConfig.getClient().toggleMod();
								boolean enabled = NCRConfig.getClient().enableMod();

								if (enabled) {
									((TexturedButtonWidget)btn).u = 0;
								} else {
									((TexturedButtonWidget)btn).u = 20;
								}

								ServerSafetyState.reset();
							}, Text.translatable("gui.nochatreports.ncr_toggle"), this);
			button.setTooltip(new AdvancedTooltip(() -> Text.translatable("gui.nochatreports.ncr_toggle_tooltip",
					Language.getInstance().get("gui.nochatreports.ncr_state_" + (NCRConfig.getClient()
							.enableMod() ? "on" : "off")))).setMaxWidth(250));
			button.active = true;
			button.visible = true;
			this.addDrawableChild(button);
		}
	}

}
