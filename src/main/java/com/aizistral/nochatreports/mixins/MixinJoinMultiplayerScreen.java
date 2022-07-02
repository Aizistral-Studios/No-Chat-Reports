package com.aizistral.nochatreports.mixins;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.core.NoReportsConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

@Mixin(JoinMultiplayerScreen.class)
public abstract class MixinJoinMultiplayerScreen extends MixinScreen {
	private static final ResourceLocation RELOAD_TEXTURE = new ResourceLocation("nochatreports", "textures/gui/config_reload_button.png");
	private static final Component RELOAD_TOOLTIP = Component.translatable("gui.nochatrepords.reload_config_tooltip");

	@Inject(method = "init", at = @At("HEAD"))
	private void onInit(CallbackInfo info) {
		var button = new ImageButton(this.width/2 + 158, this.height - 52, 20, 20, 0, 0, 20, RELOAD_TEXTURE,
				64, 64, btn -> NoReportsConfig.loadConfig(), (btn, poseStack, i, j) ->
				this.renderTooltipNoGap(poseStack, this.minecraft.font.split(RELOAD_TOOLTIP, 250), i, j),
				Component.translatable("gui.nochatrepords.reload_config"));
		button.active = true;
		button.visible = true;
		this.addRenderableWidget(button);
	}

}
