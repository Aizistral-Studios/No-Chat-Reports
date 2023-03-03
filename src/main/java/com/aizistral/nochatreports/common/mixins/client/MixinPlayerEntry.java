package com.aizistral.nochatreports.common.mixins.client;

import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.SocialInteractionsPlayerListEntry;
import net.minecraft.client.gui.screen.multiplayer.SocialInteractionsScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerSafetyState;
import com.aizistral.nochatreports.common.gui.AdvancedImageButton;
import com.aizistral.nochatreports.common.gui.InvisibleButton;

@Mixin(SocialInteractionsPlayerListEntry.class)
public class MixinPlayerEntry {
	private static final Text NCR_BUTTON_TOOLTIP = Text.translatable("gui.nochatreports.no_reporting");
	@Shadow @Final private static Identifier REPORT_BUTTON_LOCATION;
	@Shadow private ButtonWidget reportButton;

	/**
	 * @reason Disable (or hide if configured respectively) chat report button on client.
	 * @author Aizistral
	 */

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onConstructed(MinecraftClient minecraft, SocialInteractionsScreen screen, UUID uuid, String name, Supplier<Identifier> skinGetter, boolean reportable, CallbackInfo info) {
		if (NCRConfig.getClient().alwaysHideReportButton()) {
			this.reportButton = new InvisibleButton();
			this.reportButton.active = this.reportButton.visible = false;
		} else if (ServerSafetyState.getCurrent().isSecure() && this.reportButton != null) {
			this.reportButton = new AdvancedImageButton(0, 0, 20, 20, 0, 0, 20, REPORT_BUTTON_LOCATION, 64, 64,
					button -> {}, Text.translatable("gui.socialInteractions.report"), screen);
			this.reportButton.setTooltip(Tooltip.of(NCR_BUTTON_TOOLTIP));
			this.reportButton.setTooltipDelay(10);
			this.reportButton.active = false;
		}
	}

}
