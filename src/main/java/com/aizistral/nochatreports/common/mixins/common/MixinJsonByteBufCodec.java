package com.aizistral.nochatreports.common.mixins.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerStatusCache;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.ByteBufCodecs;

@Mixin(targets = "net.minecraft.network.codec.ByteBufCodecs$34")
public class MixinJsonByteBufCodec {
	@Shadow @Final
	private static Gson GSON;

	// This seems to just be a character limit so not important
	// @Shadow @Final
	// private int val$p_422673_;

	@Inject(method = "encode(Lio/netty/buffer/ByteBuf;Lcom/google/gson/JsonElement;)V", at = @At("HEAD"), cancellable = true)
	private void onEncode(ByteBuf buf, JsonElement element, CallbackInfo info) {
		if (!NCRConfig.getCommon().addQueryData() || !this.isServerStatusElement(element))
			return;

		info.cancel();

		if (NCRConfig.getCommon().enableDebugLog()) {
			NCRCore.LOGGER.info("Adding chat report prevention status to ServerStatus packet.");
		}

		JsonObject object = element.getAsJsonObject();
		object.addProperty("preventsChatReports", true);

		String string = GSON.toJson(object);
		Utf8String.write(buf, string, 32767);
	}

	@Inject(method = "decode(Lio/netty/buffer/ByteBuf;)Lcom/google/gson/JsonElement;", at = @At("RETURN"), cancellable = true)
	private void onDecode(ByteBuf buf, CallbackInfoReturnable<JsonElement> info) {
		JsonElement element = info.getReturnValue();

		if (!this.isServerStatusElement(element))
			return;

		JsonObject object = element.getAsJsonObject();
		boolean preventsReports = object.has("preventsChatReports") && object.get("preventsChatReports").getAsBoolean();

		if (NCRConfig.getCommon().enableDebugLog()) {
			NCRCore.LOGGER.info("Received chat report prevention status from ServerStatus packet: " + preventsReports);
		}

		ServerStatusCache.setPreventsReports(preventsReports);
	}

	private boolean isServerStatusElement(JsonElement element) {
		if (!element.isJsonObject())
			return false;

		JsonObject object = element.getAsJsonObject();

		if (!object.has("version") || !object.get("version").isJsonObject())
			return false;

		JsonObject version = object.get("version").getAsJsonObject();

		return version.has("name") && version.has("protocol");
	}

}
