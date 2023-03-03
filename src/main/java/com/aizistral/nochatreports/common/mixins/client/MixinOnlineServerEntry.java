package com.aizistral.nochatreports.common.mixins.client;

import java.util.ArrayList;
import java.util.stream.Collectors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerDataExtension;
import com.aizistral.nochatreports.common.gui.FontHelper;
import com.mojang.blaze3d.systems.RenderSystem;

@Mixin(MultiplayerServerListWidget.ServerEntry.class)
public abstract class MixinOnlineServerEntry extends MultiplayerServerListWidget.Entry {
	private static final Identifier VERIFIED_ICON = new Identifier("nochatreports", "textures/gui/verified_server.png");

	@Shadow @Final
	private MultiplayerScreen screen;
	@Shadow @Final
	private MinecraftClient minecraft;
	@Shadow @Final
	private ServerInfo serverData;

	@Inject(method = "render", at = @At("RETURN"))
	private void onRender(MatrixStack poseStack, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f, CallbackInfo info) {
		if (!NCRConfig.getClient().verifiedIconEnabled())
			return;

		if (this.serverData.ping >= 0 && ((ServerDataExtension) this.serverData).preventsChatReports()) {
			int xOffset = NCRConfig.getClient().getVerifiedIconOffsetX(),
					yOffset = NCRConfig.getClient().getVerifiedIconOffsetY();

			RenderSystem.setShaderTexture(0, VERIFIED_ICON);
			RenderSystem.enableBlend();
			DrawableHelper.drawTexture(poseStack, k + l - 35 + xOffset, j - 1 + yOffset, 0.0f, 0.0f, 14, 14, 14, 14);
			RenderSystem.disableBlend();

			int t = n - k;
			int u = o - j;
			if (t >= l - 35 + xOffset && t <= l - 22 + xOffset && u >= 0 + yOffset && u <= 11 + yOffset) {
				this.screen.setMultiplayerScreenTooltip(FontHelper.wrap(this.minecraft.textRenderer, Language.getInstance()
						.get("gui.nochatreports.verified_server"), 250).stream()
						.map(Text::literal).collect(Collectors.toCollection(() ->
						new ArrayList<Text>())));
			}
		}
	}

}
