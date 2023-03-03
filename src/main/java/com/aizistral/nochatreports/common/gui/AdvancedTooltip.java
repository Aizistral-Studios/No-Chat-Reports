package com.aizistral.nochatreports.common.gui;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector2ic;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class AdvancedTooltip extends Tooltip {
	@Nullable
	protected Supplier<Text> supplier;
	protected int maxWidth = ROW_LENGTH;
	protected boolean renderWithoutGap = false;

	public AdvancedTooltip(Text message, @Nullable Text narration) {
		super(message, narration);
	}

	public AdvancedTooltip(Text message) {
		this(message, message);
	}

	public AdvancedTooltip(Supplier<Text> message) {
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

	public Text getMessage() {
		return this.supplier != null ? this.supplier.get() : this.content;
	}

	@Override
	public List<OrderedText> getLines(MinecraftClient minecraft) {
		if (this.supplier == null) {
			if (this.lines == null) {
				this.lines = splitTooltip(minecraft, this.getMessage(), this.maxWidth);
			}
			return this.lines;
		} else
			return splitTooltip(minecraft, this.getMessage(), this.maxWidth);
	}

	public boolean hasCustomRender() {
		return this.renderWithoutGap;
	}

	public void doCustomRender(Screen screen, MatrixStack poseStack, int x, int y, TooltipPositioner positioner) {
		if (this.renderWithoutGap) {
			this.renderTooltipNoGap(screen, poseStack, splitTooltip(screen.client, this.getMessage(), this.maxWidth), x, y, positioner);
		} else
			throw new UnsupportedOperationException("This tooltip doesn't support custom render!");
	}

	public static List<OrderedText> splitTooltip(MinecraftClient minecraft, Text component, int maxWidth) {
		return minecraft.textRenderer.wrapLines(component, maxWidth);
	}

	protected void renderTooltipNoGap(Screen screen, MatrixStack poseStack, List<? extends OrderedText> list, int x, int y, TooltipPositioner positioner) {
		this.renderTooltipInternalNoGap(screen, poseStack, list.stream().map(TooltipComponent::of).collect(Collectors.toList()), x, y, positioner);
	}

	protected void renderTooltipInternalNoGap(Screen screen, MatrixStack poseStack, List<TooltipComponent> list, int i2, int j2, TooltipPositioner clientTooltipPositioner) {
		TooltipComponent clientTooltipComponent2;
		int t;
		if (list.isEmpty())
			return;
		int k2 = 0;
		int l2 = list.size() == 1 ? -2 : /*0*/ -2;
		for (TooltipComponent clientTooltipComponent : list) {
			int m2 = clientTooltipComponent.getWidth(screen.textRenderer);
			if (m2 > k2) {
				k2 = m2;
			}
			l2 += clientTooltipComponent.getHeight();
		}
		int n2 = k2;
		int o2 = l2;
		Vector2ic vector2ic = clientTooltipPositioner.getPosition(screen, i2, j2, n2, o2);
		int p = vector2ic.x();
		int q = vector2ic.y();
		poseStack.push();
		int r = 400;
		Tessellator tesselator = Tessellator.getInstance();
		BufferBuilder bufferBuilder2 = tesselator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
		bufferBuilder2.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		Matrix4f matrix4f2 = poseStack.peek().getPositionMatrix();
		TooltipBackgroundRenderer.render((matrix4f, bufferBuilder, i, j, k, l, m, n, o) -> fillGradient(matrix4f, bufferBuilder, i, j, k, l, m, n, o), matrix4f2, bufferBuilder2, p, q, n2, o2, 400);
		RenderSystem.enableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		BufferRenderer.drawWithGlobalProgram(bufferBuilder2.end());
		VertexConsumerProvider.Immediate bufferSource = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
		poseStack.translate(0.0f, 0.0f, 400.0f);
		int s = q;
		for (t = 0; t < list.size(); ++t) {
			clientTooltipComponent2 = list.get(t);
			clientTooltipComponent2.drawText(screen.textRenderer, p, s, matrix4f2, bufferSource);
			s += clientTooltipComponent2.getHeight() + /*(t == 0 ? 2 : 0)*/ 0;
		}
		bufferSource.draw();
		poseStack.pop();
		s = q;
		for (t = 0; t < list.size(); ++t) {
			clientTooltipComponent2 = list.get(t);
			clientTooltipComponent2.drawItems(screen.textRenderer, p, s, poseStack, screen.itemRenderer, 400);
			s += clientTooltipComponent2.getHeight() + /*(t == 0 ? 2 : 0)*/ 0;
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
		bufferBuilder.vertex(matrix4f, k, j, m).color(g, h, p, f).next();
		bufferBuilder.vertex(matrix4f, i, j, m).color(g, h, p, f).next();
		bufferBuilder.vertex(matrix4f, i, l, m).color(r, s, t, q).next();
		bufferBuilder.vertex(matrix4f, k, l, m).color(r, s, t, q).next();
	}

}
