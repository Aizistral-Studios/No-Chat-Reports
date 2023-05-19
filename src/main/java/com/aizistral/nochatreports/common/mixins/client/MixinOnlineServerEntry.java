package com.aizistral.nochatreports.common.mixins.client;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

@Mixin(ServerSelectionList.OnlineServerEntry.class)
public abstract class MixinOnlineServerEntry extends ServerSelectionList.Entry {
	private static final ResourceLocation VERIFIED_ICON = new ResourceLocation("nochatreports", "textures/gui/verified_server.png");

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

			RenderSystem.enableBlend();
			graphics.blit(VERIFIED_ICON, k + l - 35 + xOffset, j - 1 + yOffset, 0.0f, 0.0f, 14, 14, 14, 14);
			RenderSystem.disableBlend();

			int t = n - k;
			int u = o - j;
			if (t >= l - 35 + xOffset && t <= l - 22 + xOffset && u >= 0 + yOffset && u <= 11 + yOffset) {
				this.screen.setToolTip(FontHelper.wrap(this.minecraft.font, Language.getInstance()
						.getOrDefault("gui.nochatreports.verified_server"), 250).stream()
						.map(Component::literal).collect(Collectors.toCollection(() ->
						new ArrayList<Component>())));
			}
		}
	}

}
