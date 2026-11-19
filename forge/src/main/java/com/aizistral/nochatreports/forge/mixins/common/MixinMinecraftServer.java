package com.aizistral.nochatreports.forge.mixins.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.common.NCRCore;
import com.aizistral.nochatreports.common.config.NCRConfig;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {
	@Unique
	private volatile Pair<String, String> nochatreports$statusJson;

	@Inject(method = "getStatusJson", at = @At("RETURN"), cancellable = true, remap = false)
	private void onGetStatusJson(CallbackInfoReturnable<String> info) {
		String json = info.getReturnValue();

		if (json == null || !NCRConfig.getCommon().addQueryData())
			return;

		Pair<String, String> cached = this.nochatreports$statusJson;

		if (cached == null || cached.getFirst() != json) {
			cached = Pair.of(json, this.nochatreports$addStatusFlag(json));
			this.nochatreports$statusJson = cached;
		}

		info.setReturnValue(cached.getSecond());
	}

	@Unique
	private String nochatreports$addStatusFlag(String json) {
		JsonElement element;

		try {
			element = JsonParser.parseString(json);
		} catch (JsonParseException ex) {
			NCRCore.LOGGER.error("Failed to parse cached server status JSON!", ex);
			return json;
		}

		if (!element.isJsonObject()) {
			NCRCore.LOGGER.error("Cached server status JSON is not an object!");
			return json;
		}

		element.getAsJsonObject().addProperty("preventsChatReports", true);
		String patched = new Gson().toJson(element);

		if (patched.length() > FriendlyByteBuf.MAX_STRING_LENGTH) {
			if (NCRConfig.getCommon().enableDebugLog()) {
				NCRCore.LOGGER.info("Cached server status JSON is too long to add chat report prevention status.");
			}

			return json;
		}

		return patched;
	}

}
