package com.aizistral.nochatreports.gui;

import com.aizistral.nochatreports.gui.AdvancedTooltip;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class AdvancedTooltip extends Tooltip {
	@Nullable
	protected Supplier<Component> supplier;
	protected int maxWidth = MAX_WIDTH;
	protected boolean renderWithoutGap = false;

	public AdvancedTooltip(Component message, @Nullable Component narration) {
		super(message, narration);
	}

	public AdvancedTooltip(Component message) {
		this(message, message);
	}

	public AdvancedTooltip(Supplier<Component> message) {
		this(message.get());
		this.supplier = message;
	}

	public AdvancedTooltip setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
		return this;
	}

	public AdvancedTooltip setRenderWithoutGap(boolean render) {
		this.renderWithoutGap = render;
		return this;
	}

	public Component getMessage() {
		return this.supplier != null ? this.supplier.get() : this.message;
	}

	@Override
	public List<FormattedCharSequence> toCharSequence(Minecraft minecraft) {
		if (this.supplier == null) {
			if (this.cachedTooltip == null) {
				this.cachedTooltip = splitTooltip(minecraft, this.getMessage(), this.maxWidth);
			}
			return this.cachedTooltip;
		} else
			return splitTooltip(minecraft, this.getMessage(), this.maxWidth);
	}

	public boolean hasCustomRender() {
		return this.renderWithoutGap;
	}

	public void doCustomRender(Screen screen, PoseStack poseStack, int x, int y) {
		if (this.renderWithoutGap) {
			this.renderTooltipNoGap(screen, poseStack, splitTooltip(screen.minecraft, this.getMessage(), this.maxWidth), x, y);
		} else
			throw new UnsupportedOperationException("This tooltip doesn't support custom render!");
	}

	public static List<FormattedCharSequence> splitTooltip(Minecraft minecraft, Component component, int maxWidth) {
		return minecraft.font.split(component, maxWidth);
	}

	protected void renderTooltipNoGap(Screen screen, PoseStack poseStack, List<? extends FormattedCharSequence> list, int x, int y) {
		this.renderTooltipInternalNoGap(screen, poseStack, list.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), x, y);
	}

	protected void renderTooltipInternalNoGap(Screen screen, PoseStack poseStack, List<ClientTooltipComponent> list, int x, int y) {
		ClientTooltipComponent clientTooltipComponent2;
		int v;
		int m;
		if (list.isEmpty())
			return;
		int k = 0;
		int l = list.size() == 1 ? -2 : -2;
		for (ClientTooltipComponent clientTooltipComponent : list) {
			m = clientTooltipComponent.getWidth(screen.font);
			if (m > k) {
				k = m;
			}
			l += clientTooltipComponent.getHeight();
		}
		int n = x + 12;
		int o = y - 12;
		m = k;
		int p = l;
		if (n + k > screen.width) {
			n -= 28 + k;
		}
		if (o + p + 6 > screen.height) {
			o = screen.height - p - 6;
		}
		if (y - p - 8 < 0) {
			o = y + 8;
		}
		poseStack.pushPose();
		int q = -267386864;
		int r = 0x505000FF;
		int s = 1344798847;
		int t = 400;
		float f = screen.itemRenderer.blitOffset;
		screen.itemRenderer.blitOffset = 400.0f;
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferBuilder = tesselator.getBuilder();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		Matrix4f matrix4f = poseStack.last().pose();
		fillGradient(matrix4f, bufferBuilder, n - 3, o - 4, n + m + 3, o - 3, 400, -267386864, -267386864);
		fillGradient(matrix4f, bufferBuilder, n - 3, o + p + 3, n + m + 3, o + p + 4, 400, -267386864, -267386864);
		fillGradient(matrix4f, bufferBuilder, n - 3, o - 3, n + m + 3, o + p + 3, 400, -267386864, -267386864);
		fillGradient(matrix4f, bufferBuilder, n - 4, o - 3, n - 3, o + p + 3, 400, -267386864, -267386864);
		fillGradient(matrix4f, bufferBuilder, n + m + 3, o - 3, n + m + 4, o + p + 3, 400, -267386864, -267386864);
		fillGradient(matrix4f, bufferBuilder, n - 3, o - 3 + 1, n - 3 + 1, o + p + 3 - 1, 400, 0x505000FF, 1344798847);
		fillGradient(matrix4f, bufferBuilder, n + m + 2, o - 3 + 1, n + m + 3, o + p + 3 - 1, 400, 0x505000FF, 1344798847);
		fillGradient(matrix4f, bufferBuilder, n - 3, o - 3, n + m + 3, o - 3 + 1, 400, 0x505000FF, 0x505000FF);
		fillGradient(matrix4f, bufferBuilder, n - 3, o + p + 2, n + m + 3, o + p + 3, 400, 1344798847, 1344798847);
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
			clientTooltipComponent2.renderText(screen.font, n, u, matrix4f, bufferSource);
			u += clientTooltipComponent2.getHeight() /*+ (v == 0 ? 2 : 0)*/;
		}
		bufferSource.endBatch();
		poseStack.popPose();
		u = o;
		for (v = 0; v < list.size(); ++v) {
			clientTooltipComponent2 = list.get(v);
			clientTooltipComponent2.renderImage(screen.font, n, u, poseStack, screen.itemRenderer, 400);
			u += clientTooltipComponent2.getHeight() + (v == 0 ? 2 : 0);
		}
		screen.itemRenderer.blitOffset = f;
	}

	protected static void fillGradient(Matrix4f matrix4f, BufferBuilder bufferBuilder, int i, int j, int k, int l, int m, int n, int o) {
		float f = (n >> 24 & 0xFF) / 255.0f;
		float g = (n >> 16 & 0xFF) / 255.0f;
		float h = (n >> 8 & 0xFF) / 255.0f;
		float p = (n & 0xFF) / 255.0f;
		float q = (o >> 24 & 0xFF) / 255.0f;
		float r = (o >> 16 & 0xFF) / 255.0f;
		float s = (o >> 8 & 0xFF) / 255.0f;
		float t = (o & 0xFF) / 255.0f;
		bufferBuilder.vertex(matrix4f, k, j, m).color(g, h, p, f).endVertex();
		bufferBuilder.vertex(matrix4f, i, j, m).color(g, h, p, f).endVertex();
		bufferBuilder.vertex(matrix4f, i, l, m).color(r, s, t, q).endVertex();
		bufferBuilder.vertex(matrix4f, k, l, m).color(r, s, t, q).endVertex();
	}

}
