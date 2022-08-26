package com.aizistral.nochatreports.mixins.client;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.config.NCRConfigLegacy;
import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.reporting.ChatReportScreen;
import net.minecraft.client.gui.screens.social.PlayerEntry;
import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

@Mixin(PlayerEntry.class)
public class MixinPlayerEntry {
	private static final Component NCR_BUTTON_TOOLTIP = Component.translatable("gui.nochatreport.noReporting");
	@Shadow @Final private static ResourceLocation REPORT_BUTTON_LOCATION;
	@Shadow private Button reportButton;
	@Shadow float tooltipHoverTime;

	@Shadow
	static void postRenderTooltip(SocialInteractionsScreen socialInteractionsScreen, PoseStack poseStack, List<FormattedCharSequence> list, int i, int j) {
		throw new IllegalStateException("@Shadow transformation failed. Should never happen.");
	}

	/**
	 * @reason Disable (or hide if configured respectively) chat report button on client.
	 * @author Aizistral
	 */

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onConstructed(Minecraft minecraft, SocialInteractionsScreen socialInteractionsScreen, UUID uUID, String string, Supplier<ResourceLocation> supplier, boolean reportable, CallbackInfo info) {
		if (NCRConfigLegacy.alwaysHideReportButton()) {
			this.reportButton = new Button(0, 0, 20, 20, Component.empty(), button -> {}) {
				@Override
				public void render(PoseStack poseStack, int i, int j, float f) {
					// NO-OP
				}
			};
			this.reportButton.active = this.reportButton.visible = false;
		} else if (ServerSafetyState.getCurrent() == ServerSafetyLevel.SECURE && this.reportButton != null) {
			this.reportButton = new ImageButton(0, 0, 20, 20, 0, 0, 20, REPORT_BUTTON_LOCATION, 64, 64, button -> {}, new Button.OnTooltip() {
				@Override
				public void onTooltip(Button button, PoseStack poseStack, int i, int j) {
					MixinPlayerEntry.this.tooltipHoverTime += minecraft.getDeltaFrameTime();
					if (MixinPlayerEntry.this.tooltipHoverTime >= 10.0f) {
						socialInteractionsScreen.setPostRenderRunnable(() -> postRenderTooltip(socialInteractionsScreen, poseStack, minecraft.font.split(NCR_BUTTON_TOOLTIP, 150), i, j));
					}
				}

				@Override
				public void narrateTooltip(Consumer<Component> consumer) {
					consumer.accept(NCR_BUTTON_TOOLTIP);
				}
			}, Component.translatable("gui.socialInteractions.report"));
			this.reportButton.active = false;
		}
	}

}
