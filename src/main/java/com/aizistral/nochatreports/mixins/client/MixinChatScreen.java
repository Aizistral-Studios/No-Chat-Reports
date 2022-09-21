package com.aizistral.nochatreports.mixins.client;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.config.NCRConfig;
import com.aizistral.nochatreports.config.NCRConfigClient;
import com.aizistral.nochatreports.core.ServerSafetyLevel;
import com.aizistral.nochatreports.core.ServerSafetyState;
import com.aizistral.nochatreports.gui.EncryptionConfigScreen;
import com.aizistral.nochatreports.gui.EncryptionWarningScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

/**
 * This is responsible for adding safety status indicator to the bottom-right corner of chat screen.
 * @author Aizistral
 */

@Mixin(ChatScreen.class)
public abstract class MixinChatScreen extends Screen {
	private static final ResourceLocation CHAT_STATUS_ICONS = new ResourceLocation("nochatreports", "textures/gui/chat_status_icons.png");
	private static final ResourceLocation ENCRYPTION_BUTTON = new ResourceLocation("nochatreports", "textures/gui/encryption_toggle_button.png");

	protected MixinChatScreen() {
		super(null);
		throw new IllegalStateException("Can't touch this");
	}

	@Inject(method = "normalizeChatMessage", at = @At("RETURN"), cancellable = true)
	public void onBeforeMessage(String original, CallbackInfoReturnable<String> info) {
		String message = info.getReturnValue();
		NCRConfig.getEncryption().setLastMessage(message);

		if (!message.isEmpty() && NCRConfig.getEncryption().shouldEncrypt(message)) {
			NCRConfig.getEncryption().getEncryptor().ifPresent(e -> {
				int index = NCRConfig.getEncryption().getEncryptionStartIndex(message);
				String noencrypt = message.substring(0, index);
				String encrypt = message.substring(index, message.length());

				if (encrypt.length() > 0) {
					info.setReturnValue(noencrypt + e.encrypt("#%" + encrypt));
				}
			});
		}
	}

	@Inject(method = "init", at = @At("HEAD"))
	private void onInit(CallbackInfo info) {
		if (!NCRConfig.getClient().showServerSafety() || !NCRConfig.getClient().enableMod())
			return;

		ServerSafetyLevel trust = this.minecraft.isLocalServer() ? ServerSafetyLevel.SECURE : ServerSafetyState.getCurrent();

		var button = new ImageButton(this.width - 23, this.height - 37, 20, 20, this.getXOffset(trust),
				0, 20, CHAT_STATUS_ICONS, 64, 64, btn -> {}, (btn, poseStack, i, j) ->
				this.renderTooltipNoGap(poseStack, this.minecraft.font.split(trust.getTooltip(), 250), i, j),
				Component.empty());
		button.active = false;
		button.visible = true;

		this.addRenderableOnly(button);

		int xStart = !NCRConfig.getEncryption().isValid() ? 40 : (NCRConfig.getEncryption().isEnabled() ? 0 : 20);

		button = new ImageButton(this.width - 48, this.height - 37, 20, 20, xStart,
				0, 20, ENCRYPTION_BUTTON, 64, 64, btn -> {
					if (NCRConfig.getEncryption().isValid()) {
						NCRConfig.getEncryption().toggleEncryption();
						((ImageButton)btn).xTexStart = NCRConfig.getEncryption().isEnabledAndValid() ? 0 : 20;
					} else {
						MixinChatScreen.this.openEncryptionConfig();
					}
				}, (btn, poseStack, i, j) -> {
					if (NCRConfig.getEncryption().isValid()) {
						this.renderTooltip(poseStack, this.minecraft.font.split(
								Component.translatable("gui.nochatreports.encryption_tooltip", Language.getInstance()
										.getOrDefault("gui.nochatreports.encryption_state_" + (NCRConfig.getEncryption()
												.isEnabledAndValid() ? "on" : "off")), 250), 250), i, j);
					} else {
						this.renderTooltip(poseStack, this.minecraft.font.split(
								Component.translatable("gui.nochatreports.encryption_tooltip_invalid", Language.getInstance()
										.getOrDefault("gui.nochatreports.encryption_state_" + (NCRConfig.getEncryption()
												.isEnabledAndValid() ? "on" : "off")), 250), 250), i, j);

					}
				}, Component.empty()) {

			@Override
			public boolean mouseClicked(double x, double y, int i) {
				if (!this.active || !this.visible)
					return false;

				if (i == 1 && this.clicked(x, y)) {
					this.playDownSound(Minecraft.getInstance().getSoundManager());
					MixinChatScreen.this.openEncryptionConfig();
					return true;
				}

				return super.mouseClicked(x, y, i);
			}

		};
		button.active = true;
		button.visible = true;

		this.addRenderableWidget(button);
	}

	private void openEncryptionConfig() {
		if (!EncryptionWarningScreen.seenOnThisSession() && !NCRConfig.getEncryption().isWarningDisabled()
				&& !NCRConfig.getEncryption().isEnabledAndValid()) {
			Minecraft.getInstance().setScreen(new EncryptionWarningScreen(this));
		} else {
			Minecraft.getInstance().setScreen(new EncryptionConfigScreen(this));
		}
	}

	private int getXOffset(ServerSafetyLevel level) {
		return switch (level) {
		case SECURE -> 21;
		case UNINTRUSIVE, UNKNOWN -> 42;
		case INSECURE -> 0;
		};
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
		int l = list.size() == 1 ? -2 : -2;
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
