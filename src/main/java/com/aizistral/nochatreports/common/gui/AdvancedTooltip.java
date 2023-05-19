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
import net.minecraft.client.gui.GuiGraphics;
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

	public static List<FormattedCharSequence> splitTooltip(Minecraft minecraft, Component component, int maxWidth) {
		return minecraft.font.split(component, maxWidth);
	}

	public void doCustomRender(Screen screen, GuiGraphics graphics, int x, int y, ClientTooltipPositioner positioner) {
		if (this.renderWithoutGap) {
			this.renderTooltipNoGap(screen, graphics, splitTooltip(screen.minecraft, this.getMessage(), this.maxWidth), x, y, positioner);
		} else
			throw new UnsupportedOperationException("This tooltip doesn't support custom render!");
	}

	protected void renderTooltipNoGap(Screen screen, GuiGraphics poseStack, List<? extends FormattedCharSequence> list, int x, int y, ClientTooltipPositioner positioner) {
		this.renderTooltipInternalNoGap(screen, poseStack, list.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), x, y, positioner);
	}

	// Originates from GuiGraphics
	protected void renderTooltipInternalNoGap(Screen screen, GuiGraphics graphics, List<ClientTooltipComponent> list, int i, int j, ClientTooltipPositioner clientTooltipPositioner) {
		ClientTooltipComponent clientTooltipComponent2;
		int t;
		if (list.isEmpty())
			return;
		int k = 0;
		int l = list.size() == 1 ? -2 : /*0*/ -2;
		for (ClientTooltipComponent clientTooltipComponent : list) {
			int m = clientTooltipComponent.getWidth(screen.font);
			if (m > k) {
				k = m;
			}
			l += clientTooltipComponent.getHeight();
		}
		int n = k;
		int o = l;
		Vector2ic vector2ic = clientTooltipPositioner.positionTooltip(graphics.guiWidth(), graphics.guiHeight(), i, j, n, o);
		int p = vector2ic.x();
		int q = vector2ic.y();
		graphics.pose().pushPose();
		int r = 400;
		graphics.drawManaged(() -> TooltipRenderUtil.renderTooltipBackground(graphics, p, q, n, o, 400));
		graphics.pose().translate(0.0f, 0.0f, 400.0f);
		int s = q;
		for (t = 0; t < list.size(); ++t) {
			clientTooltipComponent2 = list.get(t);
			clientTooltipComponent2.renderText(screen.font, p, s, graphics.pose().last().pose(), graphics.bufferSource());
			s += clientTooltipComponent2.getHeight() + /*(t == 0 ? 2 : 0)*/ 0;
		}
		s = q;
		for (t = 0; t < list.size(); ++t) {
			clientTooltipComponent2 = list.get(t);
			clientTooltipComponent2.renderImage(screen.font, p, s, graphics);
			s += clientTooltipComponent2.getHeight() + /*(t == 0 ? 2 : 0)*/ 0;
		}
		graphics.pose().popPose();
	}

}
