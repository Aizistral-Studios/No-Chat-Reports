package com.aizistral.nochatreports.common.mixins.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerStatusCache;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.ByteBufCodecs;

@Mixin(targets = "net.minecraft.network.codec.ByteBufCodecs$35")
public class MixinJsonByteBufCodec {
	@Shadow @Final
	private static Gson GSON;

	// This seems to just be a character limit so not important
	// @Shadow @Final
	// private int val$p_422673_;

	@Inject(method = "encode", at = @At("HEAD"), cancellable = true)
	private void onEncode(ByteBuf buf, JsonElement element, CallbackInfo info) {
		System.out.println("ONENECODE WORKIN");

		System.out.println("ELEMENT: " + element.toString());

		if (!NCRConfig.getCommon().addQueryData() || !this.isServerStatusElement(element))
			return;

		System.out.println("ADDING INFO TO BUF");

		info.cancel();

		JsonObject object = element.getAsJsonObject();
		object.addProperty("preventsChatReports", true);

		String string = GSON.toJson(object);
		Utf8String.write(buf, string, 32767);
	}

	@Inject(method = "decode", at = @At("RETURN"), cancellable = true)
	private void onDecode(ByteBuf buf, CallbackInfoReturnable<JsonElement> info) {
		System.out.println("ONDECODE WORKIN");
		JsonElement element = info.getReturnValue();

		if (!this.isServerStatusElement(element))
			return;

		System.out.println("getting INFO frim BUF");

		JsonObject object = element.getAsJsonObject();
		boolean preventsReports = object.has("preventsChatReports") && object.get("preventsChatReports").getAsBoolean();

		System.out.println("PREVENTS REPORTS: " + (object.has("preventsChatReports") && object.get("preventsChatReports").getAsBoolean()));

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
