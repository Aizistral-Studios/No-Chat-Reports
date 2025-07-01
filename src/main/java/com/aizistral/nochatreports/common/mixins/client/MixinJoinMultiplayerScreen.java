package com.aizistral.nochatreports.common.mixins.client;

import com.aizistral.nochatreports.common.gui.ButtonIconData;
import com.aizistral.nochatreports.common.gui.IconData;
import com.aizistral.nochatreports.common.gui.AdvancedIconButton;
import com.aizistral.nochatreports.common.gui.SwitchableButtonIcon;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerSafetyState;
import com.aizistral.nochatreports.common.gui.AdvancedTooltip;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * This is responsible for adding config reload button to server selection menu.
 * @author Aizistral
 */

@Mixin(JoinMultiplayerScreen.class)
public abstract class MixinJoinMultiplayerScreen extends Screen {
	@Unique private static final ResourceLocation RELOAD_ICON = ResourceLocation
			.fromNamespaceAndPath("nochatreports", "config_reload");
	@Unique private static final ResourceLocation RELOAD_ICON_DISABLED = ResourceLocation
			.fromNamespaceAndPath("nochatreports", "config_reload_disabled");
	@Unique private static final ResourceLocation NCR_ACTIVE_ICON = ResourceLocation
			.fromNamespaceAndPath("nochatreports", "ncr_active");
	@Unique private static final ResourceLocation NCR_ACTIVE_ICON_DISABLED = ResourceLocation
			.fromNamespaceAndPath("nochatreports", "ncr_active_disabled");
	@Unique private static final ResourceLocation NCR_INACTIVE_ICON = ResourceLocation
			.fromNamespaceAndPath("nochatreports", "ncr_inactive");

	@Unique private static final Component RELOAD_TOOLTIP = Component.translatable("gui.nochatreports.reload_config_tooltip");

	protected MixinJoinMultiplayerScreen() {
		super(null);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = "init", at = @At("HEAD"))
	private void onInit(CallbackInfo info) {
		AdvancedIconButton reloadButton = new AdvancedIconButton(
				this.width / 2 + 158, this.height - 54, 20, 20,
				new SwitchableButtonIcon(new ButtonIconData(
						new IconData(RELOAD_ICON, 12, 13),
						new IconData(RELOAD_ICON_DISABLED, 12, 13))),
				btn -> NCRConfig.load(),
				this);
		reloadButton.setTooltip(new AdvancedTooltip(RELOAD_TOOLTIP).setMaxWidth(250));
		reloadButton.visible = NCRConfig.getClient().showReloadButton();

		AdvancedIconButton ncrToggleButton = new AdvancedIconButton(
				this.width / 2 + 158, this.height - 30, 20, 20,
				new SwitchableButtonIcon(
						new ButtonIconData(
								new IconData(NCR_ACTIVE_ICON, 14, 14),
								new IconData(NCR_ACTIVE_ICON_DISABLED, 14, 14)),
						new ButtonIconData(new IconData(NCR_INACTIVE_ICON, 14, 14))
				).setIndex(NCRConfig.getClient().enableMod() ? 0 : 1),
				btn -> {
					NCRConfig.getClient().toggleMod();
					((AdvancedIconButton) btn).setIconIndex(NCRConfig.getClient().enableMod() ? 0 : 1);
					ServerSafetyState.reset();
				},
				this);
		ncrToggleButton.setTooltip(new AdvancedTooltip(() -> Component.translatable("gui.nochatreports.ncr_toggle_tooltip",
				Language.getInstance().getOrDefault("gui.nochatreports.ncr_state_" + (NCRConfig.getClient()
						.enableMod() ? "on" : "off")))).setMaxWidth(250));
		ncrToggleButton.visible = NCRConfig.getClient().showNCRButton();

		this.addRenderableWidget(reloadButton);
		this.addRenderableWidget(ncrToggleButton);
	}
}
