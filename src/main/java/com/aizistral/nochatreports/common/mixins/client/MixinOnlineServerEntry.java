package com.aizistral.nochatreports.common.mixins.client;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerDataExtension;
import com.aizistral.nochatreports.common.gui.FontHelper;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.opengl.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Mixin(ServerSelectionList.OnlineServerEntry.class)
public abstract class MixinOnlineServerEntry extends ServerSelectionList.Entry {
	@Unique
	private static final ResourceLocation VERIFIED_ICON = ResourceLocation.fromNamespaceAndPath("nochatreports", "verified_server");

	@Shadow @Final
	private JoinMultiplayerScreen screen;
	@Shadow @Final
	private Minecraft minecraft;
	@Shadow @Final
	private ServerData serverData;

	@Inject(method = "render", at = @At("RETURN"))
	private void onRender(GuiGraphics graphics, int i, int j, int k, int l, int m, int n, int o, boolean bl, float f, CallbackInfo info) {
		if (!NCRConfig.getClient().verifiedIconEnabled())
			return;

		if (this.serverData.ping >= 0 && ((ServerDataExtension) this.serverData).preventsChatReports()) {
			int xOffset = NCRConfig.getClient().getVerifiedIconOffsetX(),
					yOffset = NCRConfig.getClient().getVerifiedIconOffsetY();

			GlStateManager._enableBlend();
			graphics.blitSprite(RenderType::guiTextured, VERIFIED_ICON, k + l - 35 + xOffset, j - 1 + yOffset, 14, 14);
			GlStateManager._disableBlend();

			int t = n - k;
			int u = o - j;
			if (t >= l - 35 + xOffset && t <= l - 22 + xOffset && u >= 0 + yOffset && u <= 11 + yOffset) {
				this.screen.setTooltipForNextRenderPass(Lists.transform(FontHelper.wrap(this.minecraft.font,
						Language.getInstance().getOrDefault("gui.nochatreports.verified_server"), 250).stream()
						.map(Component::literal).collect(Collectors.toCollection(() ->
						new ArrayList<Component>())), Component::getVisualOrderText));
			}
		}
	}

}
