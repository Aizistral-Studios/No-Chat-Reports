package com.aizistral.nochatreports.mixins.client;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.config.NCRConfigLegacy;
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

/**
 * This is responsible for adding config reload button to server selection menu.
 * @author Aizistral
 */

@Mixin(JoinMultiplayerScreen.class)
public abstract class MixinJoinMultiplayerScreen extends Screen {
	private static final ResourceLocation RELOAD_TEXTURE = new ResourceLocation("nochatreports", "textures/gui/config_reload_button.png");
	private static final Component RELOAD_TOOLTIP = Component.translatable("gui.nochatrepords.reload_config_tooltip");

	protected MixinJoinMultiplayerScreen() {
		super(null);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = "init", at = @At("HEAD"))
	private void onInit(CallbackInfo info) {
		if (!NCRConfigLegacy.showReloadButton())
			return;

		var button = new ImageButton(this.width/2 + 158, this.height - 52, 20, 20, 0, 0, 20, RELOAD_TEXTURE,
				64, 64, btn -> NCRConfigLegacy.loadConfig(), (btn, poseStack, i, j) ->
				this.renderTooltipNoGap(poseStack, this.minecraft.font.split(RELOAD_TOOLTIP, 250), i, j),
				Component.translatable("gui.nochatrepords.reload_config"));
		button.active = true;
		button.visible = true;
		this.addRenderableWidget(button);
	}

	protected void renderTooltipNoGap(PoseStack poseStack, List<? extends FormattedCharSequence> list, int i, int j) {
		this.renderTooltipInternalNoGap(poseStack, list.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), i, j);
	}

	protected void renderTooltipInternalNoGap(PoseStack poseStack, List<ClientTooltipComponent> list, int i, int j) {
		ClientTooltipComponent clientTooltipComponent2;
		int v;
		int m;
		if (list.isEmpty())
			return;
		int k = 0;
		int l = list.size() == 1 ? -2 : 0;
		for (ClientTooltipComponent clientTooltipComponent : list) {
			m = clientTooltipComponent.getWidth(this.font);
			if (m > k) {
				k = m;
			}
			l += clientTooltipComponent.getHeight();
		}
		int n = i + 12;
		int o = j - 12;
		m = k;
		int p = l;
		if (n + k > this.width) {
			n -= 28 + k;
		}
		if (o + p + 6 > this.height) {
			o = this.height - p - 6;
		}
		if (j - p - 8 < 0) {
			o = j + 8;
		}
		poseStack.pushPose();
		int q = -267386864;
		int r = 0x505000FF;
		int s = 1344798847;
		int t = 400;
		float f = this.itemRenderer.blitOffset;
		this.itemRenderer.blitOffset = 400.0f;
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tesselator.getBuilder();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		Matrix4f matrix4f = poseStack.last().pose();
		Screen.fillGradient(matrix4f, bufferBuilder, n - 3, o - 4, n + m + 3, o - 3, 400, -267386864, -267386864);
		Screen.fillGradient(matrix4f, bufferBuilder, n - 3, o + p + 3, n + m + 3, o + p + 4, 400, -267386864, -267386864);
		Screen.fillGradient(matrix4f, bufferBuilder, n - 3, o - 3, n + m + 3, o + p + 3, 400, -267386864, -267386864);
		Screen.fillGradient(matrix4f, bufferBuilder, n - 4, o - 3, n - 3, o + p + 3, 400, -267386864, -267386864);
		Screen.fillGradient(matrix4f, bufferBuilder, n + m + 3, o - 3, n + m + 4, o + p + 3, 400, -267386864, -267386864);
		Screen.fillGradient(matrix4f, bufferBuilder, n - 3, o - 3 + 1, n - 3 + 1, o + p + 3 - 1, 400, 0x505000FF, 1344798847);
		Screen.fillGradient(matrix4f, bufferBuilder, n + m + 2, o - 3 + 1, n + m + 3, o + p + 3 - 1, 400, 0x505000FF, 1344798847);
		Screen.fillGradient(matrix4f, bufferBuilder, n - 3, o - 3, n + m + 3, o - 3 + 1, 400, 0x505000FF, 0x505000FF);
		Screen.fillGradient(matrix4f, bufferBuilder, n - 3, o + p + 2, n + m + 3, o + p + 3, 400, 1344798847, 1344798847);
		RenderSystem.enableDepthTest();
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		BufferUploader.drawWithShader(bufferBuilder.end());
		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
		MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		poseStack.translate(0.0, 0.0, 400.0);
		int u = o;
		for (v = 0; v < list.size(); ++v) {
			clientTooltipComponent2 = list.get(v);
			clientTooltipComponent2.renderText(this.font, n, u, matrix4f, bufferSource);
			u += clientTooltipComponent2.getHeight() /*+ (v == 0 ? 2 : 0)*/;
		}
		bufferSource.endBatch();
		poseStack.popPose();
		u = o;
		for (v = 0; v < list.size(); ++v) {
			clientTooltipComponent2 = list.get(v);
			clientTooltipComponent2.renderImage(this.font, n, u, poseStack, this.itemRenderer, 400);
			u += clientTooltipComponent2.getHeight() + (v == 0 ? 2 : 0);
		}
		this.itemRenderer.blitOffset = f;
	}

}
