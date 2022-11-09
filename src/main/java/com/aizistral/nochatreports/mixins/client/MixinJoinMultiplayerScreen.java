package com.aizistral.nochatreports.mixins.client;

import java.util.List;
import java.util.stream.Collectors;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.aizistral.nochatreports.gui.AdvancedImageButton;
import com.aizistral.nochatreports.gui.AdvancedTooltip;
import com.aizistral.nochatreports.network.ClientChannelHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

/**
 * This is responsible for adding config reload button to server selection menu.
 * @author Aizistral
 */

@Mixin(JoinMultiplayerScreen.class)
public abstract class MixinJoinMultiplayerScreen extends Screen {
	private static final ResourceLocation RELOAD_TEXTURE = new ResourceLocation("nochatreports", "textures/gui/config_reload_button.png"),
			TOGGLE_TEXTURE = new ResourceLocation("nochatreports", "textures/gui/ncr_toggle_button.png");
	private static final Component RELOAD_TOOLTIP = Component.translatable("gui.nochatreports.reload_config_tooltip");

	protected MixinJoinMultiplayerScreen() {
		super(null);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = "init", at = @At("HEAD"))
	private void onInit(CallbackInfo info) {
		if (NCRConfig.getClient().showReloadButton()) {
			var button = new AdvancedImageButton(this.width/2 + 158, this.height - 52, 20, 20, 0, 0, 20,
					RELOAD_TEXTURE, 64, 64, btn -> NCRConfig.load(), CommonComponents.EMPTY, this);
			button.setTooltip(new AdvancedTooltip(RELOAD_TOOLTIP).setMaxWidth(250).setRenderWithoutGap(true));
			button.active = true;
			button.visible = true;
			this.addRenderableWidget(button);
		}

		if (NCRConfig.getClient().showNCRButton()) {
			var button = new AdvancedImageButton(this.width/2 + 158, this.height - 28, 20, 20,
					NCRConfig.getClient().enableMod() ? 0 : 20, 0, 20, TOGGLE_TEXTURE, 64, 64,
							btn -> {
								NCRConfig.getClient().toggleMod();
								boolean enabled = NCRConfig.getClient().enableMod();

								if (enabled) {
									((ImageButton)btn).xTexStart = 0;
									ClientChannelHandler.INSTANCE.register();
								} else {
									((ImageButton)btn).xTexStart = 20;
									ClientChannelHandler.INSTANCE.unregister();
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
