package com.aizistral.nochatreports.common.gui;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector2ic;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

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
		this.renderTooltipInternalNoGap(screen, poseStack, list.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), x, y, DefaultTooltipPositioner.INSTANCE);
	}

	protected void renderTooltipInternalNoGap(Screen screen, PoseStack poseStack, List<ClientTooltipComponent> list, int i2, int j2, ClientTooltipPositioner clientTooltipPositioner) {
		ClientTooltipComponent clientTooltipComponent2;
		int t;
		if (list.isEmpty())
			return;
		int k2 = 0;
		int l2 = list.size() == 1 ? -2 : -2;
		for (ClientTooltipComponent clientTooltipComponent : list) {
			int m2 = clientTooltipComponent.getWidth(screen.font);
			if (m2 > k2) {
				k2 = m2;
			}
			l2 += clientTooltipComponent.getHeight();
		}
		int n2 = k2;
		int o2 = l2;
		Vector2ic vector2ic = clientTooltipPositioner.positionTooltip(screen, i2, j2, n2, o2);
		int p = vector2ic.x();
		int q = vector2ic.y();
		poseStack.pushPose();
		int r = 400;
		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder bufferBuilder2 = tesselator.getBuilder();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		bufferBuilder2.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		Matrix4f matrix4f2 = poseStack.last().pose();
		TooltipRenderUtil.renderTooltipBackground((matrix4f, bufferBuilder, i, j, k, l, m, n, o) -> fillGradient(matrix4f, bufferBuilder, i, j, k, l, m, n, o), matrix4f2, bufferBuilder2, p, q, n2, o2, 400);
		RenderSystem.enableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		BufferUploader.drawWithShader(bufferBuilder2.end());
		MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
		poseStack.translate(0.0f, 0.0f, 400.0f);
		int s = q;
		for (t = 0; t < list.size(); ++t) {
			clientTooltipComponent2 = list.get(t);
			clientTooltipComponent2.renderText(screen.font, p, s, matrix4f2, bufferSource);
			s += clientTooltipComponent2.getHeight() + (t == 0 ? 2 : 0);
		}
		bufferSource.endBatch();
		poseStack.popPose();
		s = q;
		for (t = 0; t < list.size(); ++t) {
			clientTooltipComponent2 = list.get(t);
			clientTooltipComponent2.renderImage(screen.font, p, s, poseStack, screen.itemRenderer, 400);
			s += clientTooltipComponent2.getHeight() + (t == 0 ? 2 : 0);
		}
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
