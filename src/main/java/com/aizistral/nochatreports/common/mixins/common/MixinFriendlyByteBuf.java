package com.aizistral.nochatreports.common.mixins.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.common.config.NCRConfig;
import com.aizistral.nochatreports.common.core.ServerDataExtension;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;

import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.util.GsonHelper;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Mixin(FriendlyByteBuf.class)
public abstract class MixinFriendlyByteBuf {
	@Shadow @Final
	private static Gson GSON;

	@Inject(method = "readJsonWithCodec", at = @At("HEAD"), cancellable = true)
	private void onReadJsonWithCodec(Codec codec, CallbackInfoReturnable info) throws Throwable {
		if (codec == ServerStatus.CODEC) {
			info.cancel();

			JsonElement jsonElement = GsonHelper.fromJson(GSON, this.readUtf(), JsonElement.class);
			DataResult dataResult = codec.parse(JsonOps.INSTANCE, jsonElement);
			Object result = dataResult.getOrThrow(string -> new DecoderException("Failed to decode json: " + string));

			if (jsonElement.getAsJsonObject().has("preventsChatReports")) {
				((ServerDataExtension) result).setPreventsChatReports(jsonElement.getAsJsonObject()
						.get("preventsChatReports").getAsBoolean());
			}

			info.setReturnValue(result);
		}
	}

	@Inject(method = "writeJsonWithCodec", at = @At("HEAD"), cancellable = true)
	private void onWriteJsonWithCodec(Codec codec, Object object, CallbackInfo info) throws Exception {
		if (!NCRConfig.getCommon().addQueryData())
			return;

		if (codec == ServerStatus.CODEC) {
			info.cancel();

			DataResult<JsonElement> dataResult = codec.encodeStart(JsonOps.INSTANCE, object);
			JsonElement element = dataResult.getOrThrow(string -> new EncoderException("Failed to encode: " + string + " " + object));

			element.getAsJsonObject().addProperty("preventsChatReports", true);

			this.writeUtf(GSON.toJson(element));
		}
	}

	@Shadow
	public abstract String readUtf();

	@Shadow
	public abstract FriendlyByteBuf writeUtf(String string);
}
