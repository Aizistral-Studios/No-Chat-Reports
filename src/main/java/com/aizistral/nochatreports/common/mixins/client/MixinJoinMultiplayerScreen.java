package com.aizistral.nochatreports.common.mixins.client;

import com.aizistral.nochatreports.common.gui.*;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerSafetyState;

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
    @Shadow @Final
    private HeaderAndFooterLayout layout;

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
        LinearLayout ncrButtons = this.layout.addToFooter(LinearLayout.vertical().spacing(4), layoutSettings -> {
            layoutSettings.paddingLeft(336);
        });

        AdvancedIconButton ncrReloadButton = new AdvancedIconButton(0, 0, 20, 20,
                new SwitchableButtonIcon(new ButtonIconData(
                        new IconData(RELOAD_ICON, 12, 13),
                        new IconData(RELOAD_ICON_DISABLED, 12, 13))),
                btn -> NCRConfig.load(),
                this);
        ncrReloadButton.setTooltip(new AdvancedTooltip(RELOAD_TOOLTIP).setMaxWidth(250));
        ncrReloadButton.active = true;
        ncrReloadButton.visible = NCRConfig.getClient().showReloadButton();
        ncrButtons.addChild(ncrReloadButton);

        AdvancedIconButton ncrToggleButton = new AdvancedIconButton(0, 0, 20, 20,
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
        ncrToggleButton.active = true;
        ncrToggleButton.visible = NCRConfig.getClient().showNCRButton();
        ncrButtons.addChild(ncrToggleButton);
    }
}
