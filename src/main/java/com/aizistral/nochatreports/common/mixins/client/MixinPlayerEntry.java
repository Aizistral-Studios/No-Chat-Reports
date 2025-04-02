package com.aizistral.nochatreports.common.mixins.client;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Supplier;

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
import com.aizistral.nochatreports.common.gui.SwitchableSprites;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.social.PlayerEntry;
import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@Mixin(PlayerEntry.class)
public class MixinPlayerEntry {
	private static final Component NCR_BUTTON_TOOLTIP = Component.translatable("gui.nochatreports.no_reporting");

	@Shadow @Final private static WidgetSprites REPORT_BUTTON_SPRITES;
	@Shadow private Button reportButton;

	/**
	 * @reason Disable (or hide if configured respectively) chat report button on client.
	 * @author Aizistral
	 */

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onConstructed(Minecraft minecraft, SocialInteractionsScreen screen, UUID uuid, String name, Supplier<ResourceLocation> skinGetter, boolean reportable, CallbackInfo info) {
		if (NCRConfig.getClient().alwaysHideReportButton()) {
			this.reportButton = new InvisibleButton();
			this.reportButton.active = this.reportButton.visible = false;
		} else if (ServerSafetyState.getCurrent().isSecure() && this.reportButton != null) {
			this.reportButton = new AdvancedImageButton(0, 0, 20, 20, SwitchableSprites.of(REPORT_BUTTON_SPRITES),
					button -> {}, Component.translatable("gui.socialInteractions.report"), screen);
			this.reportButton.setTooltip(Tooltip.create(NCR_BUTTON_TOOLTIP));
			this.reportButton.setTooltipDelay(Duration.ofMillis(500L));
			this.reportButton.active = false;
		}
	}

}
