package com.aizistral.nochatreports.common.mixins.client;

import java.util.Arrays;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.GuiMessageTag;
import net.minecraft.client.GuiMessageTag.Icon;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

@Mixin(GuiMessageTag.Icon.class)
public abstract class MixinGuiMessageTagIcon {
	private static final ResourceLocation TEXTURE_NCR = new ResourceLocation("nochatreports", "textures/gui/encrypted_tag.png");
	@Shadow(aliases = "field_39768") @Mutable static Icon[] $VALUES;

	@Invoker("<init>")
	static Icon create(String name, int ordinal, int u, int v, int width, int height) {
		throw new IllegalStateException("Invoker transformation failed");
	}

	@Inject(method = "draw", at = @At("HEAD"), cancellable = true)
	public void onDraw(GuiGraphics graphics, int x, int y, CallbackInfo info) {
		if (((Icon)(Object)this).name().equals("CHAT_NCR_ENCRYPTED")) {
			graphics.blit(TEXTURE_NCR, x, y, 0, 0, 9, 9, 9, 9);
			info.cancel();
		}
	}

	static {
		var ordinal = $VALUES.length;
		var values = Arrays.copyOf($VALUES, ordinal + 1);
		values[ordinal] = create("CHAT_NCR_ENCRYPTED", ordinal, 9, 0, 9, 9);
		$VALUES = values;
	}

}
